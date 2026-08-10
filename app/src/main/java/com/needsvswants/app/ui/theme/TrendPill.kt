package com.needsvswants.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Compact trend indicator (design audit #5). Crimson pill for "spending more",
 * green for "spending less", muted for flat. Uses the app's meta typography.
 */
@Composable
fun TrendPill(
    trendPct: Int,
    comparedTo: String,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    val (bg, fg, arrow) = when {
        trendPct > 0 -> Triple(palette.crimson.copy(alpha = 0.14f), palette.crimson, "↑")
        trendPct < 0 -> Triple(palette.marketGreen.copy(alpha = 0.14f), palette.marketGreen, "↓")
        else -> Triple(palette.surfaceRaised, palette.textMuted, "→")
    }
    Row(
        modifier = modifier
            .background(bg, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$arrow ${kotlin.math.abs(trendPct)}%",
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