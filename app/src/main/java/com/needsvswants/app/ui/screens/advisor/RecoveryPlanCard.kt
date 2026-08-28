package com.needsvswants.app.ui.screens.advisor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.needsvswants.app.domain.RecoveryPlan
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.AppType
import com.needsvswants.app.ui.theme.Eyebrow
import com.needsvswants.app.ui.theme.GiltRule
import com.needsvswants.app.ui.theme.Motion

/**
 * Informational 3-day compensatory recovery card: day caps for the next three
 * days, an optional need-only-evenings line, and the study citation. No action
 * buttons beyond dismiss (sealing is handled elsewhere). Enters with
 * [Motion.entrance], which collapses to static when motion is disabled.
 */
@Composable
fun RecoveryPlanCard(
    plan: RecoveryPlan,
    currencySymbol: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(Motion.entrance()),
        label = "recoveryPlanCard"
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = palette.surfaceCard,
            border = BorderStroke(1.dp, palette.crimson.copy(alpha = 0.45f)),
            shadowElevation = 2.dp,
            tonalElevation = 0.dp,
            modifier = modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Eyebrow("RECOVERY PLAN", color = palette.crimson, size = 10)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "3-day plan · over by ${plan.overByCents.toMoney(currencySymbol)}",
                            style = AppType.sectionTitle,
                            color = palette.textPrimary
                        )
                    }
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Dismiss recovery plan",
                        tint = palette.textMuted,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onDismiss)
                            .padding(4.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                plan.dayCaps.forEachIndexed { index, capCents ->
                    RecoveryDayRow(
                        day = "D+${index + 1}",
                        capCents = capCents,
                        currencySymbol = currencySymbol
                    )
                }

                if (plan.needOnlyEvenings) {
                    Spacer(Modifier.height(8.dp))
                    NeedOnlyEveningsRow()
                }

                Spacer(Modifier.height(12.dp))
                GiltRule(width = 28.dp)
                Spacer(Modifier.height(10.dp))
                Eyebrow("CITATION", color = palette.gilt, size = 10)
                Text(
                    text = plan.citation,
                    style = AppType.bodySm,
                    color = palette.textSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun RecoveryDayRow(
    day: String,
    capCents: Long,
    currencySymbol: String
) {
    val palette = AppTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = day,
            style = AppType.meta,
            color = palette.textPrimary,
            modifier = Modifier.width(40.dp)
        )
        Text(
            text = "Daily Want cap",
            style = AppType.bodySm,
            color = palette.textSecondary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = capCents.toMoney(currencySymbol),
            style = AppType.moneySm,
            color = palette.crimson
        )
    }
}

@Composable
private fun NeedOnlyEveningsRow() {
    val palette = AppTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(palette.marketGreen)
        )
        Text(
            text = "Need-only evenings",
            style = AppType.bodySm,
            color = palette.textPrimary,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        )
        Text(
            text = "ON",
            style = AppType.eyebrowSm,
            color = palette.marketGreen
        )
    }
}
