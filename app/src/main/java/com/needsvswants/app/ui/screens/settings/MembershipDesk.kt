package com.needsvswants.app.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.ui.screens.auth.AuthUiState
import com.needsvswants.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Plan seal for the Membership Desk — mirrors [MaxSealBadge] language but
 * tints per tier: gold for Pro, crimson+gold for Max.
 */
@Composable
fun PlanSealBadge(entitlement: Entitlement, modifier: Modifier = Modifier) {
    val c = AppTheme.colors
    val isMax = entitlement.hasMaxAccessAt(System.currentTimeMillis())
    val label = if (isMax) "MAX" else "PRO"
    val colors = if (isMax) {
        listOf(c.crimson.copy(alpha = 0.9f), c.gold, c.gold.copy(alpha = 0.85f))
    } else {
        listOf(c.goldSoft, c.gold, c.gold.copy(alpha = 0.85f))
    }
    Box(
        modifier = modifier
            .size(44.dp)
            .background(
                brush = Brush.radialGradient(colors),
                shape = CircleShape
            )
            .border(BorderStroke(1.5.dp, c.goldSoft.copy(alpha = 0.9f)), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = AppType.eyebrowSm.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
            color = Color(0xFF1A1208),
            maxLines = 1
        )
    }
}

/**
 * Membership Desk — the single account dashboard for Pro/Max users.
 *
 * Replaces the old fragmented Account / Subscription / Membership slices. Shows
 * the plan seal, Google identity, access-until honesty, unlocked benefits, and
 * the primary actions (Renew / Upgrade to Max / Refresh). Free users keep the
 * compact "No account" + "View Pro & Max plans" invitation instead.
 */
@Composable
fun MembershipDesk(
    entitlement: Entitlement,
    authState: AuthUiState,
    refreshBusy: Boolean,
    refreshFeedback: String?,
    onRenew: () -> Unit,
    onUpgradeToMax: () -> Unit,
    onRefresh: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = AppTheme.colors
    val now = System.currentTimeMillis()
    val isMax = entitlement.hasMaxAccessAt(now)
    val isPro = entitlement.hasProAccessAt(now)
    val expiry = entitlement.expiresAtEpochMillis
    val planName = if (isMax) "Max" else "Pro"
    val accent = if (isMax) c.crimson else c.gold
    val expiryFmt = expiry?.let {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(it))
    }

    PremiumSurface(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Seal + plan identity
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlanSealBadge(entitlement)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Eyebrow(
                        text = if (isMax) "MAX DIARY" else "PRO DIARY",
                        color = accent,
                        size = 10
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "$planName membership · active",
                        style = AppType.bodyMd,
                        color = c.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (authState.signedIn) {
                    GhostTextAction(
                        text = if (authState.busy) "Signing out…" else "Sign out",
                        onClick = onSignOut,
                        enabled = !authState.busy
                    )
                }
            }

            if (authState.signedIn && authState.email != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    authState.email,
                    style = AppType.bodySm,
                    color = c.textMuted,
                    maxLines = 2
                )
            }

            if (expiryFmt != null && isPro) {
                Spacer(Modifier.height(10.dp))
                Text(
                    "Access until $expiryFmt · no auto-charge",
                    style = AppType.bodySm,
                    color = c.textSecondary
                )
                Text(
                    "Pay again only when you're ready to continue.",
                    style = AppType.caption,
                    color = c.textMuted
                )
            }

            Spacer(Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(c.divider)
            )
            Spacer(Modifier.height(12.dp))

            // Unlocked benefits checklist
            Eyebrow("UNLOCKED", color = accent, size = 10)
            Spacer(Modifier.height(6.dp))
            ReceiptFeatureLine(text = "Unlimited entries per log sheet", accent = accent, emphasize = true)
            ReceiptFeatureLine(text = "Lifetime history retention", accent = accent)
            ReceiptFeatureLine(text = "Month + Lifetime summary periods", accent = accent)
            if (isMax) {
                ReceiptFeatureLine(text = "AI Financial Advisor with citations", accent = accent, emphasize = true)
            }

            Spacer(Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(c.divider)
            )
            Spacer(Modifier.height(12.dp))

            // Primary actions
            GiltButton(
                onClick = onRenew,
                text = "Renew access",
                modifier = Modifier.fillMaxWidth(),
                height = 48.dp
            )
            if (isPro && !isMax) {
                Spacer(Modifier.height(8.dp))
                PremiumSurface(raised = false) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Go Max",
                                style = AppType.bodyMd,
                                color = c.textPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                "Adds the AI Financial Advisor.",
                                style = AppType.caption,
                                color = c.textMuted
                            )
                        }
                        GhostTextAction(text = "Upgrade", onClick = onUpgradeToMax)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    refreshFeedback ?: "Last refreshed on this device",
                    style = AppType.caption,
                    color = c.textSecondary
                )
                GhostTextAction(
                    text = if (refreshBusy) "Refreshing…" else "Refresh",
                    onClick = onRefresh,
                    enabled = !refreshBusy
                )
            }
        }
    }
}