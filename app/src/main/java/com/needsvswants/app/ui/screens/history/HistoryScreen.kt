package com.needsvswants.app.ui.screens.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    onNavigateToInput: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val entries by viewModel.entries.collectAsStateWithLifecycle()
    val symbol by viewModel.currencySymbol.collectAsStateWithLifecycle()
    var deleteTarget by remember { mutableStateOf<Entry?>(null) }

    val grouped = entries.groupBy { it.date }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().background(inkWash()).padding(horizontal = 20.dp).padding(top = 20.dp, bottom = 12.dp)) {
        Eyebrow("HISTORY", color = Crimson)
        Spacer(Modifier.height(6.dp))
        Text("LEDGER", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
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
                        "${displayFormat.format(oldestDate)} — ${displayFormat.format(newestDate)}",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.4.sp),
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (entries.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier
                        .size(96.dp)
                        .border(1.dp, Crimson.copy(alpha = 0.35f), RoundedCornerShape(48.dp))
                    )
                    Spacer(Modifier.height(18.dp))
                    Eyebrow("EMPTY DIARY", color = TextMuted)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "The page waits for ink.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                grouped.forEach { (date, dayEntries) ->
                    item {
                        val displayDate = dateFormat.parse(date)?.let { displayFormat.format(it) } ?: date
                        val dayNeeds = dayEntries.filter { it.type == EntryType.NEED }.sumOf { it.costCents }
                        val dayWants = dayEntries.filter { it.type == EntryType.WANT }.sumOf { it.costCents }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = InkElevated,
                            border = BorderStroke(1.dp, InkDivider)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        displayDate.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.6.sp),
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        "${dayEntries.size} entries",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = TextMuted
                                    )
                                }
                                Spacer(Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    DayTotal(Need, "Need", dayNeeds.toMoney(symbol))
                                    DayTotal(Want, "Want", dayWants.toMoney(symbol))
                                }
                                Spacer(Modifier.height(14.dp))
                                HorizontalDivider(color = InkDivider, thickness = 1.dp)
                                Spacer(Modifier.height(8.dp))
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
        }

        deleteTarget?.let { entry ->
            AlertDialog(
                onDismissRequest = { deleteTarget = null },
                containerColor = InkElevated,
                tonalElevation = 0.dp,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Column {
                        Eyebrow("CONFIRM", color = Danger)
                        Spacer(Modifier.height(6.dp))
                        Text("Delete entry?", color = TextPrimary, style = MaterialTheme.typography.headlineSmall)
                    }
                },
                text = { Text("${entry.item} — ${entry.costCents.toMoney(symbol)}", color = TextSecondary) },
                confirmButton = {
                    Button(
                        onClick = { viewModel.deleteEntry(entry); deleteTarget = null },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Danger.copy(alpha = 0.18f), contentColor = Danger),
                        border = BorderStroke(1.dp, Danger)
                    ) { Text("Delete", fontWeight = FontWeight.SemiBold) }
                },
                dismissButton = {
                    TextButton(onClick = { deleteTarget = null }) { Text("Cancel", color = TextMuted) }
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        GiltButton(
            onClick = onNavigateToInput,
            text = "Log an expense",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DayTotal(color: Color, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(Modifier.size(8.dp)) {
            drawCircle(color)
        }
        Spacer(Modifier.width(6.dp))
        Text(
            "$label ",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                letterSpacing = 0.1.sp
            ),
            color = TextSecondary,
            maxLines = 1,
            softWrap = false
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = adaptiveMoneySize(value, 13.sp),
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.15.sp,
                fontFeatureSettings = "tnum"
            ),
            color = TextPrimary,
            maxLines = 1,
            softWrap = false
        )
    }
}
