package com.needsvswants.app.ui.screens.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/** Kept for backwards-compatible use by other screens. */
@Composable
fun GoldUnderline() = GiltRule(width = 28.dp)

@Composable
fun InputScreen(viewModel: InputViewModel = hiltViewModel()) {
    val entries by viewModel.sheetEntries.collectAsStateWithLifecycle()
    val symbol by viewModel.currencySymbol.collectAsStateWithLifecycle()
    val item by viewModel.activeItem.collectAsStateWithLifecycle()
    val cost by viewModel.activeCost.collectAsStateWithLifecycle()
    val type by viewModel.activeType.collectAsStateWithLifecycle()
    val pendingOverspendCost by viewModel.overspendConfirmCostCents.collectAsStateWithLifecycle()
    val budgetStatus by viewModel.budgetStatus.collectAsStateWithLifecycle()
    val isFull = viewModel.isSheetFull
    val today = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
    val filled = entries.size
    var deleteTarget by remember { mutableStateOf<Entry?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(inkWash())) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Eyebrow("TODAY  ·  $today", color = Crimson)
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("LOG", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
                Column(horizontalAlignment = Alignment.End) {
                    Eyebrow("SHEET", color = TextMuted, size = 10)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "$filled / 20",
                        color = if (filled >= 18) Danger else TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            GiltRule(width = 40.dp)
        }

        if (!isFull) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = InkElevated,
                    border = BorderStroke(1.dp, InkDivider)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        OutlinedTextField(
                            value = item,
                            onValueChange = { viewModel.activeItem.value = viewModel.filterItem(it) },
                            label = { Text("ITEM", style = MaterialTheme.typography.labelSmall) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = Crimson,
                                unfocusedBorderColor = DividerStrong,
                                cursorColor = Crimson,
                                focusedLabelColor = Crimson,
                                unfocusedLabelColor = TextMuted,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(14.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = cost,
                                onValueChange = { viewModel.activeCost.value = viewModel.filterCost(it) },
                                label = { Text("COST", style = MaterialTheme.typography.labelSmall) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = Crimson,
                                    unfocusedBorderColor = DividerStrong,
                                    cursorColor = Crimson,
                                    focusedLabelColor = Crimson,
                                    unfocusedLabelColor = TextMuted,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(Modifier.width(10.dp))
                            TypeChip("NEED", type == EntryType.NEED, Need) {
                                viewModel.activeType.value = EntryType.NEED; viewModel.trySeal()
                            }
                            Spacer(Modifier.width(8.dp))
                            TypeChip("WANT", type == EntryType.WANT, Want) {
                                viewModel.activeType.value = EntryType.WANT; viewModel.trySeal()
                            }
                        }
                    }
                }
            }
            LaunchedEffect(item, cost, type) {
                if (item.isNotBlank() && cost.isNotBlank() && cost.any { it.isDigit() } && type != null) {
                    viewModel.trySeal()
                }
            }
        } else {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, MarketGreen)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Eyebrow("SHEET COMPLETE", color = MarketGreen)
                        Spacer(Modifier.height(8.dp))
                        Text("20 / 20 entries sealed", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Spacer(Modifier.height(14.dp))
                        GiltButton(onClick = {}, text = "Start new sheet", height = 48.dp)
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        if (entries.isNotEmpty()) {
            EntryLedgerHeader(
                modifier = Modifier.padding(horizontal = 34.dp, vertical = 6.dp)
            )
            HorizontalDivider(color = InkDivider, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(entries.reversed()) { _, entry ->
                EntryLedgerRow(
                    entry = entry,
                    symbol = symbol,
                    onDelete = { deleteTarget = entry },
                    showCard = true
                )
            }
        }

        pendingOverspendCost?.let { _ ->
            val on = budgetStatus as? BudgetStatus.On
            if (on != null) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissOverspendConfirm() },
                    containerColor = InkElevated,
                    tonalElevation = 0.dp,
                    shape = RoundedCornerShape(20.dp),
                    title = {
                        Column {
                            Eyebrow("DAILY BUDGET", color = Danger)
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Over budget?",
                                color = TextPrimary,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    },
                    text = {
                        Text(
                            "This puts you over your daily budget of ${on.budgetCents.toMoney(symbol)}. Log anyway?",
                            color = TextSecondary
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { viewModel.confirmOverspendSeal() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Crimson.copy(alpha = 0.18f),
                                contentColor = Crimson
                            ),
                            border = BorderStroke(1.dp, Crimson)
                        ) { Text("Log anyway", fontWeight = FontWeight.SemiBold) }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.dismissOverspendConfirm() }) {
                            Text("Cancel", color = TextMuted)
                        }
                    }
                )
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
    }
}

@Composable
private fun TypeChip(label: String, selected: Boolean, color: Color, onClick: () -> Unit) {
    val bgColor by animateColorAsState(if (selected) color.copy(alpha = 0.18f) else Color.Transparent, label = "chipBg")
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        border = BorderStroke(1.dp, if (selected) color else InkDividerStrong)
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            color = if (selected) color else TextSecondary,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
        )
    }
}
