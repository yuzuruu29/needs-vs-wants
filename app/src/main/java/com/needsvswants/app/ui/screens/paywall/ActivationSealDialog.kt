package com.needsvswants.app.ui.screens.paywall

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.AppType
import com.needsvswants.app.ui.theme.MembershipSealBadge
import com.needsvswants.app.ui.theme.Motion
import com.needsvswants.app.ui.theme.PremiumDialog
import com.needsvswants.app.ui.theme.rememberAppHaptics
import kotlinx.coroutines.delay

/** Which tier a successful activation granted. */
enum class ActivationTier { Pro, Max }

/** Copy for the one-shot activation seal (D136). Pure — unit-tested. */
data class ActivationCopy(
    val tier: ActivationTier,
    val eyebrow: String,
    val title: String,
    val body: String,
    val confirmLabel: String = "Continue"
) {
    companion object {
        /**
         * Copy for the current entitlement, or null when no grant is active
         * (Free). Max wins over Pro so an upgrade celebrates the higher tier.
         */
        fun forEntitlement(isPro: Boolean, hasMaxAccess: Boolean): ActivationCopy? = when {
            hasMaxAccess -> ActivationCopy(
                tier = ActivationTier.Max,
                eyebrow = "SUCCESSFULLY ACTIVATED",
                title = "You're on Max",
                body = "Pro is included. AI Financial Advisor with citations is unlocked."
            )
            isPro -> ActivationCopy(
                tier = ActivationTier.Pro,
                eyebrow = "SUCCESSFULLY ACTIVATED",
                title = "You're on Pro",
                body = "Unlimited sheets, lifetime history, and full period analytics are unlocked on this device."
            )
            else -> null
        }

        /** Short quiet strip shown when the dialog is not on screen. */
        fun quietStatusLine(tier: ActivationTier): String = when (tier) {
            ActivationTier.Pro -> "Successfully activated · You're on Pro"
            ActivationTier.Max -> "Successfully activated · You're on Max"
        }
    }
}

/**
 * One-shot Pro/Max activation celebration (D136).
 *
 * Ledger-stamp language, not a party: PremiumDialog chrome, a gold seal that
 * lands with [Motion.sealSpring] (crimson ring for Max), staggered copy, and
 * seal haptics. Dismissing (Continue / Close / back / outside tap) clears the
 * Success result via the caller, so the paywall shows the new card state.
 */
@Composable
fun ActivationSealDialog(
    copy: ActivationCopy,
    onDismiss: () -> Unit
) {
    val haptics = rememberAppHaptics()
    val palette = AppTheme.colors
    var sealVisible by remember { mutableStateOf(false) }

    // Stamp haptics: confirm on show, seal land shortly after (skipped when
    // motion is off — haptics still run once).
    LaunchedEffect(copy.tier) {
        haptics.success()
        if (Motion.enabled) {
            delay(120)
            haptics.seal()
        }
    }
    LaunchedEffect(Unit) { sealVisible = true }

    PremiumDialog(
        onDismissRequest = onDismiss,
        title = copy.title,
        eyebrow = copy.eyebrow,
        eyebrowColor = palette.gold,
        body = null,
        confirmLabel = copy.confirmLabel,
        onConfirm = onDismiss,
        dismissLabel = "Close",
        bodyContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = sealVisible,
                    enter = fadeIn(Motion.stamp()) + scaleIn(
                        initialScale = Motion.StampLandingScale,
                        animationSpec = Motion.sealSpring()
                    )
                ) {
                    MembershipSealBadge(
                        label = if (copy.tier == ActivationTier.Max) "MAX" else "PRO",
                        size = 48.dp,
                        crimsonRing = copy.tier == ActivationTier.Max
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    text = copy.body,
                    style = AppType.bodyMd,
                    color = palette.textSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}