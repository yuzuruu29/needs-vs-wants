package com.needsvswants.app.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlinx.coroutines.delay

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
 * Ledger paper card: near-opaque stock, optional rules (raised), gold or ink
 * hairline, soft desk shadow. Prefer over bare [Surface] on main screens.
 *
 * @param raised true = elevated ruled sheet; false = flat inset panel.
 */
@Composable
fun PremiumSurface(
    modifier: Modifier = Modifier,
    shape: Shape = AppShapes.r20,
    goldEdge: Boolean = true,
    raised: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val spec = rememberPaperSpec(if (raised) PaperKind.RAISED else PaperKind.FLAT, goldEdge = goldEdge)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .paperSurface(spec, shape)
    ) {
        Column(content = content)
    }
}

/** Eyebrow label — Inter tracked micro heading (Option A). */
@Composable
fun Eyebrow(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    size: Int = 12,
    align: TextAlign? = null,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE
) {
    val resolved = color ?: AppTheme.colors.crimson
    // Floor at 11sp so call sites with size=9/10 never go illegible.
    val floored = size.coerceAtLeast(11)
    val baseStyle = if (floored <= 11) {
        AppType.eyebrowSm
    } else {
        AppType.eyebrow.copy(
            fontSize = floored.sp,
            lineHeight = (floored * 1.35f).sp
        )
    }
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
    // One-shot pulse on the over/remaining line when the budget flips over.
    val pulse = remember { Animatable(1f) }
    LaunchedEffect(over) {
        if (over && Motion.enabled) {
            pulse.animateTo(1.05f, Motion.budget())
            pulse.animateTo(1f, Motion.feedback())
        }
    }
    val guardShape = AppShapes.r16
    Box(
        modifier = modifier
            .fillMaxWidth()
            .paperSurface(
                rememberPaperSpec(PaperKind.CHIP, goldEdge = !over),
                guardShape
            )
            // over-state edge: crimson when over budget (paper chip already has gold rim)
            .border(BorderStroke(1.dp, if (over) edge else Color.Transparent), guardShape)
    ) {
        Column(modifier = Modifier.padding(scaledSpacing(16f))) {
            Eyebrow("DAILY BUDGET", color = if (over) AppTheme.colors.crimson else AppTheme.colors.gilt)
            Spacer(Modifier.height(scaledSpacing(8f)))
            Text(
                "${status.spentCents.toMoney(symbol)} / ${status.budgetCents.toMoney(symbol)}",
                style = AppType.moneyMd,
                color = AppTheme.colors.textPrimary
            )
            Spacer(Modifier.height(scaledSpacing(8f)))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = if (over) AppTheme.colors.crimson else AppTheme.colors.marketGreen,
                trackColor = AppTheme.colors.surfaceRaised,
                strokeCap = StrokeCap.Round
            )
            Spacer(Modifier.height(scaledSpacing(8f)))
            Text(
                if (over) "Over by ${(-status.remainingCents).toMoney(symbol)}"
                else "Remaining ${status.remainingCents.toMoney(symbol)}",
                style = AppType.bodyMd,
                color = if (over) AppTheme.colors.crimson else AppTheme.colors.textSecondary,
                modifier = Modifier.graphicsLayer {
                    scaleX = pulse.value
                    scaleY = pulse.value
                }
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
        shape = AppShapes.r16,
        goldEdge = true,
        raised = true,
        content = content
    )
}

/**
 * Machined Double-Bezel (Doppelrand) container:
 * Outer shell with subtle gold hairline, inner concentric core with top highlight.
 */
@Composable
fun DoubleBezelCard(
    modifier: Modifier = Modifier,
    outerShape: Shape = AppShapes.r20,
    innerShape: Shape = AppShapes.r14,
    goldEdge: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val palette = AppTheme.colors
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(outerShape)
            .background(palette.surfaceSunken.copy(alpha = 0.5f))
            .border(
                BorderStroke(1.dp, if (goldEdge) palette.gold.copy(alpha = 0.35f) else palette.divider),
                outerShape
            )
            .padding(2.5.dp)
    ) {
        Surface(
            shape = innerShape,
            color = palette.surfaceCard,
            shadowElevation = 2.dp,
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(content = content)
        }
    }
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
    val interaction = remember { MutableInteractionSource() }
    val bg = when {
        filled -> (fillColor ?: c.crimson)
        else -> c.surfaceCard
    }
    val border = if (filled) {
        BorderStroke(1.dp, (fillColor ?: c.crimson).copy(alpha = 0.85f))
    } else {
        BorderStroke(1.dp, c.gold.copy(alpha = 0.32f))
    }
    val well = scaledSpacing(48f)
    Box(
        modifier = modifier
            .size(well)
            .pressRecoil(interaction, enabled)
            .then(
                if (filled) {
                    Modifier
                        .background(if (enabled) bg else bg.copy(alpha = 0.45f), AppShapes.r14)
                        .border(border, AppShapes.r14)
                } else {
                    // Solid paper chip — gold hairline, no translucent chrome
                    Modifier.paperSurface(
                        rememberPaperSpec(PaperKind.CHIP, goldEdge = true),
                        AppShapes.r14
                    )
                }
            )
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
                role = Role.Button
            )
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
 *
 * Calibrated to measured Source Sans 3 Regular advances (2026-08-07):
 * "₱ 1,234.56" = 4.177em, "₱ 12,345.67" = 4.674em, "₱ 1,234,567.89" = 5.917em,
 * "₱ 12,345,678.90" = 6.414em. The ledger cost column is 92dp since the
 * delete affordance moved to long-press (D97), so amounts up to 13 glyphs
 * (≈ ₱ 1,234,567.89) keep the FULL base size at every scale — 5.917em × 15sp
 * = 88.8dp ≤ 92dp. Only longer amounts step down; floor at 11sp so money
 * never shrinks below the eyebrow scale at Extra large.
 */
fun adaptiveMoneySize(text: String, base: TextUnit): TextUnit {
    val len = text.length
    val factor = when {
        len <= 7 -> 1f
        len <= 9 -> 1f
        len <= 13 -> 1f
        len <= 15 -> 0.90f
        len <= 18 -> 0.78f
        len <= 22 -> 0.66f
        else -> 0.58f
    }
    return (base.value * factor).coerceAtLeast(11f).sp
}

/**
 * Width-aware money size for tight cards. Uses [maxWidth] so long amounts
 * scale down before they paint outside the gold-edge box.
 *
 * The 0.58em/glyph estimate deliberately over-estimates real Source Sans 3
 * advances (measured ≈ 0.45em for money strings), so the result is always
 * safe — and with the 11sp floor, money never falls below the eyebrow scale
 * even at Extra large. The length-based [adaptiveMoneySize] curve is only
 * used where no container width is known (ledger columns).
 */
@Composable
fun fittingMoneySize(text: String, base: TextUnit, maxWidth: Dp): TextUnit {
    val density = LocalDensity.current
    val basePx = with(density) { base.toPx() }
    val maxPx = with(density) { maxWidth.toPx() }.coerceAtLeast(1f)
    // Tabular Source Sans 3 is ~0.58em average glyph width incl. currency symbol.
    val estimated = text.length * basePx * 0.58f
    val scale = if (estimated <= maxPx) 1f else (maxPx / estimated).coerceIn(0.38f, 1f)
    return (base.value * scale).coerceAtLeast(11f).sp
}

/** Primary action button — solid crimson with white text, tactile recoil physics and nested icon support. */
@Composable
fun GiltButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    height: Dp = 54.dp,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.95f else 1f,
        animationSpec = if (pressed) Motion.pressSpring() else Motion.recoilSpring(),
        label = "giltPress"
    )
    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        shape = AppShapes.r14,
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
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        modifier = modifier
            .scale(scale)
            .heightIn(min = height)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text.uppercase(),
                style = AppType.button,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = if (trailingIcon != null) Modifier.padding(end = 10.dp) else Modifier
            )
            if (trailingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    trailingIcon()
                }
            }
        }
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
                // Recessed ink-well on paper stock (no glass)
                .paperSurface(
                    rememberPaperSpec(PaperKind.FIELD, goldEdge = false),
                    RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
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
    var dismissed by remember { mutableStateOf(false) }
    var confirmed by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { dismissed = true }) {
        // Fire the real callback only after the exit animation finishes. Keyed on both
        // flags so the callback can never double-fire; once the caller clears its dialog
        // state, this composition (and effect) leaves the tree and the effect is cancelled.
        LaunchedEffect(dismissed, confirmed) {
            if (dismissed) {
                delay((if (Motion.enabled) Motion.FeedbackMs else 1).toLong())
                onDismissRequest()
            } else if (confirmed) {
                delay((if (Motion.enabled) Motion.FeedbackMs else 1).toLong())
                onConfirm()
            }
        }

        AnimatedVisibility(
            visible = !dismissed && !confirmed,
            enter = fadeIn(Motion.state()) + scaleIn(
                initialScale = Motion.RiseScale,
                animationSpec = Motion.state()
            ),
            exit = fadeOut(Motion.feedback()) + scaleOut(
                targetScale = Motion.RiseScale,
                animationSpec = Motion.feedback()
            )
        ) {
            Surface(
                shape = AppShapes.r20,
                color = Color.Transparent,
                modifier = Modifier.paperSurface(
                    rememberPaperSpec(PaperKind.RAISED, goldEdge = true),
                    AppShapes.r20
                )
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
                        TextButton(onClick = { dismissed = true }, modifier = Modifier.weight(1f)) {
                            Text(dismissLabel, color = c.textMuted)
                        }
                        if (confirmDanger) {
                            Button(
                                onClick = { confirmed = true },
                                shape = AppShapes.r12,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = c.danger.copy(alpha = 0.18f),
                                    contentColor = c.danger
                                ),
                                border = BorderStroke(1.dp, c.danger),
                                modifier = Modifier.weight(1f)
                            ) {
                                // Uppercase to match GiltButton confirms — dialog
                                // actions read as one voice regardless of style.
                                Text(confirmLabel.uppercase(), fontWeight = FontWeight.SemiBold)
                            }
                        } else {
                            GiltButton(
                                onClick = { confirmed = true },
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
}

/** Gold "SEALED" stamp overlay for sheet-complete / day celebration. */
@Composable
fun SealStampOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier,
    label: String = "SEALED",
    caption: String? = null
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = label,
                    style = AppType.stamp,
                    color = AppTheme.colors.gold.copy(alpha = 0.92f)
                )
                if (caption != null) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = caption,
                        style = AppType.meta,
                        color = AppTheme.colors.textSecondary
                    )
                }
            }
        }
    }
}

