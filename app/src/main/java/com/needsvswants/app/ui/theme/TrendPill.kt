package com.needsvswants.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.TrendingFlat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Compact trend indicator (design audit #5). Crimson pill for "spending more",
 * green for "spending less", muted for flat. Uses the app's meta typography and
 * real Outlined icons (D189) instead of text arrow glyphs.
 */
@Composable
fun TrendPill(
    trendPct: Int,
    comparedTo: String,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    val (bg, fg, icon) = when {
        trendPct > 0 -> Triple(palette.crimson.copy(alpha = 0.14f), palette.crimson, Icons.Outlined.ArrowDropUp)
        trendPct < 0 -> Triple(palette.marketGreen.copy(alpha = 0.14f), palette.marketGreen, Icons.Outlined.ArrowDropDown)
        else -> Triple(palette.surfaceRaised, palette.textMuted, Icons.Outlined.TrendingFlat)
    }
    Row(
        modifier = modifier
            .background(bg, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = fg,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(2.dp))
        Text(
            text = "${kotlin.math.abs(trendPct)}%",
            style = AppType.meta.copy(fontWeight = FontWeight.SemiBold),
            color = fg
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = comparedTo,
            style = AppType.caption,
            color = fg.copy(alpha = 0.8f)
        )
    }
}
