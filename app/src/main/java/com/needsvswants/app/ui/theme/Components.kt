package com.needsvswants.app.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.Insight
import com.needsvswants.app.domain.InsightAccent
import com.needsvswants.app.domain.StreakMilestone
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
 *
 * @param raised true = elevated gold-edge card; false = flat inset paper panel.
 */
@Composable
fun PremiumSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    goldEdge: Boolean = true,
    raised: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val c = AppTheme.colors
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = if (raised) c.surfaceCard else c.surfaceSunken,
        shadowElevation = if (raised) 5.dp else 0.dp,
        tonalElevation = 0.dp,
        border = BorderStroke(
            1.dp,
            if (goldEdge) c.gold.copy(alpha = if (raised) 0.28f else 0.18f) else c.divider
        )
    ) {
        Box {
            if (raised) {
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
            }
            Column(content = content)
        }
    }
}

/** Faint gold radial glow used behind hero elements (donut, stat hero). */
fun giltGlow(gold: Color, alpha: Float = 0.16f) = Brush.radialGradient(
    colors = listOf(gold.copy(alpha = alpha), Color.Transparent)
)

/** Eyebrow label — Inter tracked micro heading (Option A). */
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
    val baseStyle = if (size <= 10) AppType.eyebrowSm else AppType.eyebrow
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
        animationSpec = Motion.budget(),
        label = "budgetProgress"
    )
    val edge = if (over) AppTheme.colors.crimson.copy(alpha = 0.45f) else AppTheme.colors.gold.copy(alpha = 0.28f)
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.colors.surfaceCard,
        border = BorderStroke(1.dp, edge),
        shadowElevation = 2.dp,
        tonalElevation = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Eyebrow("DAILY BUDGET", color = if (over) AppTheme.colors.crimson else AppTheme.colors.gilt)
            Spacer(Modifier.height(8.dp))
            Text(
                "${status.spentCents.toMoney(symbol)} / ${status.budgetCents.toMoney(symbol)}",
                style = AppType.moneyMd,
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
                style = AppType.bodyMd,
                color = if (over) AppTheme.colors.crimson else AppTheme.colors.textSecondary
            )
        }
    }
}

/**
 * Quiet secondary text action (Change / Turn off / Skip / Restore).
 * Avoids stock Material TextButton chrome while keeping a full 48dp hit target.
 */
@Composable
fun GhostTextAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    danger: Boolean = false
) {
    val c = AppTheme.colors
    val color = when {
        !enabled -> c.textMuted.copy(alpha = 0.45f)
        danger -> c.danger
        else -> c.textSecondary
    }
    Box(
        modifier = modifier
            .heightIn(min = 44.dp)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AppType.meta,
            color = color
        )
    }
}

/** Preference / list panel shell — gold hairline card used on Settings + Summary. */
@Composable
fun SettingsPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    PremiumSurface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        goldEdge = true,
        raised = true,
        content = content
    )
}

/**
 * 48dp gold-edge icon well for screen headers (Help, Share, Export, Send).
 * Prefer over stock IconButton chrome.
 */
@Composable
fun HeaderIconWell(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    filled: Boolean = false,
    fillColor: Color? = null,
    content: @Composable () -> Unit
) {
    val c = AppTheme.colors
    val bg = when {
        filled -> (fillColor ?: c.crimson)
        else -> c.surfaceCard
    }
    val border = if (filled) {
        BorderStroke(1.dp, (fillColor ?: c.crimson).copy(alpha = 0.85f))
    } else {
        BorderStroke(1.dp, c.gold.copy(alpha = 0.32f))
    }
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (enabled) bg else bg.copy(alpha = 0.45f))
            .border(border, RoundedCornerShape(14.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.size(22.dp), contentAlignment = Alignment.Center) {
            content()
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

/** Primary action button — solid crimson with white text, press scale physics. */
@Composable
fun GiltButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    height: Dp = 54.dp
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = Motion.pressSpring(),
        label = "giltPress"
    )
    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.crimson,
            contentColor = AppTheme.colors.surfaceCard,
            disabledContainerColor = AppTheme.colors.crimson.copy(alpha = 0.35f),
            disabledContentColor = AppTheme.colors.surfaceCard.copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp,
            pressedElevation = 1.dp
        ),
        modifier = modifier
            .scale(scale)
            .heightIn(min = height)
    ) {
        Text(
            text = text.uppercase(),
            style = AppType.button,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Paper-ledger text field: flat well, bottom rule that turns gold on focus,
 * crimson error. Replaces stock OutlinedTextField on Log / budget / advisor.
 */
@Composable
fun LedgerField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    supportingText: String? = null,
    textStyle: TextStyle = AppType.input
) {
    val c = AppTheme.colors
    var focused by remember { mutableStateOf(false) }
    val ruleColor = when {
        isError -> c.crimson
        focused -> c.gold
        else -> c.dividerStrong
    }
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = AppType.eyebrowSm,
            color = if (isError) c.crimson else if (focused) c.crimson else c.textMuted
        )
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            textStyle = textStyle.copy(color = c.textPrimary),
            cursorBrush = SolidColor(c.crimson),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focused = it.isFocused }
                .background(c.surfaceCard, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .border(
                    BorderStroke(1.dp, if (focused) c.gold.copy(alpha = 0.45f) else c.divider),
                    RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 14.dp),
            decorationBox = { inner ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = label.lowercase().replaceFirstChar { it.titlecase() },
                            style = textStyle,
                            color = c.textMuted.copy(alpha = 0.72f)
                        )
                    }
                    inner()
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(ruleColor)
        )
        if (supportingText != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = supportingText,
                style = AppType.caption,
                color = if (isError) c.danger else c.textMuted
            )
        }
    }
}

