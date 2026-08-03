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
import com.needsvswants.app.domain.DailyBudgetMath
import com.needsvswants.app.domain.toInputAmount
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
    val dailyBudgetCents by viewModel.dailyBudgetCents.collectAsStateWithLifecycle()
    val isFull = viewModel.isSheetFull
    val today = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
    val filled = entries.size
    var deleteTarget by remember { mutableStateOf<Entry?>(null) }
    var budgetAmount by remember { mutableStateOf("") }
    var budgetError by remember { mutableStateOf(false) }
    var editingBudget by remember { mutableStateOf(false) }

    LaunchedEffect(dailyBudgetCents) {
        val cents = dailyBudgetCents
        if (cents == null) {
            editingBudget = true
        } else {
            if (budgetAmount.isBlank()) {
                budgetAmount = cents.toInputAmount()
            }
            editingBudget = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(themedInkWash())) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Eyebrow("TODAY  ·  $today", color = AppTheme.colors.crimson)
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("LOG", style = MaterialTheme.typography.displayLarge, color = AppTheme.colors.textPrimary)
                Column(horizontalAlignment = Alignment.End) {
                    Eyebrow("SHEET", color = AppTheme.colors.textMuted, size = 10)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "$filled / 20",
                        color = if (filled >= 18) AppTheme.colors.danger else AppTheme.colors.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            GiltRule(width = 40.dp)
            Spacer(Modifier.height(12.dp))
            LogDailyBudgetSection(
                budgetStatus = budgetStatus,
                symbol = symbol,
                budgetAmount = budgetAmount,
                budgetError = budgetError,
                editingBudget = editingBudget,
                onBudgetAmountChange = {
                    budgetAmount = viewModel.filterBudgetAmount(it)
                    budgetError = false
                },
                onSave = {
                    if (viewModel.saveDailyBudget(budgetAmount)) {
                        budgetAmount = ""
                        budgetError = false
                        editingBudget = false
                    } else {
                        budgetError = true
                    }
                },
                onClear = {
                    viewModel.clearDailyBudget()
                    budgetAmount = ""
                    budgetError = false
                    editingBudget = true
                },
                onStartEdit = {
                    dailyBudgetCents?.let { budgetAmount = it.toInputAmount() }
                    editingBudget = true
                },
                onCancelEdit = {
                    budgetError = false
                    editingBudget = false
                    dailyBudgetCents?.let { budgetAmount = it.toInputAmount() }
                }
            )
        }

        if (!isFull) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = AppTheme.colors.inkElevated,
                    border = BorderStroke(1.dp, AppTheme.colors.inkDivider)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        OutlinedTextField(
                            value = item,
                            onValueChange = { viewModel.activeItem.value = viewModel.filterItem(it) },
                            label = { Text("ITEM", style = MaterialTheme.typography.labelSmall) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = AppTheme.colors.textPrimary,
                                unfocusedTextColor = AppTheme.colors.textPrimary,
                                focusedBorderColor = AppTheme.colors.crimson,
                                unfocusedBorderColor = AppTheme.colors.dividerStrong,
                                cursorColor = AppTheme.colors.crimson,
                                focusedLabelColor = AppTheme.colors.crimson,
                                unfocusedLabelColor = AppTheme.colors.textMuted,
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
                                    focusedTextColor = AppTheme.colors.textPrimary,
                                    unfocusedTextColor = AppTheme.colors.textPrimary,
                                    focusedBorderColor = AppTheme.colors.crimson,
                                    unfocusedBorderColor = AppTheme.colors.dividerStrong,
                                    cursorColor = AppTheme.colors.crimson,
                                    focusedLabelColor = AppTheme.colors.crimson,
                                    unfocusedLabelColor = AppTheme.colors.textMuted,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(Modifier.width(10.dp))
                            TypeChip("NEED", type == EntryType.NEED, AppTheme.colors.need) {
                                viewModel.activeType.value = EntryType.NEED; viewModel.trySeal()
                            }
                            Spacer(Modifier.width(8.dp))
                            TypeChip("WANT", type == EntryType.WANT, AppTheme.colors.want) {
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
                    color = AppTheme.colors.surfaceCard,
                    border = BorderStroke(1.dp, AppTheme.colors.marketGreen)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Eyebrow("SHEET COMPLETE", color = AppTheme.colors.marketGreen)
                        Spacer(Modifier.height(8.dp))
                        Text("20 / 20 entries sealed", style = MaterialTheme.typography.titleMedium, color = AppTheme.colors.textPrimary)
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
            HorizontalDivider(color = AppTheme.colors.inkDivider, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
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

        pendingOverspendCost?.let { pendingCost ->
            val on = budgetStatus as? BudgetStatus.On
            if (on != null) {
                val overBy = DailyBudgetMath.overBy(on.spentCents, on.budgetCents, pendingCost)
                AlertDialog(
                    onDismissRequest = { viewModel.dismissOverspendConfirm() },
                    containerColor = AppTheme.colors.inkElevated,
                    tonalElevation = 0.dp,
                    shape = RoundedCornerShape(20.dp),
                    title = {
                        Column {
                            Eyebrow("DAILY BUDGET", color = AppTheme.colors.danger)
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Over budget?",
                                color = AppTheme.colors.textPrimary,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    },
                    text = {
                        Column {
                            Text(
                                "\"$item\" · ${pendingCost.toMoney(symbol)}",
                                color = AppTheme.colors.textPrimary,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "This puts you over by ${overBy.toMoney(symbol)}. Your daily budget is ${on.budgetCents.toMoney(symbol)}. Log anyway?",
                                color = AppTheme.colors.textSecondary
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { viewModel.confirmOverspendSeal() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppTheme.colors.crimson.copy(alpha = 0.18f),
                                contentColor = AppTheme.colors.crimson
                            ),
                            border = BorderStroke(1.dp, AppTheme.colors.crimson)
                        ) { Text("Log anyway", fontWeight = FontWeight.SemiBold) }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.dismissOverspendConfirm() }) {
                            Text("Cancel", color = AppTheme.colors.textMuted)
                        }
                    }
                )
            }
        }

        deleteTarget?.let { entry ->
            AlertDialog(
                onDismissRequest = { deleteTarget = null },
                containerColor = AppTheme.colors.inkElevated,
                tonalElevation = 0.dp,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Column {
                        Eyebrow("CONFIRM", color = AppTheme.colors.danger)
                        Spacer(Modifier.height(6.dp))
                        Text("Delete entry?", color = AppTheme.colors.textPrimary, style = MaterialTheme.typography.headlineSmall)
                    }
                },
                text = { Text("${entry.item} — ${entry.costCents.toMoney(symbol)}", color = AppTheme.colors.textSecondary) },
                confirmButton = {
                    Button(
                        onClick = { viewModel.deleteEntry(entry); deleteTarget = null },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.danger.copy(alpha = 0.18f), contentColor = AppTheme.colors.danger),
                        border = BorderStroke(1.dp, AppTheme.colors.danger)
                    ) { Text("Delete", fontWeight = FontWeight.SemiBold) }
                },
                dismissButton = {
                    TextButton(onClick = { deleteTarget = null }) { Text("Cancel", color = AppTheme.colors.textMuted) }
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
        border = BorderStroke(1.dp, if (selected) color else AppTheme.colors.inkDividerStrong)
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            color = if (selected) color else AppTheme.colors.textSecondary,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
        )
    }
}

/**
 * Daily budget lives on Log: meter when on; amount field to set/update; Turn off to clear.
 * When a limit is active, the form collapses until the user taps Change.
 */
@Composable
private fun LogDailyBudgetSection(
    budgetStatus: BudgetStatus,
    symbol: String,
    budgetAmount: String,
    budgetError: Boolean,
    editingBudget: Boolean,
    onBudgetAmountChange: (String) -> Unit,
    onSave: () -> Unit,
    onClear: () -> Unit,
    onStartEdit: () -> Unit,
    onCancelEdit: () -> Unit
) {
    val budgetOn = budgetStatus is BudgetStatus.On

    if (budgetOn) {
        DailyBudgetMeter(status = budgetStatus as BudgetStatus.On, symbol = symbol)
        if (!editingBudget) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onStartEdit) {
                    Text("Change", color = AppTheme.colors.textSecondary, fontWeight = FontWeight.Medium)
                }
                TextButton(onClick = onClear) {
                    Text("Turn off", color = AppTheme.colors.danger, fontWeight = FontWeight.Medium)
                }
            }
        } else {
            Spacer(Modifier.height(8.dp))
        }
    }

    if (editingBudget || !budgetOn) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = AppTheme.colors.inkElevated,
            border = BorderStroke(1.dp, AppTheme.colors.inkDivider)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (!budgetOn) {
                    Eyebrow("DAILY BUDGET", color = AppTheme.colors.gilt)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Optional. Off until you set an amount.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.colors.textSecondary
                    )
                    Spacer(Modifier.height(12.dp))
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Eyebrow("UPDATE LIMIT", color = AppTheme.colors.gilt)
                        TextButton(onClick = onCancelEdit) {
                            Text("Cancel", color = AppTheme.colors.textMuted, fontWeight = FontWeight.Medium)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
                OutlinedTextField(
                    value = budgetAmount,
                    onValueChange = onBudgetAmountChange,
                    label = { Text("AMOUNT", style = MaterialTheme.typography.labelSmall) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = budgetError,
                    supportingText = if (budgetError) {
                        { Text("Enter a valid amount", color = AppTheme.colors.danger) }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = AppTheme.colors.textPrimary,
                        unfocusedTextColor = AppTheme.colors.textPrimary,
                        focusedBorderColor = AppTheme.colors.crimson,
                        unfocusedBorderColor = AppTheme.colors.dividerStrong,
                        cursorColor = AppTheme.colors.crimson,
                        focusedLabelColor = AppTheme.colors.crimson,
                        unfocusedLabelColor = AppTheme.colors.textMuted,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GiltButton(
                        onClick = onSave,
                        text = if (budgetOn) "Update budget" else "Save budget",
                        height = 46.dp,
                        enabled = budgetAmount.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    )
                    if (budgetOn) {
                        TextButton(onClick = onClear) {
                            Text("Turn off", color = AppTheme.colors.danger, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
