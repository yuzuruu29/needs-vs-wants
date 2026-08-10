package com.needsvswants.app.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Pre-built skeleton placeholders (design audit #2). Dimensions match the loaded
 * cards they replace so the shimmer→content swap causes zero layout shift.
 */

/** Matches the FloatingGeminiOrb hero (210dp circle). */
@Composable
fun SummarySkeletonDonut(modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(210.dp), contentAlignment = Alignment.Center) {
        PaperShimmer(Modifier.size(210.dp), CircleShape)
    }
}

/** Matches the Need/Want stat card pair (2-up rectangles). */
@Composable
fun SummarySkeletonStatRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PaperShimmer(Modifier.weight(1f).height(104.dp))
        PaperShimmer(Modifier.weight(1f).height(104.dp))
    }
}

/** Matches the SparklineChart band (full-width × 60dp). */
@Composable
fun SummarySkeletonSparkline(modifier: Modifier = Modifier) {
    PaperShimmer(modifier.fillMaxWidth().height(60.dp))
}

/** Matches a History day-group card. */
@Composable
fun HistorySkeletonGroup(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PaperShimmer(Modifier.fillMaxWidth().height(40.dp), RoundedCornerShape(14.dp))
        PaperShimmer(Modifier.fillMaxWidth().height(40.dp), RoundedCornerShape(14.dp))
    }
}

/** Matches a Paywall PlanTierCard (reserved; paywall data is local-fast). */
@Composable
fun PaywallSkeletonCard(modifier: Modifier = Modifier) {
    PaperShimmer(modifier.fillMaxWidth().height(150.dp), RoundedCornerShape(20.dp))
}