package com.needsvswants.app.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
    val c = AppTheme.colors
    Text(
        text = text.uppercase(),
        style = AppType.eyebrowSm,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .border(BorderStroke(1.dp, color.copy(alpha = 0.45f)), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

/** Gold "Most popular" ribbon — website `.pri-popular`. */
@Composable
fun PopularRibbon(modifier: Modifier = Modifier) {
    val c = AppTheme.colors
    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    listOf(c.goldSoft, c.gold, c.goldSoft.copy(alpha = 0.92f))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = "MOST POPULAR",
            style = AppType.eyebrowSm.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF1A1208),
            maxLines = 1
        )
    }
}

/** Gold circular seal stamp — website Max `.pri-success`. */
@Composable
fun MaxSealBadge(modifier: Modifier = Modifier, label: String = "MAX") {
    val c = AppTheme.colors
    Box(
        modifier = modifier
            .size(36.dp)
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
            style = AppType.eyebrowSm.copy(fontSize = 8.sp, fontWeight = FontWeight.Bold),
            color = Color(0xFF1A1208),
            maxLines = 1
        )
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
    statusNote: String? = null
) {
    val c = AppTheme.colors
    val interaction = remember { MutableInteractionSource() }
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
    val outerRadius = 22.dp
    val innerRadius = 16.dp

    Box(
        modifier = modifier
            .scale(scale)
            .fillMaxWidth()
            .semantics { this.selected = selected }
            .clip(RoundedCornerShape(outerRadius))
            .background(shellBrush, RoundedCornerShape(outerRadius))
            .border(
                BorderStroke(if (selected) 1.5.dp else 1.dp, shellBorder),
                RoundedCornerShape(outerRadius)
            )
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = null,
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
                        RoundedCornerShape(innerRadius)
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(innerRadius))
                .background(c.surfaceCard, RoundedCornerShape(innerRadius))
                .border(
                    BorderStroke(1.dp, c.divider.copy(alpha = 0.65f)),
                    RoundedCornerShape(innerRadius)
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
                        size = 10,
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

                if (plan == MembershipPlan.Pro) {
                    Spacer(Modifier.height(10.dp))
                    PopularRibbon()
                }

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
                        style = PaywallType.planPrice,
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

                features.forEach { (line, emphasize) ->
                    ReceiptFeatureLine(
                        text = line,
                        accent = accent,
                        emphasize = emphasize
                    )
                }

                if (statusNote != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = statusNote,
                        style = PaywallType.meta,
                        color = c.textMuted
                    )
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

/** Trial timeline strip under selected paid plan. */
@Composable
fun TrialTimelineCard(
    modifier: Modifier = Modifier,
    forMax: Boolean = false
) {
    val c = AppTheme.colors
    val planWord = if (forMax) "Max" else "Pro"
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(c.surfaceSunken)
            .border(BorderStroke(1.dp, c.gold.copy(alpha = 0.28f)), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Eyebrow("3-DAY FREE TRIAL", color = c.gilt, size = 10)
        Spacer(Modifier.height(8.dp))
        TimelineRow("Today", "Full $planWord features unlock on this device.")
        TimelineRow("Day 3", "Trial ends. Play bills the monthly rate unless you cancel.")
        TimelineRow("Anytime", "Cancel in Google Play subscriptions.")
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
            modifier = Modifier.width(64.dp)
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
            .clip(RoundedCornerShape(16.dp))
            .background(c.surfaceCard)
            .border(BorderStroke(1.dp, accent.copy(alpha = 0.35f)), RoundedCornerShape(16.dp))
            .padding(14.dp),
        content = content
    )
}