/**
 * Shared ledger column metrics — widths grow with fontScale so enlarged text
 * never clips into neighboring columns (D8 + Log visibility fix).
 *
 * Text columns (TIME / COST / TYPE) track the scale at full rate; the gutter
 * stays on the damped [scaledSpacing] curve. No delete column: delete is a
 * long-press on the row (D97), which also frees ~44-57dp per row at Extra
 * large and lets COST widen to 92dp so everyday amounts render at full size.
 */
fun ledgerScaledFactor(fontScale: Float): Float = fontScale.coerceIn(0.85f, 1.45f)

@Composable
private fun ledgerScaled(base: Float): Dp =
    (base * ledgerScaledFactor(LocalDensity.current.fontScale)).dp

internal data class LedgerColumnMetrics(
    val timeW: Dp,
    val costW: Dp,
    val typeW: Dp,
    val gutter: Dp,
    val trail: Dp
)

/** Column widths at a given fontScale — pure so the Extra-large width budget is unit-testable. */
internal fun ledgerColumnMetricsAt(fontScale: Float): LedgerColumnMetrics {
    val f = ledgerScaledFactor(fontScale)
    val c = scaledSpacingFactor(fontScale)
    return LedgerColumnMetrics(
        timeW = (40f * f).dp,
        costW = (92f * f).dp,
        typeW = (36f * f).dp,
        gutter = (6f * c).dp,
        trail = (4f * c).dp
    )
}

