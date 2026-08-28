package com.needsvswants.app.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.needsvswants.app.MainActivity
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.di.WidgetEntryPoint
import com.needsvswants.app.domain.StreakMath
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.theme.Crimson
import com.needsvswants.app.ui.theme.Gold
import com.needsvswants.app.ui.theme.MarketGreen
import com.needsvswants.app.ui.theme.Surface
import com.needsvswants.app.ui.theme.TextMuted
import com.needsvswants.app.ui.theme.TextPrimary
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Live home-screen tile: today's Need/Want split + streak.
 * Brand palette (supermarket light) — not a dark marketing card.
 */
class NvwWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val snapshot = loadSnapshot(context)
        provideContent {
            WidgetContent(snapshot)
        }
    }

    companion object {
        suspend fun refreshAll(context: Context) {
            NvwWidget().updateAll(context)
        }

        private suspend fun loadSnapshot(context: Context): WidgetSnapshot {
            val app = context.applicationContext
            val ep = EntryPointAccessors.fromApplication(app, WidgetEntryPoint::class.java)
            val entries = ep.entryRepository().observeAll().first()
            val symbol = ep.appPreferences().currencySymbol.first()

            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val todayEntries = entries.filter { it.date == today }
            val needsCents = todayEntries.filter { it.type == EntryType.NEED }.sumOf { it.costCents }
            val wantsCents = todayEntries.filter { it.type == EntryType.WANT }.sumOf { it.costCents }
            val total = needsCents + wantsCents
            val needPct = if (total > 0) ((needsCents * 100) / total).toInt() else 0
            val wantPct = if (total > 0) 100 - needPct else 0
            val streak = StreakMath.currentStreak(entries.map { it.date }.distinct())

            return WidgetSnapshot(
                symbol = symbol,
                needsCents = needsCents,
                wantsCents = wantsCents,
                needPct = needPct,
                wantPct = wantPct,
                streakDays = streak,
                empty = todayEntries.isEmpty()
            )
        }
    }
}

private data class WidgetSnapshot(
    val symbol: String,
    val needsCents: Long,
    val wantsCents: Long,
    val needPct: Int,
    val wantPct: Int,
    val streakDays: Int,
    val empty: Boolean
)

@Composable
private fun WidgetContent(snapshot: WidgetSnapshot) {
    // Single-sourced light supermarket tokens from ui/theme/Color.kt (D7). The widget
    // is fixed-light by design, so it reads the light palette constants directly
    // instead of re-declaring hex values that drift. Glance's ColorProvider(Color)
    // overload takes androidx.compose.ui.graphics.Color (a Long-backed value); passing
    // an android.graphics.Color.parseColor Int would resolve to the @RestrictedApi
    // resource-id overload (lint ResourceType error).
    val bg = ColorProvider(Surface)
    val textPrimary = ColorProvider(TextPrimary)
    val textMuted = ColorProvider(TextMuted)
    val crimson = ColorProvider(Crimson)
    val green = ColorProvider(MarketGreen)
    val gold = ColorProvider(Gold)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(bg)
            .padding(14.dp)
            .clickable(actionStartActivity<MainActivity>()),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "NEEDS vs WANTS",
            style = TextStyle(color = crimson, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(GlanceModifier.height(4.dp))
        Text(
            text = "Today",
            style = TextStyle(color = textMuted, fontSize = 11.sp)
        )
        Spacer(GlanceModifier.height(10.dp))

        if (snapshot.empty) {
            Text(
                text = "No log yet",
                style = TextStyle(color = textPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            )
            Spacer(GlanceModifier.height(6.dp))
            Text(
                text = if (snapshot.streakDays > 0) {
                    "Day ${snapshot.streakDays} · tap to log"
                } else {
                    "Tap to seal a purchase"
                },
                style = TextStyle(color = gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        } else {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "NEED ${snapshot.needPct}%",
                        style = TextStyle(color = green, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = snapshot.needsCents.toMoney(snapshot.symbol),
                        style = TextStyle(color = textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    )
                }
                Spacer(GlanceModifier.width(16.dp))
                Column {
                    Text(
                        text = "WANT ${snapshot.wantPct}%",
                        style = TextStyle(color = crimson, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = snapshot.wantsCents.toMoney(snapshot.symbol),
                        style = TextStyle(color = textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    )
                }
            }
            Spacer(GlanceModifier.height(10.dp))
            Text(
                text = if (snapshot.streakDays > 0) {
                    "Day ${snapshot.streakDays} logged"
                } else {
                    "Tap to log another"
                },
                style = TextStyle(color = gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}
