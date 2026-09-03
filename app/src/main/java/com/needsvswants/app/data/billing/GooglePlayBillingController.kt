package com.needsvswants.app.data.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult as PlayBillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsResult
import com.android.billingclient.api.QueryPurchasesParams
import com.needsvswants.app.BuildConfig
import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.remote.HttpJsonClient
import com.needsvswants.app.data.remote.SupabaseConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * An owned Google Play subscription tracked from the controller's verified
 * purchase stream. Used as the replacement source when the user changes
 * subscription (e.g. Pro -> Max) so Play replaces the old purchase instead of
 * letting both subscriptions coexist.
 */
internal data class ActivePlaySubscription(val productId: String, val purchaseToken: String)

/**
 * Replacement decision derived from [ActivePlaySubscription].
 * [replacementMode] pins the proration policy: CHARGE_PRORATED_PRICE for
 * Pro -> Max changes.
 */
internal data class SubscriptionReplacementSpec(
    val oldPurchaseToken: String,
    val oldProductId: String
) {
    val replacementMode: Int =
        BillingFlowParams.ProductDetailsParams.SubscriptionProductReplacementParams.ReplacementMode.CHARGE_PRORATED_PRICE
}

/** Minimal, unit-testable view of a Play purchase needed by the restore pipeline. */
internal data class RestorablePurchase(
    val purchaseToken: String,
    val productIds: List<String>,
    val isAcknowledged: Boolean
)

/**
 * Typed outcome of the restore pipeline. Success may only be reported when
 * restoration genuinely completed (>= 1 purchase verified) or the query
 * definitively found no owned subscriptions; a missing auth token or failed
 * verification is [RestoreOutcome.Error] / [RestoreOutcome.NoneFound], never
 * success.
 */
internal sealed interface RestoreOutcome {
    /** Restoration completed: [verifiedCount] purchases passed server verification. */
    data class Restored(
        val verifiedCount: Int,
        /** Tokens whose acknowledgement failed after successful verification (surfaced, never swallowed). */
        val ackFailedTokens: List<String> = emptyList()
    ) : RestoreOutcome

    /** Query succeeded and Google Play definitively holds no owned subscriptions. */
    data object NoneFound : RestoreOutcome

    /** Query or verification failed; restoration did NOT complete. */
    data class Error(val reason: String) : RestoreOutcome
}

/** Result of the awaited Play purchases query during restore. */
internal sealed interface OwnedQueryResult {
    data class Ok(val purchases: List<Purchase>) : OwnedQueryResult
    data class Error(val reason: String) : OwnedQueryResult
}

/** Maps the typed restore outcome onto the UI-facing [BillingResult] contract. */
internal fun RestoreOutcome.toBillingResult(): BillingResult = when (this) {
    is RestoreOutcome.Restored -> BillingResult.Success
    is RestoreOutcome.NoneFound -> BillingResult.Failed("No active Google Play subscriptions found.")
    is RestoreOutcome.Error -> BillingResult.Failed(reason)
}

/**
 * Google Play Billing client & controller for Google Play Store distribution.
 *
 * Implements [BillingController] for Google Play Billing Library (PBL 7.x/9.x KTX):
 * 1. Manages connection lifecycle with automatic reconnection.
 * 2. Queries subscription ProductDetails for [proProductId] and [maxProductId].
 * 3. Exposes live localized formatted pricing strings.
 * 4. Launches in-app billing flow via [launchBillingFlow].
 * 5. Handles purchase updates, server-side verification via Supabase Edge Function
 *    `google_play_verify`, and purchase acknowledgement.
 * 6. Implements [restorePurchases] against Google Play cache + server verification.
 */
