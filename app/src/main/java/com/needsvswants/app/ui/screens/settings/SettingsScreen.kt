package com.needsvswants.app.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.screens.input.GoldUnderline
import com.needsvswants.app.ui.theme.*

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val currentSymbol by viewModel.currentSymbol.collectAsStateWithLifecycle()
    val currentCode by viewModel.currentCode.collectAsStateWithLifecycle()
    val dailyBudgetCents by viewModel.dailyBudgetCents.collectAsStateWithLifecycle()
    var showWipeConfirm by remember { mutableStateOf(false) }
    var budgetAmount by remember { mutableStateOf("") }
    var budgetError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(inkWash()).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp).padding(top = 20.dp, bottom = 12.dp)) {
        Eyebrow("PREFERENCES", color = Crimson)
        Spacer(Modifier.height(6.dp))
        Text("SETTINGS", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        GiltRule(width = 40.dp)

        Spacer(Modifier.height(28.dp))

        SectionLabel("CURRENCY")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = InkElevated,
            border = BorderStroke(1.dp, InkDivider)
        ) {
            Column(modifier = Modifier.padding(6.dp)) {
                currencies.forEachIndexed { i, option ->
                    val selected = option.code == currentCode
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setCurrency(option.symbol, option.code) }
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .then(
                                    if (selected) Modifier.background(Brush.horizontalGradient(listOf(Gilt, GiltSoft)), RoundedCornerShape(9.dp))
                                    else Modifier.border(1.dp, InkDividerStrong, RoundedCornerShape(9.dp))
                                )
                        ) {
                            if (selected) Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(Ink, RoundedCornerShape(3.dp))
                                    .fillMaxSize()
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Text(option.symbol, style = MaterialTheme.typography.titleMedium, color = if (selected) Gilt else TextSecondary, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.width(14.dp))
                        Text(option.label, style = MaterialTheme.typography.bodyMedium, color = if (selected) TextPrimary else TextSecondary)
                    }
                    if (i < currencies.lastIndex) HorizontalDivider(color = InkDivider, modifier = Modifier.padding(horizontal = 12.dp), thickness = 1.dp)
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabel("DAILY BUDGET")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = InkElevated,
            border = BorderStroke(1.dp, InkDivider)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    "Optional. Off until you set an amount.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                dailyBudgetCents?.let { currentCents ->
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Eyebrow("ACTIVE LIMIT", color = Gilt)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                currentCents.toMoney(currentSymbol),
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        TextButton(onClick = { viewModel.clearDailyBudget() }) {
                            Text("Turn off", color = Danger, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = budgetAmount,
                    onValueChange = {
                        budgetAmount = viewModel.filterBudgetAmount(it)
                        budgetError = false
                    },
                    label = { Text("AMOUNT", style = MaterialTheme.typography.labelSmall) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = budgetError,
                    supportingText = if (budgetError) {
                        { Text("Enter a valid amount", color = Danger) }
                    } else null,
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
                Spacer(Modifier.height(12.dp))
                GiltButton(
                    onClick = {
                        if (viewModel.saveDailyBudget(budgetAmount)) {
                            budgetAmount = ""
                            budgetError = false
                        } else {
                            budgetError = true
                        }
                    },
                    text = "Save budget",
                    height = 46.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabel("DATA")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = InkElevated,
            border = BorderStroke(1.dp, InkDivider)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showWipeConfirm = true }
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Wipe diary", style = MaterialTheme.typography.bodyMedium, color = Danger, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(2.dp))
                    Text("Permanently delete all entries & reset settings", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.2.sp), color = TextMuted)
                }
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "wipe", tint = Danger.copy(alpha = 0.6f))
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabel("ABOUT")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = InkElevated,
            border = BorderStroke(1.dp, InkDivider),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(Crimson, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(10.dp))
                    Text("Needs vs. Wants Expense Tracker", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.weight(1f))
                    Text("v1.0.0", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "This app allows you to record all of your daily expenses, helping you become more aware of your spending habits and tendencies. By consistently tracking every expense, you can better distinguish between your needs and wants, make smarter financial decisions, and develop stronger self-discipline. The key is to be honest with yourself\u2014every expense counts. Start tracking today and take control of your finances.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }

    if (showWipeConfirm) {
        AlertDialog(
            onDismissRequest = { showWipeConfirm = false },
            containerColor = InkElevated,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column {
                    Eyebrow("DANGER", color = Danger)
                    Spacer(Modifier.height(6.dp))
                    Text("Wipe all data?", color = TextPrimary, style = MaterialTheme.typography.headlineSmall)
                }
            },
            text = {
                Text(
                    "This will permanently delete all entries and reset settings.\nThere is no recovery.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.wipeData(); showWipeConfirm = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Danger, contentColor = Color.White)
                ) { Text("Wipe", fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                TextButton(onClick = { showWipeConfirm = false }) { Text("Cancel", color = TextMuted) }
            }
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Eyebrow(text, color = TextMuted, size = 12)
}
