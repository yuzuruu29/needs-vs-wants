package com.needsvswants.app.ui.theme

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.toMoney

/** Soft vertical wash used on every screen — light top → slightly warmer raised base. */
fun inkWash(top: Color, bottom: Color) = Brush.verticalGradient(
    colors = listOf(top, bottom)
)

/** Themed background wash — soft cream depth (supermarket premium, not purple-AI). */
@Composable
fun themedInkWash(): Brush {
    val c = AppTheme.colors
    return Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to c.background,
            0.55f to c.background,
            1.0f to c.surfaceSunken
        )
    )
}

/**
 * Glossy card surface: white/raised fill, soft elevation, gold-kissed border.
 * Prefer this over bare [Surface] on main screens for a premium feel.
 */
@Composable
fun PremiumSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    goldEdge: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val c = AppTheme.colors
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = c.surfaceCard,
        shadowElevation = 5.dp,
        tonalElevation = 0.dp,
        border = BorderStroke(
            1.dp,
            if (goldEdge) c.gold.copy(alpha = 0.28f) else c.divider
        )
    ) {
        // Subtle top sheen
        Box {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = if (c.isLightStatusBars) 0.55f else 0.06f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Column(content = content)
        }
    }
}

/** Faint gold radial glow used behind hero elements (donut, stat hero). */
fun giltGlow(gold: Color, alpha: Float = 0.16f) = Brush.radialGradient(
    colors = listOf(gold.copy(alpha = alpha), Color.Transparent)
)

/** Eyebrow label — wide-spaced uppercase micro heading. */
@Composable
fun Eyebrow(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    size: Int = 11,
    align: TextAlign? = null,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE
) {
    val resolved = color ?: AppTheme.colors.crimson
    val baseStyle = MaterialTheme.typography.labelSmall.copy(
        fontSize = size.sp,
        letterSpacing = (size * 0.12f).sp
    )
    Text(
        text = text.uppercase(),
        style = if (align != null) baseStyle.copy(textAlign = align) else baseStyle,
        color = resolved,
        modifier = modifier,
        softWrap = softWrap,
        maxLines = maxLines,
        overflow = if (maxLines == 1) TextOverflow.Ellipsis else TextOverflow.Clip
    )
}

/** The short AppTheme.colors.gold rule used under section titles. */
@Composable
fun GiltRule(modifier: Modifier = Modifier, width: Dp = 32.dp, height: Dp = 1.5.dp) {
    Box(modifier = modifier
        .width(width)
        .height(height)
        .background(AppTheme.colors.gold)
    )
}

/** Backwards-compatible name originally used by screens. */
@Composable
fun GoldUnderline() = GiltRule(width = 28.dp)

/** Spent / budget meter with Remaining or Over-by line — Summary Day and Log when budget is on. */
@Composable
fun DailyBudgetMeter(
    status: BudgetStatus.On,
    symbol: String,
    modifier: Modifier = Modifier
) {
    val over = status.remainingCents < 0
    val animatedProgress by animateFloatAsState(
        targetValue = status.progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "budgetProgress"
    )
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.colors.surfaceCard,
        border = BorderStroke(1.dp, if (over) AppTheme.colors.crimson.copy(alpha = 0.45f) else AppTheme.colors.divider),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Eyebrow("DAILY BUDGET", color = if (over) AppTheme.colors.crimson else AppTheme.colors.gilt)
            Spacer(Modifier.height(8.dp))
            Text(
                "${status.spentCents.toMoney(symbol)} / ${status.budgetCents.toMoney(symbol)}",
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.colors.textPrimary
            )
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = if (over) AppTheme.colors.crimson else AppTheme.colors.marketGreen,
                trackColor = AppTheme.colors.surfaceRaised,
                strokeCap = StrokeCap.Round
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (over) "Over by ${(-status.remainingCents).toMoney(symbol)}"
                else "Remaining ${status.remainingCents.toMoney(symbol)}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (over) AppTheme.colors.crimson else AppTheme.colors.textSecondary
            )
        }
    }
}

/**
 * Shrink a money string's font so it always fits its container instead of
 * clipping or overflowing. `base` is the normal size for short amounts.
 */
fun adaptiveMoneySize(text: String, base: TextUnit): TextUnit {
    val len = text.length
    val factor = when {
        len <= 9 -> 1f
        len <= 12 -> 0.82f
        len <= 15 -> 0.68f
        len <= 19 -> 0.58f
        else -> 0.5f
    }
    return (base.value * factor).sp
}

/** Premium card — glossy surface with gold edge. */
@Composable
fun GiltCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    giltAccent: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    PremiumSurface(modifier = modifier, shape = shape, goldEdge = true || giltAccent, content = content)
}