/**
 * Branded dialog: Inter dialog title (Option A), gold hairline, GiltButton actions.
 * Replaces stock AlertDialog chrome across Log / History / Settings / Instructions.
 */
@Composable
fun PremiumDialog(
    onDismissRequest: () -> Unit,
    title: String,
    eyebrow: String? = null,
    eyebrowColor: Color? = null,
    body: String? = null,
    confirmLabel: String,
    onConfirm: () -> Unit,
    dismissLabel: String = "Cancel",
    confirmDanger: Boolean = false,
    bodyContent: (@Composable () -> Unit)? = null
) {
    val c = AppTheme.colors
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = c.surfaceCard,
            border = BorderStroke(1.dp, c.gold.copy(alpha = 0.28f)),
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                if (eyebrow != null) {
                    Eyebrow(eyebrow, color = eyebrowColor ?: c.crimson)
                    Spacer(Modifier.height(6.dp))
                }
                Text(
                    text = title,
                    style = AppType.dialogTitle,
                    color = c.textPrimary
                )
                Spacer(Modifier.height(8.dp))
                GiltRule(width = 32.dp)
                Spacer(Modifier.height(14.dp))
                if (bodyContent != null) {
                    bodyContent()
                } else if (body != null) {
                    Text(body, style = AppType.body, color = c.textSecondary)
                }
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismissRequest, modifier = Modifier.weight(1f)) {
                        Text(dismissLabel, color = c.textMuted)
                    }
                    if (confirmDanger) {
                        Button(
                            onClick = onConfirm,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = c.danger.copy(alpha = 0.18f),
                                contentColor = c.danger
                            ),
                            border = BorderStroke(1.dp, c.danger),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(confirmLabel, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        GiltButton(
                            onClick = onConfirm,
                            text = confirmLabel,
                            height = 46.dp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

/** Gold "SEALED" stamp overlay for sheet-complete / day celebration. */
@Composable
fun SealStampOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier,
    label: String = "SEALED"
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(Motion.stamp()) + scaleIn(
            initialScale = Motion.StampLandingScale,
            animationSpec = Motion.sealSpring()
        ),
        exit = fadeOut(Motion.feedback()) + scaleOut(
            targetScale = Motion.StampLeavingScale,
            animationSpec = Motion.feedback()
        )
    ) {
        Box(
            modifier = modifier
                .semantics {
                    contentDescription = label
                    liveRegion = LiveRegionMode.Polite
                }
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = AppType.stamp,
                color = AppTheme.colors.gold.copy(alpha = 0.92f)
            )
        }
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

/**
 * Shared ledger column widths. Item column owns flexible space; trailing cluster is tight
 * so long product names stay readable (D8 + Log visibility fix).
 */
@Composable
private fun ledgerColumnMetrics(): LedgerColumnMetrics {
    return LedgerColumnMetrics(
        timeW = ledgerScaled(42f),
        costW = ledgerScaled(78f),
        typeW = ledgerScaled(36f),
        deleteW = ledgerScaled(40f),
        gutter = ledgerScaled(6f),
        gutterTight = ledgerScaled(4f),
        trail = ledgerScaled(4f)
    )
}

private data class LedgerColumnMetrics(
    val timeW: Dp,
    val costW: Dp,
    val typeW: Dp,
    val deleteW: Dp,
    val gutter: Dp,
    val gutterTight: Dp,
    val trail: Dp
)

/** Column labels for the sealed-entry table on the Log screen. */
@Composable
fun EntryLedgerHeader(modifier: Modifier = Modifier) {
    val m = ledgerColumnMetrics()
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "TIME",
            style = ledgerHeaderStyle(),
            color = AppTheme.colors.textMuted,
            modifier = Modifier.width(m.timeW),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
        Spacer(Modifier.width(m.gutter))
        Text(
            text = "ITEM",
            style = ledgerHeaderStyle(),
            color = AppTheme.colors.textMuted,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.width(m.gutter))
        Text(
            text = "COST",
            style = ledgerHeaderStyle(),
            color = AppTheme.colors.textMuted,
            modifier = Modifier.width(m.costW),
            textAlign = TextAlign.End,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
        Spacer(Modifier.width(m.trail))
        Text(
            text = "TYPE",
            style = ledgerHeaderStyle(letterSpacing = 0.4.sp),
            color = AppTheme.colors.textMuted,
            modifier = Modifier.width(m.typeW),
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
        Spacer(Modifier.width(m.gutterTight))
        Spacer(Modifier.width(m.deleteW))
    }
}

/**
 * Single expense line used on both Log and History.
 * Item is the primary readable field (up to 2 lines); trailing cluster stays compact.
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
    val m = ledgerColumnMetrics()
    val content: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (showCard) Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                    else Modifier.padding(vertical = 10.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.time,
                style = AppType.ledgerMeta,
                color = AppTheme.colors.textMuted,
                modifier = Modifier.width(m.timeW),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip
            )
            Spacer(Modifier.width(m.gutter))
            Text(
                text = entry.item,
                style = AppType.ledgerItem,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .widthIn(min = 72.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
            Spacer(Modifier.width(m.gutter))
            Text(
                text = money,
                style = AppType.moneySm.copy(
                    fontSize = adaptiveMoneySize(money, 13.sp)
                ),
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.width(m.costW),
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(m.trail))
            Box(
                modifier = Modifier
                    .width(m.typeW)
                    .heightIn(min = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(ledgerScaled(28f))
                        .border(BorderStroke(1.2.dp, typeColor), RoundedCornerShape(7.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (entry.type == EntryType.NEED) "N" else "W",
                        color = typeColor,
                        style = AppType.titleSm.copy(letterSpacing = 0.sp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.width(m.gutterTight))
            Box(
                modifier = Modifier
                    .size(m.deleteW)
                    .clickable(role = Role.Button, onClick = onDelete),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = AppTheme.colors.danger.copy(alpha = 0.55f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    if (showCard) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = AppTheme.colors.surfaceCard,
            border = BorderStroke(1.dp, AppTheme.colors.gold.copy(alpha = 0.22f)),
            shadowElevation = 1.dp,
            tonalElevation = 0.dp
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
private fun ledgerHeaderStyle(letterSpacing: TextUnit = 1.0.sp): TextStyle =
    AppType.ledgerHeader.copy(letterSpacing = letterSpacing)

/**
 * Quiet ledger footnote for consecutive logging days.
 * Not a habit-app badge card — hairline rule + two short lines under stats.
 * Hidden when streak is 0 so the donut keeps the hero role.
 */
@Composable
fun StreakLine(
    currentStreak: Int,
    bestStreak: Int,
    modifier: Modifier = Modifier
) {
    if (currentStreak <= 0) return
    val c = AppTheme.colors
    val next = StreakMilestone.nextAfter(currentStreak)
    // One secondary phrase only — never middot-chain day + progress + best.
    val secondary = when {
        next != null -> {
            val left = next.days - currentStreak
            val unit = if (left == 1) "day" else "days"
            "$left $unit to ${next.label}"
        }
        bestStreak > currentStreak -> "Best $bestStreak"
        else -> "Full cycle"
    }

    Column(modifier = modifier.fillMaxWidth()) {
        GiltRule(width = 28.dp)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (currentStreak == 1) "Day 1 logged" else "Day $currentStreak logged",
                style = AppType.titleSm,
                color = c.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = secondary,
                style = AppType.meta,
                color = c.textMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Quiet milestone mark — uses shared PremiumDialog chrome. */
@Composable
fun MilestoneMarkDialog(
    milestone: StreakMilestone?,
    onDismiss: () -> Unit
) {
    if (milestone == null) return
    val haptics = rememberAppHaptics()

    androidx.compose.runtime.LaunchedEffect(milestone.days) {
        haptics.tick()
    }

    PremiumDialog(
        onDismissRequest = onDismiss,
        title = milestone.label,
        eyebrow = "STREAK MARK",
        eyebrowColor = AppTheme.colors.gold,
        body = "Day ${milestone.days} of consecutive logging.",
        confirmLabel = "Continue",
        onConfirm = onDismiss,
        dismissLabel = "Close"
    )
}

/** Single high-signal insight strip for Summary (not a carousel). */
@Composable
fun InsightStrip(
    insight: Insight?,
    modifier: Modifier = Modifier
) {
    if (insight == null) return
    val palette = AppTheme.colors
    val accentColor = when (insight.accent) {
        InsightAccent.POSITIVE -> palette.need
        InsightAccent.NEUTRAL -> palette.gold
        InsightAccent.ALERT -> palette.want
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.surfaceCard, RoundedCornerShape(16.dp))
            .border(BorderStroke(1.dp, palette.gold.copy(alpha = 0.28f)), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(accentColor, RoundedCornerShape(4.dp))
            )
            Spacer(Modifier.width(8.dp))
            Eyebrow(insight.title.uppercase(), color = accentColor, size = 10)
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = insight.body,
            style = AppType.bodyMd,
            color = palette.textPrimary
        )
    }
}