@Composable
private fun ledgerColumnMetrics(): LedgerColumnMetrics =
    ledgerColumnMetricsAt(LocalDensity.current.fontScale)

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
            overflow = TextOverflow.Ellipsis
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
            overflow = TextOverflow.Ellipsis
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
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Single expense line used on both Log and History.
 * Item is the primary readable field (up to 2 lines); trailing cluster stays compact.
 *
 * Delete is a long-press on the row (D97) — the caller opens its confirm dialog.
 * No trash affordance, so the row keeps every pixel of width at Extra large.
 */
@OptIn(ExperimentalFoundationApi::class)
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
    // Indication comes from the theme-wide ink wave (D99) via LocalIndication.
    val sfx = rememberAppSfx()
    val press = Modifier.combinedClickable(
        onClick = { sfx.tap() },
        onLongClick = {
            sfx.longPress()
            onDelete()
        },
        onLongClickLabel = "Delete entry",
        role = Role.Button
    )
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
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(m.gutter))
            Text(
                text = entry.item,
                style = AppType.ledgerItem,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
            Spacer(Modifier.width(m.gutter))
            Text(
                text = money,
                style = AppType.moneySm.copy(
                    fontSize = adaptiveMoneySize(money, 15.sp)
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
                        .border(BorderStroke(1.2.dp, typeColor), AppShapes.r8),
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
        }
    }

    if (showCard) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                // Paper first, then press: the ink wave paints ON TOP of the
                // stock and is clipped to the card shape (ripple convention).
                .paperSurface(
                    rememberPaperSpec(PaperKind.CHIP, goldEdge = true),
                    AppShapes.r12
                )
                .then(press)
        ) {
            content()
        }
    } else {
        Box(modifier = modifier.fillMaxWidth().then(press)) {
            content()
        }
    }
}

