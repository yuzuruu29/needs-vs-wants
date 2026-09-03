package com.needsvswants.app.ui.screens.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
import com.needsvswants.app.ui.theme.AppShapes
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.AppType
import com.needsvswants.app.ui.theme.Eyebrow
import com.needsvswants.app.ui.theme.GiltButton
import com.needsvswants.app.ui.theme.GiltRule
import com.needsvswants.app.ui.theme.Motion
import com.needsvswants.app.ui.theme.pressRecoil
import com.needsvswants.app.ui.theme.rememberAppHaptics
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlinx.coroutines.launch

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
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visible = true
        }
        AnimatedVisibility(
            visible = visible,
            enter = androidx.compose.animation.slideInVertically(
                initialOffsetY = { it / 6 },
                animationSpec = Motion.receiptPrint()
            ) + fadeIn(Motion.receiptPrint()),
            exit = fadeOut(Motion.feedback())
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
                            imageVector = Icons.Outlined.Close,
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
                            .clip(AppShapes.r16)
                            .background(palette.marketGreen.copy(alpha = 0.12f))
                            .border(BorderStroke(1.dp, palette.marketGreen.copy(alpha = 0.4f)), AppShapes.r16)
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
                            .clip(AppShapes.r16)
                            .background(palette.crimson.copy(alpha = 0.12f))
                            .border(BorderStroke(1.dp, palette.crimson.copy(alpha = 0.4f)), AppShapes.r16)
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
    val haptics = rememberAppHaptics()
    val scope = rememberCoroutineScope()
    var rawAmount by remember(item.costCents) { mutableStateOf(item.costCents.toInputAmount()) }
    // Elastic swipe-to-classify (D195): right stamps WANT, left stamps NEED,
    // with rotational torque and an ink watermark surfacing beneath the card.
    // Swipes starting on the text fields stay text selection by contract.
    var dragPx by remember(item.id) { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(item.id) {
                val settle = Animatable(0f)
                detectHorizontalDragGestures(
                    onDragStart = { scope.launch { settle.stop() } },
                    onHorizontalDrag = { change, amount ->
                        change.consume()
                        dragPx = (dragPx + amount).coerceIn(-180f, 180f)
                    },
                    onDragEnd = {
                        val threshold = 72.dp.toPx()
                        if (kotlin.math.abs(dragPx) >= threshold) {
                            onTypeSelect(if (dragPx > 0f) EntryType.WANT else EntryType.NEED)
                            haptics.textureTick(0.85f)
                        } else if (kotlin.math.abs(dragPx) > 8f) {
                            haptics.tick()
                        }
                        scope.launch {
                            settle.snapTo(dragPx)
                            // Trailing block publishes every frame; without it the card
                            // freezes at the drag offset and snaps back in one jump.
                            settle.animateTo(0f, Motion.spatialSpring()) { dragPx = value }
                        }
                    },
                    onDragCancel = {
                        scope.launch {
                            settle.snapTo(dragPx)
                            settle.animateTo(0f, Motion.spatialSpring()) { dragPx = value }
                        }
                    }
                )
            }
    ) {
        val strength = (kotlin.math.abs(dragPx) / 96f).coerceIn(0f, 1f)
        if (strength > 0.02f) {
            Text(
                text = if (dragPx > 0f) "WANT" else "NEED",
                style = AppType.stamp.copy(fontSize = 26.sp),
                color = (if (dragPx > 0f) palette.crimson else palette.marketGreen)
                    .copy(alpha = 0.25f + 0.45f * strength),
                modifier = Modifier
                    .align(if (dragPx > 0f) Alignment.CenterStart else Alignment.CenterEnd)
                    .graphicsLayer { rotationZ = if (dragPx > 0f) -8f else 8f }
                    .padding(horizontal = 14.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationX = dragPx
                    rotationZ = dragPx * 0.02f
                }
                .clip(AppShapes.r12)
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
                AppShapes.r12
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
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = "Delete item",
                        tint = palette.textMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Need / Want classification buttons with tactile recoil
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val isNeed = item.type == EntryType.NEED
                val isWant = item.type == EntryType.WANT
                val needInteraction = remember { MutableInteractionSource() }
                val wantInteraction = remember { MutableInteractionSource() }

                // NEED Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .pressRecoil(needInteraction)
                        .clip(AppShapes.r8)
                        .background(if (isNeed) palette.marketGreen else palette.marketGreen.copy(alpha = 0.08f))
                        .border(
                            BorderStroke(
                                1.dp,
                                if (isNeed) palette.marketGreen else palette.marketGreen.copy(alpha = 0.35f)
                            ),
                            AppShapes.r8
                        )
                        .clickable(interactionSource = needInteraction, indication = null) { onTypeSelect(EntryType.NEED) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NEED",
                        style = AppType.eyebrowSm.copy(fontWeight = FontWeight.Bold),
                        // Card-paper ink on the filled tint: white fell to ~2.9:1
                        // on the dark-theme tints (D191).
                        color = if (isNeed) palette.surfaceCard else palette.marketGreen
                    )
                }

                // WANT Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .pressRecoil(wantInteraction)
                        .clip(AppShapes.r8)
                        .background(if (isWant) palette.crimson else palette.crimson.copy(alpha = 0.08f))
                        .border(
                            BorderStroke(
                                1.dp,
                                if (isWant) palette.crimson else palette.crimson.copy(alpha = 0.35f)
                            ),
                            AppShapes.r8
                        )
                        .clickable(interactionSource = wantInteraction, indication = null) { onTypeSelect(EntryType.WANT) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "WANT",
                        style = AppType.eyebrowSm.copy(fontWeight = FontWeight.Bold),
                        color = if (isWant) palette.surfaceCard else palette.crimson
                    )
                }
            }
        }
        }
    }
}
