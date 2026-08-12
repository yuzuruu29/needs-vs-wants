package com.needsvswants.app.data.billing

/**
 * Billing cycle the user picked on the paywall.
 *
 * Monthly is the classic 30-day cycle (PayPal monthly plan / PayMongo one-time
 * 30-day grant). Annual is the 12-for-10 price point (PayPal yearly plan /
 * PayMongo one-time 365-day grant).
 */
enum class BillingPeriod {
    MONTHLY,
    ANNUAL
}
