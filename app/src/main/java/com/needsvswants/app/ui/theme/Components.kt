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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.needsvswants.app.domain.toMoney

/** Soft vertical wash used on every screen — light top → slightly warmer raised base. */
fun inkWash(top: Color = Surface, bottom: Color = SurfaceRaised) = Brush.verticalGradient(
    colors = listOf(top, bottom)
)

/** Faint gold radial glow used behind hero elements (donut, stat hero). */
fun giltGlow(alpha: Float = 0.16f) = Brush.radialGradient(
    colors = listOf(Gold.copy(alpha = alpha), Color.Transparent)
)

/** Horizontal gold stroke gradient used to crown premium cards. */
private fun topStrokeGradient() = BorderStroke(1.dp, Brush.horizontalGradient(
    listOf(Color.Transparent, Gold.copy(alpha = 0.55f), Color.Transparent)
))

/** Eyebrow label — wide-spaced uppercase micro heading. */
@Composable
fun Eyebrow(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Crimson,
    size: Int = 11,
    align: TextAlign? = null,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE
) {
    val baseStyle = MaterialTheme.typography.labelSmall.copy(
        fontSize = size.sp,
        letterSpacing = (size * 0.12f).sp
    )
    Text(
        text = text.uppercase(),
        style = if (align != null) baseStyle.copy(textAlign = align) else baseStyle,
        color = color,
        modifier = modifier,
        softWrap = softWrap,
        maxLines = maxLines
    )
}

/** The short gold rule used under section titles. */
@Composable
fun GiltRule(modifier: Modifier = Modifier, width: Dp = 32.dp, height: Dp = 1.5.dp) {
    Box(modifier = modifier
        .width(width)
        .height(height)
        .background(Gold)
    )
}

/** Backwards-compatible name originally used by screens. */
@Composable
fun GoldUnderline() = GiltRule(width = 28.dp)

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

/** Premium card — white surface, hairline border, optional gold top glow. */
@Composable
fun GiltCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    giltAccent: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = SurfaceCard,
        border = BorderStroke(1.dp, Divider)
    ) {
        Column {
            content()
        }
    }
    if (giltAccent) {
        // Crown drawn inside parent via overlay in actual screens; keep helper non-breaking.
    }
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
            containerColor = Crimson,
            contentColor = SurfaceCard,
            disabledContainerColor = Crimson.copy(alpha = 0.35f),
            disabledContentColor = SurfaceCard.copy(alpha = 0.6f)
        ),
        modifier = modifier.height(height)
    ) {
        Text(
            text = text.uppercase(),
            style = TextStyle(
                fontFamily = BodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                letterSpacing = 1.2.sp
            )
        )
    }
}

/**
 * Shared ledger column metrics — header and rows must use the same widths
 * so TIME / ITEM / COST / TYPE never drift or wrap awkwardly.
 */
private object LedgerCols {
    val Time = 48.dp
    val Cost = 88.dp
    val Type = 42.dp
    val Delete = 32.dp
    val GutterTight = 6.dp
    val Gutter = 10.dp
    val TrailGutter = 8.dp
}

/** Column labels for the sealed-entry table on the Log screen. */
@Composable
fun EntryLedgerHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "TIME",
            style = ledgerHeaderStyle(),
            color = TextMuted,
            modifier = Modifier.width(LedgerCols.Time),
            maxLines = 1,
            softWrap = false
        )
        Spacer(Modifier.width(LedgerCols.Gutter))
        Text(
            text = "ITEM",
            style = ledgerHeaderStyle(),
            color = TextMuted,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            softWrap = false
        )
        Spacer(Modifier.width(LedgerCols.Gutter))
        Text(
            text = "COST",
            style = ledgerHeaderStyle(),
            color = TextMuted,
            modifier = Modifier.width(LedgerCols.Cost),
            textAlign = TextAlign.End,
            maxLines = 1,
            softWrap = false
        )
        Spacer(Modifier.width(LedgerCols.TrailGutter))
        Text(
            text = "TYPE",
            style = ledgerHeaderStyle(letterSpacing = 0.55.sp),
            color = TextMuted,
            modifier = Modifier.width(LedgerCols.Type),
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false
        )
        Spacer(Modifier.width(LedgerCols.GutterTight))
        Spacer(Modifier.width(LedgerCols.Delete))
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
    val typeColor = if (entry.type == EntryType.NEED) Need else Want
    val money = entry.costCents.toMoney(symbol)
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
                color = TextMuted,
                modifier = Modifier.width(LedgerCols.Time),
                maxLines = 1,
                softWrap = false
            )
            Spacer(Modifier.width(LedgerCols.Gutter))
            Text(
                text = entry.item,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(LedgerCols.Gutter))
            Text(
                text = money,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFeatureSettings = "tnum",
                    fontSize = adaptiveMoneySize(money, 13.sp)
                ),
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.width(LedgerCols.Cost),
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(LedgerCols.TrailGutter))
            Box(
                modifier = Modifier
                    .width(LedgerCols.Type)
                    .height(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
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
            Spacer(Modifier.width(LedgerCols.GutterTight))
            Box(
                modifier = Modifier
                    .size(LedgerCols.Delete)
                    .clickable(role = Role.Button, onClick = onDelete),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Danger.copy(alpha = 0.55f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }

    if (showCard) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = InkElevated,
            border = BorderStroke(1.dp, InkDivider)
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