/** Primary action button — solid crimson with white text, premium weight. */
@Composable
fun GiltButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    height: Dp = 54.dp
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.crimson,
            contentColor = AppTheme.colors.surfaceCard,
            disabledContainerColor = AppTheme.colors.crimson.copy(alpha = 0.35f),
            disabledContentColor = AppTheme.colors.surfaceCard.copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp, pressedElevation = 1.dp),
        modifier = modifier.heightIn(min = height)
    ) {
        Text(
            text = text.uppercase(),
            style = TextStyle(
                fontFamily = BodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                letterSpacing = 1.0.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Shared ledger column metrics — widths scale with fontScale so enlarged text
 * does not clip into neighboring columns.
 */
@Composable
private fun ledgerScaled(base: Float): Dp {
    val fs = androidx.compose.ui.platform.LocalDensity.current.fontScale.coerceIn(1f, 1.35f)
    return (base * fs).dp
}

/** Column labels for the sealed-entry table on the Log screen. */
@Composable
fun EntryLedgerHeader(modifier: Modifier = Modifier) {
    val timeW = ledgerScaled(48f)
    val costW = ledgerScaled(92f)
    val typeW = ledgerScaled(46f)
    val deleteW = ledgerScaled(32f)
    val gutter = ledgerScaled(8f)
    val gutterTight = ledgerScaled(6f)
    val trail = ledgerScaled(6f)
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "TIME",
            style = ledgerHeaderStyle(),
            color = AppTheme.colors.textMuted,
            modifier = Modifier.width(timeW),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
        Spacer(Modifier.width(gutter))
        Text(
            text = "ITEM",
            style = ledgerHeaderStyle(),
            color = AppTheme.colors.textMuted,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.width(gutter))
        Text(
            text = "COST",
            style = ledgerHeaderStyle(),
            color = AppTheme.colors.textMuted,
            modifier = Modifier.width(costW),
            textAlign = TextAlign.End,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
        Spacer(Modifier.width(trail))
        Text(
            text = "TYPE",
            style = ledgerHeaderStyle(letterSpacing = 0.4.sp),
            color = AppTheme.colors.textMuted,
            modifier = Modifier.width(typeW),
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
        Spacer(Modifier.width(gutterTight))
        Spacer(Modifier.width(deleteW))
    }
}

/**
 * Single expense line used on both Log and History.
 * Trailing cluster (cost · type · delete) stays tight; item takes flexible space.
 */
@Composable
fun EntryLedgerRow(
    entry: Entry,
    symbol: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    showCard: Boolean = false
) {
    val typeColor = if (entry.type == EntryType.NEED) AppTheme.colors.need else AppTheme.colors.want
    val money = entry.costCents.toMoney(symbol)
    val timeW = ledgerScaled(48f)
    val costW = ledgerScaled(92f)
    val typeW = ledgerScaled(46f)
    val deleteW = ledgerScaled(32f)
    val gutter = ledgerScaled(8f)
    val gutterTight = ledgerScaled(6f)
    val trail = ledgerScaled(6f)
    val content: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (showCard) Modifier.padding(horizontal = 14.dp, vertical = 13.dp)
                    else Modifier.padding(vertical = 11.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.time,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    letterSpacing = 0.2.sp,
                    fontFeatureSettings = "tnum"
                ),
                color = AppTheme.colors.textMuted,
                modifier = Modifier.width(timeW),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip
            )
            Spacer(Modifier.width(gutter))
            Text(
                text = entry.item,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(gutter))
            Text(
                text = money,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFeatureSettings = "tnum",
                    fontSize = adaptiveMoneySize(money, 13.sp)
                ),
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.width(costW),
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(trail))
            Box(
                modifier = Modifier
                    .width(typeW)
                    .heightIn(min = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(ledgerScaled(30f))
                        .border(BorderStroke(1.2.dp, typeColor), RoundedCornerShape(7.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (entry.type == EntryType.NEED) "N" else "W",
                        color = typeColor,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.width(gutterTight))
            Box(
                modifier = Modifier
                    .size(deleteW)
                    .clickable(role = Role.Button, onClick = onDelete),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = AppTheme.colors.danger.copy(alpha = 0.55f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }

    if (showCard) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = AppTheme.colors.inkElevated,
            border = BorderStroke(1.dp, AppTheme.colors.inkDivider)
        ) {
            content()
        }
    } else {
        Box(modifier = modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
private fun ledgerHeaderStyle(letterSpacing: TextUnit = 1.1.sp): TextStyle =
    MaterialTheme.typography.labelSmall.copy(
        fontSize = 10.sp,
        letterSpacing = letterSpacing,
        fontWeight = FontWeight.SemiBold
    )
