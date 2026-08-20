package com.needsvswants.app.data.billing

import com.needsvswants.app.BuildConfig
import com.needsvswants.app.data.remote.SupabaseConfig
import javax.inject.Inject
import javax.inject.Singleton

/** Payment provider the user picked for checkout on the paywall. */
enum class PaymentProvider {
    /** PayPal auto-charging subscription; Pro carries the 3-day trial on the plan. */
    PAYPAL,
    /** PayMongo one-time hosted checkout (GCash / card), manual monthly renewal. */
    PAYMONGO,
    /** Google Play In-App Billing (Play Store distribution). */
    GOOGLE_PLAY
}

/**
 * Resolves the concrete [BillingController] for a [PaymentProvider] and reports
 * per-provider availability.
 */
interface CheckoutProvider {
    /** True when running inside a Google Play Store build. */
    val isPlayStoreBuild: Boolean
        get() = false

    /** True when PayPal checkout can be started (Supabase enabled + plan ids). */
    val payPalAvailable: Boolean

    /** True when PayMongo checkout can be started (Supabase enabled + plan ids). */
    val payMongoAvailable: Boolean

    /** The controller that executes checkouts for [provider]. */
    fun controllerFor(provider: PaymentProvider): BillingController
}

/** Default [CheckoutProvider] supporting both Direct and Google Play distribution. */
@Singleton
class DefaultCheckoutProvider @Inject constructor(
    private val payPal: PayPalBillingController,
    private val payMongo: PayMongoBillingController,
    private val googlePlay: GooglePlayBillingController,
    private val config: SupabaseConfig
) : CheckoutProvider {

    override val isPlayStoreBuild: Boolean
        get() = BuildConfig.PLAY_STORE_BUILD

    override val payPalAvailable: Boolean
        get() = if (BuildConfig.PLAY_STORE_BUILD) false else payPal.isPayPalAvailable

    override val payMongoAvailable: Boolean
        get() = if (BuildConfig.PLAY_STORE_BUILD) false else (
            config.enabled &&
                (config.proMonthlyProductId.isNotBlank() || config.maxMonthlyProductId.isNotBlank())
            )

    override fun controllerFor(provider: PaymentProvider): BillingController = when (provider) {
        PaymentProvider.PAYPAL -> if (BuildConfig.PLAY_STORE_BUILD) googlePlay else payPal
        PaymentProvider.PAYMONGO -> if (BuildConfig.PLAY_STORE_BUILD) googlePlay else payMongo
        PaymentProvider.GOOGLE_PLAY -> googlePlay
    }
}
