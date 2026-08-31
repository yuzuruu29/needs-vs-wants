package com.needsvswants.app.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.fillMaxSize
import com.needsvswants.app.data.billing.BillingPeriod
import com.needsvswants.app.data.billing.PaymentProvider

/**
 * Membership plan identity for the paywall (website #pro-pricing parity).
 * Free = current device trainer; Pro = unlimited sheets/history; Max = Pro + Advisor.
 */
enum class MembershipPlan {
    Free,
    Pro,
    Max
}

/** Small bordered chip — maps to website `.pri-tag`. */
@Composable
fun TierTag(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.gold
) {
    Text(
        text = text.uppercase(),
        style = AppType.eyebrowSm,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .border(BorderStroke(1.dp, color.copy(alpha = 0.45f)), AppShapes.r20)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

/** Gold circular seal stamp — website Max `.pri-success`. */
@Composable
fun MaxSealBadge(modifier: Modifier = Modifier, label: String = "MAX") =
    MembershipSealBadge(label = label, modifier = modifier)

/**
 * Gold membership seal stamp with optional crimson outer ring (Max tier).
 * Shared by the paywall hero seal and plan-card badges (D136).
 */
@Composable
fun MembershipSealBadge(
    label: String,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    crimsonRing: Boolean = false
) {
    val c = AppTheme.colors
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        if (crimsonRing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(BorderStroke(2.dp, c.crimson.copy(alpha = 0.9f)), CircleShape)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (crimsonRing) 5.dp else 0.dp)
                .background(
                    brush = Brush.radialGradient(
                        listOf(c.goldSoft, c.gold, c.gold.copy(alpha = 0.85f))
                    ),
                    shape = CircleShape
                )
                .border(BorderStroke(1.5.dp, c.goldSoft.copy(alpha = 0.9f)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                // Above the 11sp legibility floor used elsewhere: at Extra large this
                // renders ≈11.2sp inside the fixed 36dp seal.
                style = AppType.eyebrowSm.copy(fontSize = (size.value * 0.264f).sp, fontWeight = FontWeight.Bold),
                color = c.onGold,
                maxLines = 1
            )
        }
    }
}

/** Receipt-style feature row with tier-tinted check mark. */
@Composable
fun ReceiptFeatureLine(
    text: String,
    accent: Color,
    modifier: Modifier = Modifier,
    emphasize: Boolean = false
) {
    val c = AppTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(16.dp)
                .background(accent.copy(alpha = 0.14f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(9.dp)) {
                val path = Path().apply {
                    moveTo(size.width * 0.12f, size.height * 0.52f)
                    lineTo(size.width * 0.38f, size.height * 0.78f)
                    lineTo(size.width * 0.88f, size.height * 0.22f)
                }
                drawPath(
                    path = path,
                    color = accent,
                    style = Stroke(
                        width = 1.8.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = if (emphasize) PaywallType.planFeatureEmph else PaywallType.planFeature,
            color = c.textSecondary,
            modifier = Modifier.weight(1f),
            softWrap = true
        )
    }
}

/**
 * Double-bezel plan card (website `.pri-shell` + `.pri-core`).
 * Outer raised shell, inner white core, top flag strip, optional ribbon/seal.
 */
@Composable
fun PlanTierCard(
    plan: MembershipPlan,
    selected: Boolean,
    onClick: () -> Unit,
    eyebrow: String,
    title: String,
    tag: String,
    price: String,
    priceSuffix: String,
    subtitle: String,
    features: List<Pair<String, Boolean>>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    statusNote: String? = null,
    /**
     * Flagship treatment (D191): the featured card carries a larger price
     * numeral and an always-on heavy border so the hierarchy reads Max, Pro,
     * Free instead of three equal cards.
     */
    featured: Boolean = false,
    /** Quiet treatment: features collapse to one wrapped line (Free tier). */
    compact: Boolean = false
) {
    val c = AppTheme.colors

    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.985f,
        animationSpec = Motion.selectionSpring(),
        label = "planSelectScale"
    )

    val flagColor = when (plan) {
        MembershipPlan.Free -> c.dividerStrong
        MembershipPlan.Pro -> c.gold
        MembershipPlan.Max -> c.crimson
    }
    val accent = when (plan) {
        MembershipPlan.Free -> c.textMuted
        MembershipPlan.Pro -> c.marketGreen
        MembershipPlan.Max -> c.crimson
    }
    val shellBorder = when {
        selected && plan == MembershipPlan.Pro -> c.gold.copy(alpha = 0.75f)
        selected && plan == MembershipPlan.Max -> c.crimson.copy(alpha = 0.55f)
        selected -> c.gold.copy(alpha = 0.45f)
        plan == MembershipPlan.Pro -> c.gold.copy(alpha = 0.40f)
        plan == MembershipPlan.Max -> c.crimson.copy(alpha = 0.28f)
        else -> c.divider
    }
    val shellBrush = when (plan) {
        MembershipPlan.Pro -> Brush.verticalGradient(
            listOf(c.gold.copy(alpha = 0.12f), c.surfaceRaised.copy(alpha = 0.55f))
        )
        MembershipPlan.Max -> Brush.verticalGradient(
            listOf(c.crimson.copy(alpha = 0.08f), c.gold.copy(alpha = 0.06f), c.surfaceRaised.copy(alpha = 0.5f))
        )
        MembershipPlan.Free -> Brush.verticalGradient(
            listOf(c.surfaceRaised, c.surfaceRaised)
        )
    }

    Box(
        modifier = modifier
            .scale(scale)
            .fillMaxWidth()
            .semantics { this.selected = selected }
            .clip(AppShapes.r20)
            .background(shellBrush, AppShapes.r20)
            .border(
                BorderStroke(if (selected || featured) 1.5.dp else 1.dp, shellBorder),
                AppShapes.r20
            )
            .clickable(
                enabled = enabled,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(6.dp)
    ) {
        // Optional Max gold wash behind core
        if (plan == MembershipPlan.Max) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                c.gold.copy(alpha = 0.14f),
                                Color.Transparent
                            )
                        ),
                        AppShapes.r16
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AppShapes.r16)
                .background(c.surfaceCard, AppShapes.r16)
                .border(
                    BorderStroke(1.dp, c.divider.copy(alpha = 0.65f)),
                    AppShapes.r16
                )
        ) {
            // Top flag strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(
                        if (plan == MembershipPlan.Pro) {
                            Brush.horizontalGradient(listOf(c.goldSoft, c.gold, c.goldSoft))
                        } else {
                            Brush.horizontalGradient(listOf(flagColor, flagColor))
                        }
                    )
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                // Meta row: eyebrow + optional Max seal (never share a line with the title)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Eyebrow(
                        text = eyebrow,
                        color = when (plan) {
                            MembershipPlan.Free -> c.textMuted
                            MembershipPlan.Pro -> c.gold
                            MembershipPlan.Max -> c.crimson
                        },
                        size = 11,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (plan == MembershipPlan.Max) {
                        MaxSealBadge(label = "MAX")
                    }
                }

                Spacer(Modifier.height(8.dp))
                // Full-width title — title case, Inter, 2 lines, no wide-track ALL CAPS clip
                Text(
                    text = title,
                    style = PaywallType.planTitle,
                    color = c.textPrimary,
                    maxLines = 2,
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))
                TierTag(
                    text = tag,
                    color = when (plan) {
                        MembershipPlan.Free -> c.textMuted
                        MembershipPlan.Pro -> c.gold
                        MembershipPlan.Max -> c.crimson
                    }
                )

                Spacer(Modifier.height(14.dp))
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = price,
                        style = if (featured) AppType.moneyDisplay else PaywallType.planPrice,
                        color = c.textPrimary,
                        maxLines = 1
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = priceSuffix,
                        style = PaywallType.planPriceSuffix,
                        color = c.textMuted,
                        modifier = Modifier.padding(bottom = 4.dp),
                        maxLines = 2,
                        softWrap = true
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    style = PaywallType.planSub,
                    color = c.textSecondary,
                    maxLines = 3,
                    softWrap = true,
                    overflow = TextOverflow.Clip
                )

                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(c.divider)
                )
                Spacer(Modifier.height(8.dp))

                if (compact) {
                    Text(
                        text = features.joinToString(" · ") { it.first },
                        style = PaywallType.planFeature,
                        color = c.textSecondary,
                        maxLines = 3,
                        softWrap = true
                    )
                } else {
                    features.forEach { (line, emphasize) ->
                        ReceiptFeatureLine(
                            text = line,
                            accent = accent,
                            emphasize = emphasize
                        )
                    }
                }

                if (statusNote != null) {
                    Spacer(Modifier.height(10.dp))
                    // Active-membership pill (D136): soft green chip + hairline so
                    // "You're on Pro/Max" reads as a sealed state, not muted text.
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(c.marketGreen.copy(alpha = 0.10f))
                            .border(
                                BorderStroke(1.dp, c.marketGreen.copy(alpha = 0.35f)),
                                CircleShape
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = statusNote,
                            style = PaywallType.meta,
                            color = c.marketGreen
                        )
                    }
                }
            }
        }
    }
}

