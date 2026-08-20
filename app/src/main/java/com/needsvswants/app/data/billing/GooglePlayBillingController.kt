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
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

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
                        "GooglePlayBilling",
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

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .setOfferToken(offer.offerToken)
            .build()

        val billingFlowParamsBuilder = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))

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
                scope.launch {
                    for (purchase in purchases) {
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            handlePurchasedItem(purchase)
                        }
                    }
                }
            }
        }
    }

    private suspend fun handlePurchasedItem(purchase: Purchase) {
        val verified = verifyPurchaseWithBackend(purchase)
        if (verified) {
            acknowledgePurchaseIfNeeded(purchase)
            val token = auth.ensureFreshAccessToken()
            if (token != null) {
                entitlements.refreshFromRemote(token)
            }
        }
    }

    private suspend fun verifyPurchaseWithBackend(
        purchase: Purchase
    ): Boolean = withContext(Dispatchers.IO) {
        if (!config.enabled) return@withContext false
        val accessToken = auth.ensureFreshAccessToken() ?: return@withContext false
        val url = "${config.url.trimEnd('/')}/functions/v1/google_play_verify"
        val body = """{"package_name":"${context.packageName}","purchase_token":"${purchase.purchaseToken.escapeJson()}","kind":"subscription"}"""

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

    private suspend fun acknowledgePurchaseIfNeeded(purchase: Purchase) = withContext(Dispatchers.IO) {
        val client = billingClient ?: return@withContext
        if (!purchase.isAcknowledged) {
            val ackParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            client.acknowledgePurchase(ackParams) { /* acknowledged */ }
        }
    }

    override suspend fun restorePurchases(): BillingResult = withContext(Dispatchers.IO) {
        val client = billingClient ?: return@withContext BillingResult.Unavailable
        if (!isConnected) {
            startConnection()
            delay(1000)
            if (!isConnected) return@withContext BillingResult.Failed("Unable to connect to Google Play Store.")
        }

        var foundActive = false
        var lastError: String? = null

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        client.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                scope.launch {
                    for (purchase in purchases) {
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            val verified = verifyPurchaseWithBackend(purchase)
                            if (verified) {
                                foundActive = true
                                acknowledgePurchaseIfNeeded(purchase)
                            }
                        }
                    }
                    val token = auth.ensureFreshAccessToken()
                    if (token != null) {
                        entitlements.refreshFromRemote(token)
                    }
                }
            } else {
                lastError = billingResult.debugMessage
            }
        }

        delay(1500) // Allow async verification to settle
        val token = auth.ensureFreshAccessToken()
        if (token != null) {
            entitlements.refreshFromRemote(token)
            BillingResult.Success
        } else {
            if (foundActive) BillingResult.Success
            else BillingResult.Failed(lastError ?: "No active Google Play subscriptions found.")
        }
    }

    private fun String.escapeJson(): String =
        replace("\\", "\\\\").replace("\"", "\\\"")
}
