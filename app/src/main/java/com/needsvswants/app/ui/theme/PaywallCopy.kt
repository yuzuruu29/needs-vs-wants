package com.needsvswants.app.ui.theme

/**
 * Single source of truth for paywall price + billing copy (D171).
 *
 * Every surface that names a peso amount or describes a billing mechanic —
 * plan-card tags and price rows, the payment-method selector, the sticky
 * CTA footer, and the trial timeline — renders from here so they can never
 * disagree again (pricing changed once before and the literals had to be
 * chased across two files).
 *
 * Amounts mirror the live PayPal plans / PayMongo rates locked in D146/D147
 * (Pro P49/mo · P490/yr, Max P99/mo · P990/yr). When pricing next changes,
 * this object is the only app file to edit (plus the website mirrors).
 * Pure data so unit tests can pin the matrix.
 */
object PaywallCopy {
    const val PRO_MONTHLY = "₱49"
    const val PRO_ANNUAL = "₱490"
    const val MAX_MONTHLY = "₱99"
    const val MAX_ANNUAL = "₱990"

    /** Free tier price row, so every price on the desk renders from this object. */
    const val FREE_PRICE = "₱0"

    fun proPrice(isAnnual: Boolean): String = if (isAnnual) PRO_ANNUAL else PRO_MONTHLY
    fun maxPrice(isAnnual: Boolean): String = if (isAnnual) MAX_ANNUAL else MAX_MONTHLY

    /** Payment-method selector details — one middot voice for both providers. */
    fun paypalProDetail(isAnnual: Boolean): String =
        "3-day free trial · then ${proPrice(isAnnual)}/${if (isAnnual) "yr" else "mo"} · cancel in PayPal"

    fun paypalMaxDetail(isAnnual: Boolean): String =
        "${maxPrice(isAnnual)}/${if (isAnnual) "yr" else "mo"} · cancel in PayPal"

    fun paymongoDetail(isAnnual: Boolean): String =
        if (isAnnual) "One-time $PRO_ANNUAL · 365 days · no auto-charge"
        else "One-time $PRO_MONTHLY · 30 days · no auto-charge"

    /** Sticky CTA footer lines. The trial claim is unconditional everywhere. */
    fun paypalProFooter(isAnnual: Boolean): String =
        "3-day free trial on PayPal, then ${proPrice(isAnnual)}/${if (isAnnual) "yr" else "mo"}. Cancel anytime in PayPal."

    fun paypalMaxFooter(isAnnual: Boolean): String =
        "${maxPrice(isAnnual)}/${if (isAnnual) "yr" else "mo"} via PayPal. Cancel anytime in PayPal."

    const val PAYMONGO_FOOTER =
        "One-time payment via GCash, card, PayMaya, GrabPay, or QR PH. You pay each period when ready — access ends on your expiry date. No auto-charge."

    /** Trial-timeline charge lines (PayPal branches show exact amounts). */
    fun paypalMaxChargeLine(isAnnual: Boolean): String =
        "PayPal charges ${maxPrice(isAnnual)} each ${if (isAnnual) "year" else "month"} until you cancel."

    fun paypalProTrialEndLine(isAnnual: Boolean): String =
        "Trial ends. PayPal charges ${proPrice(isAnnual)}/${if (isAnnual) "yr" else "mo"} unless you cancel."
}
