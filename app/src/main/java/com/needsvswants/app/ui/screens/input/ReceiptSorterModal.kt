package com.needsvswants.app.ui.screens.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.ReceiptScanResult
import com.needsvswants.app.domain.ScannedLineItem
import com.needsvswants.app.domain.filterAmountInput
import com.needsvswants.app.domain.parseCents
import com.needsvswants.app.domain.toInputAmount
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.AppType
import com.needsvswants.app.ui.theme.Eyebrow
import com.needsvswants.app.ui.theme.GiltButton
import com.needsvswants.app.ui.theme.GiltRule
import com.needsvswants.app.ui.theme.rememberAppHaptics

@Composable
fun ReceiptSorterModal(
    scanResult: ReceiptScanResult,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onSealBatch: (List<ScannedLineItem>, Long?) -> Unit
) {
    val palette = AppTheme.colors
    val haptics = rememberAppHaptics()

    val items = remember {
        mutableStateListOf<ScannedLineItem>().apply {
            addAll(scanResult.items)
        }
    }

    val storeName = scanResult.storeName ?: "Store Receipt"
    val sortedCount = items.count { it.type != null }
    val needsCount = items.count { it.type == EntryType.NEED }
    val wantsCount = items.count { it.type == EntryType.WANT }
    val needsTotal = items.filter { it.type == EntryType.NEED }.sumOf { it.costCents }
    val wantsTotal = items.filter { it.type == EntryType.WANT }.sumOf { it.costCents }
    val totalCents = items.filter { it.type != null }.sumOf { it.costCents }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(palette.background),
            color = palette.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                // Top navigation bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Eyebrow(
                            text = "RECEIPT SORTER · PRO",
                            color = palette.gold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = storeName,
                            style = AppType.screenTitle,
                            color = palette.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .background(palette.surfaceRaised, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = palette.textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
                GiltRule(modifier = Modifier.padding(horizontal = 20.dp), width = 50.dp)
                Spacer(Modifier.height(12.dp))

                // Batch classification shortcuts
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quick sort all:",
                        style = AppType.caption,
                        color = palette.textMuted
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(palette.marketGreen.copy(alpha = 0.12f))
                            .border(BorderStroke(1.dp, palette.marketGreen.copy(alpha = 0.4f)), RoundedCornerShape(16.dp))
                            .clickable {
                                haptics.tick()
                                for (i in items.indices) {
                                    items[i] = items[i].copy(type = EntryType.NEED)
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "All Need",
                            style = AppType.caption.copy(fontWeight = FontWeight.SemiBold),
                            color = palette.marketGreen
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(palette.crimson.copy(alpha = 0.12f))
                            .border(BorderStroke(1.dp, palette.crimson.copy(alpha = 0.4f)), RoundedCornerShape(16.dp))
                            .clickable {
                                haptics.tick()
                                for (i in items.indices) {
                                    items[i] = items[i].copy(type = EntryType.WANT)
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "All Want",
                            style = AppType.caption.copy(fontWeight = FontWeight.SemiBold),
                            color = palette.crimson
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Line items list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (items.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "All receipt items removed.",
                                    style = AppType.body,
                                    color = palette.textMuted
                                )
                            }
                        }
                    } else {
                        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
                            ScannedItemRow(
                                item = item,
                                currencySymbol = currencySymbol,
                                onNameChange = { newName ->
                                    items[index] = item.copy(name = newName)
                                },
                                onCostChange = { newCents ->
                                    items[index] = item.copy(costCents = newCents)
                                },
                                onTypeSelect = { selectedType ->
                                    haptics.tick()
                                    items[index] = item.copy(type = selectedType)
                                },
                                onDelete = {
                                    haptics.tick()
                                    items.removeAt(index)
                                }
                            )
                        }
                    }
                }

                // Sticky Bottom Summary & Seal Action
                Surface(
                    color = palette.surfaceCard,
                    shadowElevation = 8.dp,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "$sortedCount of ${items.size} sorted",
                                    style = AppType.caption,
                                    color = palette.textMuted
                                )
                                Text(
                                    text = totalCents.toMoney(currencySymbol),
                                    style = AppType.moneyMd,
                                    color = palette.textPrimary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$needsCount Needs · ${needsTotal.toMoney(currencySymbol)}",
                                    style = AppType.caption,
                                    color = palette.marketGreen
                                )
                                Text(
                                    text = "$wantsCount Wants · ${wantsTotal.toMoney(currencySymbol)}",
                                    style = AppType.caption,
                                    color = palette.crimson
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        GiltButton(
                            onClick = {
                            onSealBatch(items, scanResult.dateUtc)
                        },
                            text = if (sortedCount < items.size) {
                                "Classify items above"
                            } else {
                                "Seal $sortedCount ${if (sortedCount == 1) "Item" else "Items"} to Sheet"
                            },
                            enabled = sortedCount == items.size && items.all { it.name.isNotBlank() && it.costCents > 0L },
                            modifier = Modifier.fillMaxWidth(),
                            height = 48.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScannedItemRow(
    item: ScannedLineItem,
    currencySymbol: String,
    onNameChange: (String) -> Unit,
    onCostChange: (Long) -> Unit,
    onTypeSelect: (EntryType) -> Unit,
    onDelete: () -> Unit
) {
    val palette = AppTheme.colors
    var rawAmount by remember(item.costCents) { mutableStateOf(item.costCents.toInputAmount()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(palette.surfaceCard)
            .border(
                BorderStroke(
                    1.dp,
                    when (item.type) {
                        EntryType.NEED -> palette.marketGreen.copy(alpha = 0.5f)
                        EntryType.WANT -> palette.crimson.copy(alpha = 0.5f)
                        null -> palette.divider.copy(alpha = 0.6f)
                    }
                ),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Editable item name
                BasicTextField(
                    value = item.name,
                    onValueChange = onNameChange,
                    textStyle = AppType.body.copy(
                        color = palette.textPrimary,
                        fontWeight = FontWeight.Medium
                    ),
                    cursorBrush = SolidColor(palette.gold),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(Modifier.width(8.dp))

                // Editable cost
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currencySymbol,
                        style = AppType.caption,
                        color = palette.textMuted
                    )
                    Spacer(Modifier.width(2.dp))
                    BasicTextField(
                        value = rawAmount,
                        onValueChange = { input ->
                            val filtered = filterAmountInput(input)
                            rawAmount = filtered
                            val cents = parseCents(filtered)
                            onCostChange(cents ?: 0L)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        textStyle = AppType.moneySm.copy(
                            color = palette.textPrimary,
                            textAlign = TextAlign.End
                        ),
                        cursorBrush = SolidColor(palette.gold),
                        singleLine = true,
                        modifier = Modifier.width(72.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete item",
                        tint = palette.textMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Need / Want classification buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val isNeed = item.type == EntryType.NEED
                val isWant = item.type == EntryType.WANT

                // NEED Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isNeed) palette.marketGreen else palette.marketGreen.copy(alpha = 0.08f))
                        .border(
                            BorderStroke(
                                1.dp,
                                if (isNeed) palette.marketGreen else palette.marketGreen.copy(alpha = 0.35f)
                            ),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onTypeSelect(EntryType.NEED) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NEED",
                        style = AppType.eyebrowSm.copy(fontWeight = FontWeight.Bold),
                        color = if (isNeed) Color.White else palette.marketGreen
                    )
                }

                // WANT Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isWant) palette.crimson else palette.crimson.copy(alpha = 0.08f))
                        .border(
                            BorderStroke(
                                1.dp,
                                if (isWant) palette.crimson else palette.crimson.copy(alpha = 0.35f)
                            ),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onTypeSelect(EntryType.WANT) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "WANT",
                        style = AppType.eyebrowSm.copy(fontWeight = FontWeight.Bold),
                        color = if (isWant) Color.White else palette.crimson
                    )
                }
            }
        }
    }
}
