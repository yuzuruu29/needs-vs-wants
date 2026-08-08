package com.needsvswants.app.data.billing

import com.needsvswants.app.data.remote.SupabaseConfig
import javax.inject.Inject
import javax.inject.Singleton

/** Payment provider the user picked for checkout on the paywall. */
enum class PaymentProvider {
    /** PayPal auto-charging subscription; Pro carries the 3-day trial on the plan. */
    PAYPAL,
    /** PayMongo one-time hosted checkout (GCash / card), manual monthly renewal. */
    PAYMONGO
}

/**
 * Resolves the concrete [BillingController] for a [PaymentProvider] and reports
 * per-provider availability. Both controllers are already [Singleton] [Inject];
 * the default `BillingController` binding (PayMongo) stays separate for
 * provider-agnostic operations like [BillingController.restorePurchases].
 */
interface CheckoutProvider {
    /** True when PayPal checkout can be started (Supabase enabled + plan ids). */
    val payPalAvailable: Boolean

    /** True when PayMongo checkout can be started (Supabase enabled + plan ids). */
    val payMongoAvailable: Boolean

    /** The controller that executes checkouts for [provider]. */
    fun controllerFor(provider: PaymentProvider): BillingController
}

/** Default [CheckoutProvider] over the two live web-checkout controllers. */
@Singleton
class DefaultCheckoutProvider @Inject constructor(
    private val payPal: PayPalBillingController,
    private val payMongo: PayMongoBillingController,
    private val config: SupabaseConfig
) : CheckoutProvider {

    override val payPalAvailable: Boolean
        get() = payPal.isPayPalAvailable

    override val payMongoAvailable: Boolean
        get() = config.enabled &&
            (config.proMonthlyProductId.isNotBlank() || config.maxMonthlyProductId.isNotBlank())

    override fun controllerFor(provider: PaymentProvider): BillingController = when (provider) {
        PaymentProvider.PAYPAL -> payPal
        PaymentProvider.PAYMONGO -> payMongo
    }
}
