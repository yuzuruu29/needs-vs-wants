package com.needsvswants.app.domain

/**
 * Simple local Free-tier quota configuration (AdMob removed 2026-08-09).
 *
 * Free logging is a plain daily allowance: [FREE_DAILY_LOGS] seals per local
 * calendar day. Unused allowance carries into the next *consecutive* day when
 * the prior day logged at least once; a missed day resets the carry. There is
 * no rewarded-ad bonus path — monetization is withdraw from the Free tier.
 */
object FreeQuotaConfig {
    /** Free seals per local calendar day. */
    const val FREE_DAILY_LOGS = 5
}