@Composable
private fun ledgerHeaderStyle(letterSpacing: TextUnit = 1.0.sp): TextStyle =
    AppType.ledgerHeader.copy(letterSpacing = letterSpacing)

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
            .paperSurface(
                rememberPaperSpec(PaperKind.CHIP, goldEdge = true),
                AppShapes.r16
            )
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(accentColor, AppShapes.r6)
            )
            Spacer(Modifier.width(8.dp))
            Eyebrow(insight.title.uppercase(), color = accentColor, size = 11)
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = insight.body,
            style = AppType.bodyMd,
            color = palette.textPrimary
        )
    }
}

/**
 * The one selectable filter chip (D190). Tinted fill + stronger border when
 * selected; tactile press recoil. TypeChip and EditTypeChip were near-duplicates
 * and are consolidated here.
 */
@Composable
fun SelectChip(
    label: String,
    selected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    /** Dense variant for scrolled chip rows (hour stamps, replay, suggestions). */
    compact: Boolean = false,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val bgColor by animateColorAsState(
        targetValue = if (selected) color.copy(alpha = 0.16f) else AppTheme.colors.surfaceSunken,
        animationSpec = Motion.state(),
        label = "selectChipBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) color else AppTheme.colors.dividerStrong,
        animationSpec = Motion.state(),
        label = "selectChipBorder"
    )
    Surface(
        onClick = onClick,
        interactionSource = interaction,
        shape = if (compact) AppShapes.r8 else AppShapes.r12,
        color = bgColor,
        border = BorderStroke(if (selected) 1.5.dp else 1.dp, borderColor),
        modifier = modifier
            .heightIn(min = if (compact) 44.dp else 48.dp)
            .pressRecoil(interaction)
    ) {
        Box(
            modifier = Modifier.padding(
                if (compact) PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                else PaddingValues(horizontal = 14.dp, vertical = 12.dp)
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                label,
                color = if (selected) color else AppTheme.colors.textSecondary,
                style = if (compact) {
                    AppType.meta.copy(
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                    )
                } else {
                    AppType.button.copy(
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                    )
                }
            )
        }
    }
}