/** Compact Need | Want dual seal mark for paywall hero (no Material crown icon). */
@Composable
fun NeedWantSealMark(modifier: Modifier = Modifier) {
    val c = AppTheme.colors
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(c.marketGreen.copy(alpha = 0.14f), CircleShape)
                .border(BorderStroke(1.dp, c.marketGreen.copy(alpha = 0.45f)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "N",
                style = AppType.titleSm,
                color = c.marketGreen
            )
        }
        Box(
            modifier = Modifier
                .width(14.dp)
                .height(1.5.dp)
                .background(c.gold)
        )
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(c.crimson.copy(alpha = 0.12f), CircleShape)
                .border(BorderStroke(1.dp, c.crimson.copy(alpha = 0.45f)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "W",
                style = AppType.titleSm,
                color = c.crimson
            )
        }
    }
}

/**
 * Timeline strip under the selected paid plan, matched to the chosen payment
 * provider: PayPal auto-subscription (trial on Pro, straight billing on Max)
 * or PayMongo one-time manual renewal.
 */
@Composable
fun TrialTimelineCard(
    modifier: Modifier = Modifier,
    forMax: Boolean = false,
    provider: PaymentProvider = PaymentProvider.PAYMONGO,
    period: BillingPeriod = BillingPeriod.MONTHLY
) {
    val c = AppTheme.colors
    val planWord = if (forMax) "Max" else "Pro"
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShapes.r14)
            .background(c.surfaceSunken)
            .border(BorderStroke(1.dp, c.gold.copy(alpha = 0.28f)), AppShapes.r14)
            .padding(14.dp)
    ) {
        when (provider) {
            PaymentProvider.PAYPAL -> if (forMax) {
                Eyebrow(
                    if (period == BillingPeriod.ANNUAL) "BILLED ANNUALLY" else "BILLED MONTHLY",
                    color = c.gilt,
                    size = 11
                )
                Spacer(Modifier.height(8.dp))
                TimelineRow("Today", "Max unlocks after PayPal approval.")
                TimelineRow(
                    if (period == BillingPeriod.ANNUAL) "Yearly" else "Monthly",
                    PaywallCopy.paypalMaxChargeLine(period == BillingPeriod.ANNUAL)
                )
                TimelineRow("Anytime", "Cancel in your PayPal account / subscription settings.")
            } else {
                Eyebrow("TRIAL ON PAYPAL", color = c.gilt, size = 11)
                Spacer(Modifier.height(8.dp))
                TimelineRow("Today", "After PayPal approval, Pro unlocks on this device.")
                TimelineRow(
                    "Day 3",
                    PaywallCopy.paypalProTrialEndLine(period == BillingPeriod.ANNUAL)
                )
                TimelineRow("Anytime", "Cancel in your PayPal account / subscription settings.")
            }
            PaymentProvider.PAYMONGO -> {
                Eyebrow("WHEN YOU NEED IT", color = c.gilt, size = 11)
                Spacer(Modifier.height(8.dp))
                TimelineRow("Today", "After payment, $planWord unlocks on this device.")
                TimelineRow("Expiry", "Access lasts until your expiry date — no auto-charge.")
                TimelineRow("Renew", "Pay again only when you're ready to continue.")
            }
            PaymentProvider.GOOGLE_PLAY -> {
                Eyebrow("GOOGLE PLAY SUBSCRIPTION", color = c.gilt, size = 11)
                Spacer(Modifier.height(8.dp))
                TimelineRow("Today", "$planWord unlocks immediately after Google Play confirmation.")
                TimelineRow(
                    if (period == BillingPeriod.ANNUAL) "Yearly" else "Monthly",
                    "Google Play renews your subscription automatically each period."
                )
                TimelineRow("Anytime", "Manage or cancel anytime in Google Play Store settings.")
            }
        }
    }
}

@Composable
private fun TimelineRow(label: String, detail: String) {
    val c = AppTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = PaywallType.planFeatureEmph,
            color = c.textPrimary,
            modifier = Modifier.width(scaledSpacing(72f))
        )
        Text(
            text = detail,
            style = PaywallType.planSub,
            color = c.textSecondary,
            modifier = Modifier.weight(1f),
            softWrap = true
        )
    }
}

/** Branded notice surface used for account / sign-in strips on the paywall. */
@Composable
fun PaywallNoticeSurface(
    modifier: Modifier = Modifier,
    accent: Color = AppTheme.colors.gold,
    content: @Composable ColumnScope.() -> Unit
) {
    val c = AppTheme.colors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShapes.r16)
            .background(c.surfaceCard)
            .border(BorderStroke(1.dp, accent.copy(alpha = 0.35f)), AppShapes.r16)
            .padding(14.dp),
        content = content
    )
}