@Singleton
class GooglePlayBillingController @Inject constructor(
    @ApplicationContext private val context: Context,
    private val config: SupabaseConfig,
    private val auth: AuthRepository,
    private val entitlements: EntitlementRepository
) : BillingController, PurchasesUpdatedListener {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val proProductId: String = BuildConfig.PLAY_SUB_PRO.ifBlank { "needsvswants_pro" }
    val maxProductId: String = BuildConfig.PLAY_SUB_MAX.ifBlank { "needsvswants_max" }

    private var billingClient: BillingClient? = null
    private var isConnected = false
    private var reconnectAttempts = 0

    private var currentActivityRef: WeakReference<Activity>? = null

    private val _isPlayAvailableFlow = MutableStateFlow(false)
    val isPlayAvailableFlow: StateFlow<Boolean> = _isPlayAvailableFlow.asStateFlow()

    override val isPlayAvailable: Boolean
        get() = _isPlayAvailableFlow.value

    override val isPayPalAvailable: Boolean
        get() = false

    // Localized formatted pricing strings populated from ProductDetails
    private val _proMonthlyPrice = MutableStateFlow<String?>(null)
    val proMonthlyPrice: StateFlow<String?> = _proMonthlyPrice.asStateFlow()

    private val _proAnnualPrice = MutableStateFlow<String?>(null)
    val proAnnualPrice: StateFlow<String?> = _proAnnualPrice.asStateFlow()

    private val _maxMonthlyPrice = MutableStateFlow<String?>(null)
    val maxMonthlyPrice: StateFlow<String?> = _maxMonthlyPrice.asStateFlow()

    private val _maxAnnualPrice = MutableStateFlow<String?>(null)
    val maxAnnualPrice: StateFlow<String?> = _maxAnnualPrice.asStateFlow()

    // Cached ProductDetails
    private var proProductDetails: ProductDetails? = null
    private var maxProductDetails: ProductDetails? = null

    // Active Play subscription tracked from the verified purchase stream; the
    // replacement source for upgrade/downgrade billing flows.
    @Volatile
    private var activePlaySubscription: ActivePlaySubscription? = null

    /** Attach active activity context for launching Google Play billing sheets. */
    fun setActivity(activity: Activity?) {
        currentActivityRef = if (activity != null) WeakReference(activity) else null
    }

    @Synchronized
    fun ensureClient(): BillingClient? {
        if (billingClient == null) {
            runCatching {
                billingClient = BillingClient.newBuilder(context)
                    .setListener(this)
                    .enablePendingPurchases(
                        PendingPurchasesParams.newBuilder()
                            .enableOneTimeProducts()
                            .build()
                    )
                    .build()
                startConnection()
            }
        }
        return billingClient
    }

    fun startConnection(onConnected: (() -> Unit)? = null) {
        val client = ensureClient() ?: return
        if (isConnected) {
            onConnected?.invoke()
            return
        }

        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: PlayBillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    isConnected = true
                    reconnectAttempts = 0
                    _isPlayAvailableFlow.value = true
                    scope.launch {
                        queryProducts()
                        queryActivePurchases()
                    }
                    onConnected?.invoke()
                } else {
                    isConnected = false
                    _isPlayAvailableFlow.value = false
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnected = false
                _isPlayAvailableFlow.value = false
                retryConnection()
            }
        })
    }

    private fun retryConnection() {
        if (reconnectAttempts >= 5) return
        reconnectAttempts++
        val delayMillis = (1000L * (1 shl reconnectAttempts)).coerceAtMost(30000L)
        scope.launch {
            delay(delayMillis)
            startConnection()
        }
    }

    suspend fun queryProducts() = withContext(Dispatchers.IO) {
        val client = billingClient ?: return@withContext
        if (!isConnected) return@withContext

        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(proProductId)
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(maxProductId)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        client.queryProductDetailsAsync(params) { billingResult, queryResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val products = queryResult.productDetailsList
                val unfetched = queryResult.unfetchedProductList
                if (unfetched.isNotEmpty()) {
                    android.util.Log.w(
                        LOG_TAG,
                        "Unfetched Play products: ${unfetched.joinToString { it.productId }}"
                    )
                }
                for (details in products) {
                    when (details.productId) {
                        proProductId -> {
                            proProductDetails = details
                            updatePrices(details, isPro = true)
                        }
                        maxProductId -> {
                            maxProductDetails = details
                            updatePrices(details, isPro = false)
                        }
                    }
                }
            }
        }
    }

    private fun updatePrices(details: ProductDetails, isPro: Boolean) {
        val monthlyOffer = findOfferDetails(details, BillingPeriod.MONTHLY)
        val annualOffer = findOfferDetails(details, BillingPeriod.ANNUAL)

        val monthlyPrice = monthlyOffer?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice
        val annualPrice = annualOffer?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice

        if (isPro) {
            _proMonthlyPrice.value = monthlyPrice
            _proAnnualPrice.value = annualPrice
        } else {
            _maxMonthlyPrice.value = monthlyPrice
            _maxAnnualPrice.value = annualPrice
        }
    }

    fun findOfferDetails(
        productDetails: ProductDetails,
        period: BillingPeriod
    ): ProductDetails.SubscriptionOfferDetails? {
        val offers = productDetails.subscriptionOfferDetails ?: return null
        val targetPeriodSubstring = if (period == BillingPeriod.ANNUAL) "annual" else "monthly"
        val byId = offers.firstOrNull { it.basePlanId.contains(targetPeriodSubstring, ignoreCase = true) }
        if (byId != null) return byId

        val targetIso = if (period == BillingPeriod.ANNUAL) "P1Y" else "P1M"
        return offers.firstOrNull { offer ->
            offer.pricingPhases.pricingPhaseList.any { it.billingPeriod == targetIso }
        } ?: offers.firstOrNull()
    }

    override suspend fun startTrial(productId: String, period: BillingPeriod): BillingResult {
        return purchase(productId, period)
    }

    override suspend fun purchase(productId: String, period: BillingPeriod): BillingResult {
        val activity = currentActivityRef?.get()
            ?: return BillingResult.Failed("Unable to launch Google Play Store purchase. Please try again.")

        return launchPlayPurchase(activity, productId, period)
    }

    suspend fun launchPlayPurchase(
        activity: Activity,
        productIdHint: String,
        period: BillingPeriod
    ): BillingResult = withContext(Dispatchers.Main) {
        val client = billingClient ?: return@withContext BillingResult.Unavailable
        if (!isConnected) {
            startConnection()
            return@withContext BillingResult.Failed("Connecting to Google Play Store. Please try again in a moment.")
        }

        val isMax = productIdHint == maxProductId ||
            productIdHint == config.maxMonthlyProductId ||
            productIdHint == config.maxAnnualProductId ||
            productIdHint.contains("max", ignoreCase = true)

        val details = if (isMax) maxProductDetails else proProductDetails
        if (details == null) {
            scope.launch { queryProducts() }
            return@withContext BillingResult.Failed("Loading subscription details from Google Play. Please try again.")
        }

        val offer = findOfferDetails(details, period)
            ?: return@withContext BillingResult.Failed("Selected subscription plan unavailable on Google Play.")

        // While a Play subscription is already owned (e.g. Pro -> Max), the flow
        // must carry replacement parameters; without them Play lets both
        // subscriptions coexist and entitlement becomes callback-order dependent.
        val replacement = subscriptionReplacementFor(activePlaySubscription)

        val productDetailsParamsBuilder = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .setOfferToken(offer.offerToken)
        if (replacement != null) {
            productDetailsParamsBuilder.setSubscriptionProductReplacementParams(
                productReplacementParams(replacement)
            )
        }

        val billingFlowParamsBuilder = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParamsBuilder.build()))
        if (replacement != null) {
            billingFlowParamsBuilder.setSubscriptionUpdateParams(subscriptionUpdateParams(replacement))
        }

        // Set obfuscated account id when user is signed in to bind purchase to Supabase account
        val userSession = auth.session.firstOrNull()
        if (userSession?.userId?.isNotBlank() == true) {
            billingFlowParamsBuilder.setObfuscatedAccountId(userSession.userId)
        }

        val billingResult = client.launchBillingFlow(activity, billingFlowParamsBuilder.build())
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            BillingResult.Pending
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            BillingResult.Failed("Purchase cancelled.")
        } else {
            BillingResult.Failed(billingResult.debugMessage.ifBlank { "Google Play purchase failed (code ${billingResult.responseCode})." })
        }
    }

    /**
     * Replacement decision for launching a subscription purchase while a Play
     * subscription is already owned; null launches a plain first-time flow.
     */
    internal fun subscriptionReplacementFor(activeSubscription: ActivePlaySubscription?): SubscriptionReplacementSpec? =
        activeSubscription?.let {
            SubscriptionReplacementSpec(oldPurchaseToken = it.purchaseToken, oldProductId = it.productId)
        }

    /** Per-product replacement params (old product id + proration policy). */
    internal fun productReplacementParams(
        spec: SubscriptionReplacementSpec
    ): BillingFlowParams.ProductDetailsParams.SubscriptionProductReplacementParams =
        BillingFlowParams.ProductDetailsParams.SubscriptionProductReplacementParams.newBuilder()
            .setOldProductId(spec.oldProductId)
            .setReplacementMode(spec.replacementMode)
            .build()

    /** Flow-level replacement params binding the old purchase token. */
    internal fun subscriptionUpdateParams(
        spec: SubscriptionReplacementSpec
    ): BillingFlowParams.SubscriptionUpdateParams =
        BillingFlowParams.SubscriptionUpdateParams.newBuilder()
            .setOldPurchaseToken(spec.oldPurchaseToken)
            .build()

    override fun onPurchasesUpdated(
        billingResult: PlayBillingResult,
        purchases: List<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            scope.launch {
                for (purchase in purchases) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        handlePurchasedItem(purchase)
                    }
                }
            }
        }
    }

    private suspend fun queryActivePurchases() {
        val client = billingClient ?: return
        if (!isConnected) return

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        client.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val purchased = purchases.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
                if (purchased.isEmpty()) {
                    // Play definitively holds no owned subscriptions; drop any
                    // stale tracked replacement source.
                    activePlaySubscription = null
                }
                scope.launch {
                    for (purchase in purchased) {
                        handlePurchasedItem(purchase)
                    }
                }
            }
        }
    }

    private fun recordActivePlaySubscription(purchase: Purchase) {
        val productId = purchase.products.firstOrNull() ?: return
        if (purchase.purchaseToken.isBlank()) return
        activePlaySubscription = ActivePlaySubscription(productId, purchase.purchaseToken)
    }

    private suspend fun handlePurchasedItem(purchase: Purchase) {
        recordActivePlaySubscription(purchase)
        val verified = verifyPurchaseWithBackend(purchase.purchaseToken)
        if (verified) {
            // Awaited; failures are logged inside acknowledgePurchaseIfNeeded.
            acknowledgePurchaseIfNeeded(purchase.purchaseToken, purchase.isAcknowledged)
            val token = auth.ensureFreshAccessToken()
            if (token != null) {
                entitlements.refreshFromRemote(token)
            }
        }
    }

    private suspend fun verifyPurchaseWithBackend(
        purchaseToken: String
    ): Boolean = withContext(Dispatchers.IO) {
        if (!config.enabled) return@withContext false
        val accessToken = auth.ensureFreshAccessToken() ?: return@withContext false
        val url = "${config.url.trimEnd('/')}/functions/v1/google_play_verify"
        val body = """{"package_name":"${context.packageName}","purchase_token":"${purchaseToken.escapeJson()}","kind":"subscription"}"""

        val result = HttpJsonClient.request(
            url = url,
            method = "POST",
            headers = mapOf(
                "apikey" to config.anonKey,
                "Authorization" to "Bearer $accessToken",
                "Content-Type" to "application/json"
            ),
            body = body
        )

        result.map { json ->
            json.contains("\"valid\":true") || json.contains("\"success\":true")
        }.getOrDefault(false)
    }

    /**
     * Acknowledges the purchase when needed, awaiting the Play result (bounded
     * by [ACK_TIMEOUT_MS]). Returns whether the purchase is acknowledged and
     * logs every failure - acknowledgement is never fire-and-forget.
     */
    private suspend fun acknowledgePurchaseIfNeeded(
        purchaseToken: String,
        alreadyAcknowledged: Boolean = false
    ): Boolean = withContext(Dispatchers.IO) {
        if (alreadyAcknowledged) return@withContext true
        val client = billingClient
        if (client == null) {
            // Never log a whole purchase token: it is the bearer credential the
            // backend accepts to grant an entitlement. A prefix is enough to correlate.
            android.util.Log.w(
                LOG_TAG,
                "Cannot acknowledge ${purchaseToken.take(8)}…: BillingClient unavailable."
            )
            return@withContext false
        }
        val ackParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        val result = withTimeoutOrNull(ACK_TIMEOUT_MS) {
            suspendCancellableCoroutine { cont ->
                client.acknowledgePurchase(ackParams) { billingResult ->
                    if (cont.isActive) cont.resume(billingResult)
                }
            }
        }
        val acknowledged = result != null &&
            result.responseCode == BillingClient.BillingResponseCode.OK
        if (!acknowledged) {
            val detail = result?.debugMessage?.takeIf { it.isNotBlank() } ?: "timed out"
            android.util.Log.w(
                LOG_TAG,
                "Acknowledgement failed for ${purchaseToken.take(8)}…: $detail"
            )
        }
        acknowledged
    }

    /**
     * Restores purchases honestly: the Play query and every verification /
     * acknowledgement are awaited, and the reported [BillingResult] reflects
     * what actually happened (see [RestoreOutcome] / [toBillingResult]).
     */
    override suspend fun restorePurchases(): BillingResult = withContext(Dispatchers.IO) {
        val client = billingClient ?: return@withContext BillingResult.Unavailable
        if (!isConnected) {
            startConnection()
            if (!awaitConnected(RESTORE_CONNECT_TIMEOUT_MS)) {
                return@withContext BillingResult.Failed("Unable to connect to Google Play Store.")
            }
        }

        val outcome = when (val query = queryOwnedSubscriptions(client)) {
            is OwnedQueryResult.Error -> RestoreOutcome.Error(query.reason)
            is OwnedQueryResult.Ok -> {
                val candidates = query.purchases
                    .filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
                    .map { RestorablePurchase(it.purchaseToken, it.products, it.isAcknowledged) }
                val processed = processRestoredPurchases(candidates)
                if (processed is RestoreOutcome.NoneFound && query.purchases.isEmpty()) {
                    activePlaySubscription = null
                }
                processed
            }
        }

        if (outcome !is RestoreOutcome.Error) {
            // Sync the local snapshot with server truth for completed restores
            // and confirmed-empty accounts alike.
            val token = auth.ensureFreshAccessToken()
            if (token != null) {
                entitlements.refreshFromRemote(token)
            } else if (outcome is RestoreOutcome.Restored) {
                android.util.Log.w(
                    LOG_TAG,
                    "Restore verified ${outcome.verifiedCount} purchase(s) but no auth token; local entitlement not refreshed."
                )
            }
        }

        outcome.toBillingResult()
    }

    /** Awaited Play query for owned subscriptions, bounded by [RESTORE_QUERY_TIMEOUT_MS]. */
    private suspend fun queryOwnedSubscriptions(client: BillingClient): OwnedQueryResult {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val response = withTimeoutOrNull(RESTORE_QUERY_TIMEOUT_MS) {
            suspendCancellableCoroutine { cont ->
                client.queryPurchasesAsync(params) { billingResult, purchases ->
                    if (cont.isActive) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            cont.resume(OwnedQueryResult.Ok(purchases ?: emptyList()))
                        } else {
                            cont.resume(
                                OwnedQueryResult.Error(
                                    billingResult.debugMessage.ifBlank {
                                        "Google Play purchase query failed (code ${billingResult.responseCode})."
                                    }
                                )
                            )
                        }
                    }
                }
            }
        }
        return response ?: OwnedQueryResult.Error("Timed out querying Google Play purchases.")
    }

    /**
     * Verify/ack core of restore, expressed over [RestorablePurchase] values so
     * it stays unit-testable without a live BillingClient. Verification and
     * acknowledgement are awaited per purchase; failures are never silently
     * swallowed (ack failures are logged and reported on the outcome).
     */
    internal suspend fun processRestoredPurchases(
        purchases: List<RestorablePurchase>,
        verify: suspend (purchaseToken: String) -> Boolean = { verifyPurchaseWithBackend(it) },
        acknowledge: suspend (purchaseToken: String, alreadyAcknowledged: Boolean) -> Boolean =
            { token, alreadyAcknowledged -> acknowledgePurchaseIfNeeded(token, alreadyAcknowledged) }
    ): RestoreOutcome {
        if (purchases.isEmpty()) return RestoreOutcome.NoneFound

        var verifiedCount = 0
        val ackFailedTokens = mutableListOf<String>()
        for (purchase in purchases) {
            if (!verify(purchase.purchaseToken)) continue
            verifiedCount++
            val productId = purchase.productIds.firstOrNull()
            if (productId != null && purchase.purchaseToken.isNotBlank()) {
                activePlaySubscription = ActivePlaySubscription(productId, purchase.purchaseToken)
            }
            if (!acknowledge(purchase.purchaseToken, purchase.isAcknowledged)) {
                ackFailedTokens.add(purchase.purchaseToken)
            }
        }

        if (verifiedCount == 0) {
            // Nothing verified: an error, never success - regardless of tokens.
            val signedIn = auth.ensureFreshAccessToken() != null
            return if (signedIn) {
                RestoreOutcome.Error("Couldn't verify your Google Play subscriptions. Try again.")
            } else {
                RestoreOutcome.Error("Sign in to your account so your Google Play subscriptions can be verified.")
            }
        }
        return RestoreOutcome.Restored(verifiedCount, ackFailedTokens)
    }

    /** Polls until connected or [timeoutMillis] elapsed (replaces fixed sleeps). */
    private suspend fun awaitConnected(timeoutMillis: Long): Boolean {
        val deadline = System.currentTimeMillis() + timeoutMillis
        while (!isConnected && System.currentTimeMillis() < deadline) {
            delay(CONNECT_POLL_INTERVAL_MS)
        }
        return isConnected
    }

    private companion object {
        const val LOG_TAG = "GooglePlayBilling"

        /** Awaited restore query bound; replaces the previous fixed 1.5 s sleep. */
        const val RESTORE_QUERY_TIMEOUT_MS = 10_000L
        const val RESTORE_CONNECT_TIMEOUT_MS = 10_000L
        const val ACK_TIMEOUT_MS = 10_000L
        const val CONNECT_POLL_INTERVAL_MS = 100L
    }

    private fun String.escapeJson(): String =
        replace("\\", "\\\\").replace("\"", "\\\"")
}
