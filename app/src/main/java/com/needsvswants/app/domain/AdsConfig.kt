package com.needsvswants.app.domain

/**
 * Free-tier rewarded-ad configuration.
 *
 * [ENABLED] is the master kill switch: when false, the AdMob gateway is not
 * bound and the Watch-ad affordance is hidden everywhere. [FREE_DAILY_LOGS]
 * is the base allowance per local calendar day; the carry-forward rule lives
 * in [DailyLogQuota]. Each completed rewarded ad grants
 * [EXTRA_LOGS_PER_REWARD] bonus seals, capped at [MAX_REWARDED_ADS_PER_DAY]
 * completions per day.
 *
 * The AdMob app id and rewarded unit id come from BuildConfig
 * (local.properties override); the values below are Google TEST ids.
 */
object AdsConfig {
    const val ENABLED = true
    const val FREE_DAILY_LOGS = 5
    const val EXTRA_LOGS_PER_REWARD = 8
    const val MAX_REWARDED_ADS_PER_DAY = 3
}
