package com.needsvswants.app.ui.screens.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.navigation.verticalScrollFirst
import com.needsvswants.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.platform.LocalContext
import android.content.Intent

@Composable
fun HistoryScreen(
    onNavigateToInput: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val entries by viewModel.entries.collectAsStateWithLifecycle()
    val symbol by viewModel.currencySymbol.collectAsStateWithLifecycle()
    val isPro by viewModel.isPro.collectAsStateWithLifecycle()
    var deleteTarget by remember { mutableStateOf<Entry?>(null) }
    val haptics = rememberAppHaptics()
    val context = LocalContext.current

    val grouped = entries.groupBy { it.date }.toList()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().background(themedInkWash()).padding(horizontal = 20.dp).padding(top = 20.dp, bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Eyebrow(
                    if (isPro) "LIFETIME HISTORY" else "HISTORY",
                    color = if (isPro) AppTheme.colors.gilt else AppTheme.colors.crimson
                )
                Spacer(Modifier.height(6.dp))
                Text("LEDGER", style = AppType.screenTitle, color = AppTheme.colors.textPrimary)
            }
            if (entries.isNotEmpty()) {
                HeaderIconWell(
                    onClick = {
                        val csv = viewModel.exportCsvText()
                        val send = Intent(Intent.ACTION_SEND).apply {
                            type = "text/csv"
                            putExtra(Intent.EXTRA_TEXT, csv)
                            putExtra(Intent.EXTRA_SUBJECT, "Needs vs Wants - Spending History CSV")
                        }
                        context.startActivity(Intent.createChooser(send, "Export CSV"))
                    },
                    contentDescription = "Export CSV"
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = null,
                        tint = AppTheme.colors.crimson,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        GiltRule(width = 40.dp)
        Spacer(Modifier.height(8.dp))
        if (entries.isNotEmpty()) {
            val oldest = entries.lastOrNull()?.date
            val newest = entries.firstOrNull()?.date
            if (oldest != null && newest != null) {
                val oldestDate = dateFormat.parse(oldest)
                val newestDate = dateFormat.parse(newest)
                if (oldestDate != null && newestDate != null) {
                    Text(
                        "${displayFormat.format(oldestDate)} - ${displayFormat.format(newestDate)}",
                        style = AppType.caption,
                        color = AppTheme.colors.textMuted
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (entries.isEmpty()) {
            val sealBreath = rememberIdleBreathAlpha()
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    NeedWantSealMark(modifier = Modifier.alpha(sealBreath))
                    Spacer(Modifier.height(18.dp))
                    Eyebrow("EMPTY DIARY", color = AppTheme.colors.textMuted)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "The page waits for ink.",
                        style = AppType.bodyMd,
                        color = AppTheme.colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .verticalScrollFirst(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(grouped, key = { it.first }) { (date, dayEntries) ->
                        val displayDate = dateFormat.parse(date)?.let { displayFormat.format(it) } ?: date
                        val dayNeeds = dayEntries.filter { it.type == EntryType.NEED }.sumOf { it.costCents }
                        val dayWants = dayEntries.filter { it.type == EntryType.WANT }.sumOf { it.costCents }

                        val dayTotal = dayNeeds + dayWants
                        val entryLabel = if (dayEntries.size == 1) "1 entry" else "${dayEntries.size} entries"
                        PremiumSurface(
                            modifier = Modifier.animateItem(
                                fadeInSpec = Motion.entrance(),
                                placementSpec = Motion.state<IntOffset>(),
                                fadeOutSpec = Motion.feedback()
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Eyebrow(displayDate, color = AppTheme.colors.crimson, size = 10)
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            dayTotal.toMoney(symbol),
                                            style = AppType.moneyMd.copy(
                                                fontSize = adaptiveMoneySize(
                                                    dayTotal.toMoney(symbol),
                                                    18.sp
                                                )
                                            ),
                                            color = AppTheme.colors.textPrimary,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                    Text(
                                        entryLabel.uppercase(),
                                        style = AppType.eyebrowSm,
                                        color = AppTheme.colors.textMuted,
                                        modifier = Modifier
                                            .border(
                                                BorderStroke(1.dp, AppTheme.colors.dividerStrong),
                                                RoundedCornerShape(20.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                                Spacer(Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    DaySplitChip(
                                        color = AppTheme.colors.need,
                                        label = "Need",
                                        value = dayNeeds.toMoney(symbol),
                                        modifier = Modifier.weight(1f)
                                    )
                                    DaySplitChip(
                                        color = AppTheme.colors.want,
                                        label = "Want",
                                        value = dayWants.toMoney(symbol),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Spacer(Modifier.height(14.dp))
                                GiltRule(width = 28.dp)
                                Spacer(Modifier.height(10.dp))
                                dayEntries.forEach { entry ->
                                    EntryLedgerRow(
                                        entry = entry,
                                        symbol = symbol,
                                        onDelete = { deleteTarget = entry }
                                    )
                                }
                            }
                        }
                }
            }
        }

        deleteTarget?.let { entry ->
            PremiumDialog(
                onDismissRequest = { deleteTarget = null },
                eyebrow = "CONFIRM",
                eyebrowColor = AppTheme.colors.danger,
                title = "Delete entry?",
                body = "${entry.item} · ${entry.costCents.toMoney(symbol)}",
                confirmLabel = "Delete",
                onConfirm = {
                    haptics.warn()
                    viewModel.deleteEntry(entry)
                    deleteTarget = null
                },
                dismissLabel = "Cancel",
                confirmDanger = true
            )
        }

        Spacer(Modifier.height(12.dp))

        GiltButton(
            onClick = onNavigateToInput,
            text = "Log a purchase",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DaySplitChip(
    color: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    Column(
        modifier = modifier
            .background(palette.surfaceSunken, RoundedCornerShape(12.dp))
            .border(BorderStroke(1.dp, color.copy(alpha = 0.28f)), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Canvas(Modifier.size(7.dp)) { drawCircle(color) }
            Spacer(Modifier.width(6.dp))
            Text(
                label.uppercase(),
                style = AppType.eyebrowSm,
                color = color
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            value,
            style = AppType.moneySm.copy(fontSize = adaptiveMoneySize(value, 14.sp)),
            color = palette.textPrimary,
            maxLines = 1,
            softWrap = false
        )
    }
}
