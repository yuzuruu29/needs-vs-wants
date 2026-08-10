package com.needsvswants.app.ui.screens.input

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.DailyBudgetMath
import com.needsvswants.app.domain.toInputAmount
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.AppType
import com.needsvswants.app.ui.theme.Eyebrow
import com.needsvswants.app.ui.theme.EntryLedgerHeader
import com.needsvswants.app.ui.theme.EntryLedgerRow
import com.needsvswants.app.ui.theme.GhostTextAction
import com.needsvswants.app.ui.theme.GiltButton
import com.needsvswants.app.ui.theme.GiltRule
import com.needsvswants.app.ui.theme.LedgerField
import com.needsvswants.app.ui.theme.Motion
import com.needsvswants.app.ui.theme.PremiumDialog
import com.needsvswants.app.ui.theme.PremiumSurface
import com.needsvswants.app.ui.theme.SealStampOverlay
import com.needsvswants.app.ui.theme.DailyBudgetMeter
import com.needsvswants.app.ui.theme.TierTag
import com.needsvswants.app.ui.navigation.verticalScrollFirst
import com.needsvswants.app.ui.theme.rememberAppHaptics
import com.needsvswants.app.ui.theme.rememberAppSfx
import com.needsvswants.app.ui.theme.themedInkWash
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputScreen(
    viewModel: InputViewModel = hiltViewModel(),
    onOpenPaywall: () -> Unit = {}
) {
    val entries by viewModel.sheetEntries.collectAsStateWithLifecycle()
    val symbol by viewModel.currencySymbol.collectAsStateWithLifecycle()
    val item by viewModel.activeItem.collectAsStateWithLifecycle()
    val cost by viewModel.activeCost.collectAsStateWithLifecycle()
    val type by viewModel.activeType.collectAsStateWithLifecycle()
    val pendingOverspendCost by viewModel.overspendConfirmCostCents.collectAsStateWithLifecycle()
    val quotaBlocked by viewModel.quotaBlocked.collectAsStateWithLifecycle()
    val budgetStatus by viewModel.budgetStatus.collectAsStateWithLifecycle()
    val dailyBudgetCents by viewModel.dailyBudgetCents.collectAsStateWithLifecycle()
    val budgetNudgePending by viewModel.budgetNudgePending.collectAsStateWithLifecycle()
    val entitlement by viewModel.entitlement.collectAsStateWithLifecycle()
    val isFull = viewModel.isSheetFull
    val today = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
    val filled = entries.size
    val now = System.currentTimeMillis()
    // Pro/Max: sheets are unlimited — the counter shows just the count, no "/ 20".
    val hasProAccess = entitlement.hasProAccessAt(now)
    var deleteTarget by remember { mutableStateOf<Entry?>(null) }
    var budgetAmount by remember { mutableStateOf("") }
    var budgetError by remember { mutableStateOf(false) }
    // Off by default so the seal form + list get the screen; open only when user asks.
    var editingBudget by remember { mutableStateOf(false) }
    var showNewSheetConfirm by remember { mutableStateOf(false) }
    var showSealStamp by remember { mutableStateOf(false) }
    val haptics = rememberAppHaptics()
    val sfx = rememberAppSfx()
    val listState = rememberLazyListState()
    val palette = AppTheme.colors

    LaunchedEffect(dailyBudgetCents) {
        val cents = dailyBudgetCents
        if (cents == null) {
            editingBudget = false
            if (budgetAmount.isBlank()) budgetAmount = ""
        } else {
            if (budgetAmount.isBlank()) {
                budgetAmount = cents.toInputAmount()
            }
            editingBudget = false
        }
    }

    // Onboarding nudge: the user picked "Stay under a daily budget" — pre-open the
    // set-budget form once (no invented amount; they type their own), then consume.
    LaunchedEffect(budgetNudgePending, budgetStatus) {
        if (budgetNudgePending && budgetStatus !is BudgetStatus.On) {
            editingBudget = true
            viewModel.consumeBudgetNudge()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sealEvents.collect { event ->
            if (event is SealEvent.Sealed) {
                haptics.seal()
                // Newest entry is at the top of the list.
                listState.animateScrollToItem(0)
                if (event.sheetComplete) {
                    haptics.success()
                    showSealStamp = true
                    delay(Motion.SealHoldMs.toLong())
                    showSealStamp = false
                }
            }
        }
    }

    if (!isFull) {
        LaunchedEffect(item, cost, type) {
            if (item.isNotBlank() && cost.isNotBlank() && cost.any { it.isDigit() } && type != null) {
                viewModel.trySeal()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themedInkWash())
            .imePadding()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .verticalScrollFirst(),
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // ── Title ────────────────────────────────────────────────────
            item(key = "log-title") {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Eyebrow("TODAY  ·  $today", color = palette.crimson)
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "LOG",
                            style = AppType.screenTitle,
                            color = palette.textPrimary
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Eyebrow("SHEET", color = palette.textMuted, size = 10)
                            Spacer(Modifier.height(2.dp))
                            Text(
                                if (hasProAccess) "$filled · unlimited" else "$filled / 20",
                                color = if (!hasProAccess && filled >= 18) palette.danger else palette.textPrimary,
                                style = AppType.moneyMd
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    GiltRule(width = 40.dp)
                }
                if (hasProAccess) {
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TierTag(
                            text = if (entitlement.hasMaxAccessAt(now)) "MAX" else "PRO",
                            color = if (entitlement.hasMaxAccessAt(now)) palette.crimson else palette.gold
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Unlimited sheet",
                            style = AppType.caption,
                            color = palette.textMuted
                        )
                    }
                }
            }

            // ── Daily budget (compact when off) ──────────────────────────
            item(key = "log-budget") {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
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
                            editingBudget = false
                        },
                        onStartEdit = {
                            dailyBudgetCents?.let { budgetAmount = it.toInputAmount() }
                            editingBudget = true
                        },
                        onCancelEdit = {
                            budgetError = false
                            editingBudget = false
                            dailyBudgetCents?.let { budgetAmount = it.toInputAmount() }
                        },
                        onRequestSetBudget = { editingBudget = true }
                    )
                }
            }

            // ── Seal form (sticky) ───────────────────────────────────────
            stickyHeader(key = "log-seal-form") {
                Surface(
                    color = palette.background.copy(alpha = 0.97f),
                    shadowElevation = 2.dp,
                    tonalElevation = 0.dp
                ) {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                        AnimatedContent(
                            targetState = isFull,
                            transitionSpec = {
                                fadeIn(Motion.state()) togetherWith fadeOut(Motion.state())
                            },
                            label = "logFormOrComplete"
                        ) { sheetFull ->
                            if (!sheetFull) {
                                PremiumSurface(raised = false) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        LedgerField(
                                            value = item,
                                            onValueChange = {
                                                viewModel.activeItem.value = viewModel.filterItem(it)
                                            },
                                            label = "Item",
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(Modifier.height(12.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            LedgerField(
                                                value = cost,
                                                onValueChange = {
                                                    viewModel.activeCost.value = viewModel.filterCost(it)
                                                },
                                                label = "Cost",
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Decimal
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                            Spacer(Modifier.width(10.dp))
                                            TypeChip("NEED", type == EntryType.NEED, palette.need) {
                                                if (type != EntryType.NEED) {
                                                    haptics.tick()
                                                    sfx.tap()
                                                }
                                                viewModel.activeType.value = EntryType.NEED
                                                viewModel.trySeal()
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            TypeChip("WANT", type == EntryType.WANT, palette.want) {
                                                if (type != EntryType.WANT) {
                                                    haptics.tick()
                                                    sfx.tap()
                                                }
                                                viewModel.activeType.value = EntryType.WANT
                                                viewModel.trySeal()
                                            }
                                        }
                                    }
                                }
                            } else {
                                PremiumSurface(goldEdge = true) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Eyebrow("SHEET COMPLETE", color = palette.marketGreen)
                                        Spacer(Modifier.height(8.dp))
                                        Text(
                                            "20 / 20 entries sealed",
                                            style = AppType.titleMd,
                                            color = palette.textPrimary
                                        )
                                        Spacer(Modifier.height(12.dp))
                                        GiltButton(
                                            onClick = { showNewSheetConfirm = true },
                                            text = "Start new sheet",
                                            height = 48.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ── Sealed list header ───────────────────────────────────────
            if (entries.isNotEmpty()) {
                item(key = "log-list-header") {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Eyebrow("SEALED TODAY", color = palette.gilt, size = 10)
                            Text(
                                "$filled sealed",
                                style = AppType.caption,
                                color = palette.textMuted
                            )
                        }
                        EntryLedgerHeader(
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp)
                        )
                        HorizontalDivider(
                            color = palette.divider,
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(Modifier.height(6.dp))
                    }
                }

                items(
                    items = entries.reversed(),
                    key = { it.id }
                ) { entry ->
                    EntryLedgerRow(
                        entry = entry,
                        symbol = symbol,
                        onDelete = { deleteTarget = entry },
                        showCard = true,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                            .animateItem(
                                fadeInSpec = Motion.entrance(),
                                placementSpec = Motion.state<IntOffset>(),
                                fadeOutSpec = Motion.feedback()
                            )
                    )
                }
            } else {
                item(key = "log-empty") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Eyebrow("EMPTY SHEET", color = palette.textMuted)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Type an item, cost, then NEED or WANT.\nThe row seals itself.",
                            style = AppType.bodyMd,
                            color = palette.textSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        pendingOverspendCost?.let { pendingCost ->
            val on = budgetStatus as? BudgetStatus.On
            if (on != null) {
                val overBy = DailyBudgetMath.overBy(on.spentCents, on.budgetCents, pendingCost)
                PremiumDialog(
                    onDismissRequest = { viewModel.dismissOverspendConfirm() },
                    eyebrow = "DAILY BUDGET",
                    eyebrowColor = palette.danger,
                    title = "Over budget?",
                    confirmLabel = "Log anyway",
                    onConfirm = { viewModel.confirmOverspendSeal() },
                    dismissLabel = "Cancel",
                    confirmDanger = true,
                    bodyContent = {
                        Column {
                            Text(
                                "\"$item\" · ${pendingCost.toMoney(symbol)}",
                                color = palette.textPrimary,
                                style = AppType.titleMd
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "This puts you over by ${overBy.toMoney(symbol)}. Your daily budget is ${on.budgetCents.toMoney(symbol)}. Log anyway?",
                                color = palette.textSecondary,
                                style = AppType.body
                            )
                        }
                    }
                )
            }
        }

        quotaBlocked?.let {
            PremiumDialog(
                onDismissRequest = { viewModel.dismissQuotaBlocked() },
                eyebrow = "DAILY QUOTA",
                eyebrowColor = palette.gilt,
                title = "You've used your free logs for today.",
                bodyContent = {
                    Column {
                        Text(
                            "Come back tomorrow. Unused Free logs carry to the next day while your logging streak stays active.",
                            color = palette.textSecondary,
                            style = AppType.body
                        )
                    }
                },
                confirmLabel = "Go Pro/Max",
                onConfirm = {
                    viewModel.dismissQuotaBlocked()
                    onOpenPaywall()
                },
                dismissLabel = "Come back tomorrow"
            )
        }

        deleteTarget?.let { entry ->
            PremiumDialog(
                onDismissRequest = { deleteTarget = null },
                eyebrow = "CONFIRM",
                eyebrowColor = palette.danger,
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

        if (showNewSheetConfirm) {
            PremiumDialog(
                onDismissRequest = { showNewSheetConfirm = false },
                eyebrow = "NEW SHEET",
                title = "Start a new sheet?",
                body = "This clears all 20 entries on today's log sheet so you can start fresh.",
                confirmLabel = "Start new sheet",
                onConfirm = {
                    viewModel.startNewSheet()
                    haptics.success()
                    showNewSheetConfirm = false
                },
                dismissLabel = "Cancel"
            )
        }

        SealStampOverlay(
            visible = showSealStamp,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun TypeChip(label: String, selected: Boolean, color: Color, onClick: () -> Unit) {
    val bgColor by animateColorAsState(
        targetValue = if (selected) color.copy(alpha = 0.16f) else AppTheme.colors.surfaceSunken,
        animationSpec = Motion.state(),
        label = "chipBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) color else AppTheme.colors.dividerStrong,
        animationSpec = Motion.state(),
        label = "chipBorder"
    )
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.03f else 1f,
        animationSpec = if (selected) Motion.selectionSpring() else Motion.seal(),
        label = "chipScale"
    )
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(if (selected) 1.5.dp else 1.dp, borderColor),
        modifier = Modifier
            .heightIn(min = 48.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                label,
                color = if (selected) color else AppTheme.colors.textSecondary,
                style = AppType.button.copy(
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                )
            )
        }
    }
}

/**
 * Daily budget on Log: compact when off; meter when on; full form only while editing.
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
    onCancelEdit: () -> Unit,
    onRequestSetBudget: () -> Unit
) {
    val budgetOn = budgetStatus is BudgetStatus.On
    val onStatus = budgetStatus as? BudgetStatus.On
    var lastOnStatus by remember { mutableStateOf<BudgetStatus.On?>(null) }
    LaunchedEffect(onStatus) { if (onStatus != null) lastOnStatus = onStatus }
    val palette = AppTheme.colors

    // Meter + actions survive state flips: hoisted outside the `when` so the
    // enter/exit transitions can actually play — a `when` branch would dispose
    // the subtree the instant its condition flips false. Transform-only
    // (fade/scale) so siblings never relayout. The exiting content keeps the
    // last `On` status via [lastOnStatus] so the exit transition never casts
    // an `Off` state into [BudgetStatus.On].
    AnimatedVisibility(
        visible = onStatus != null && !editingBudget,
        enter = fadeIn(Motion.budget()) + scaleIn(initialScale = Motion.RiseScale, animationSpec = Motion.budget()),
        exit = fadeOut(Motion.feedback()) + scaleOut(targetScale = Motion.RiseScale, animationSpec = Motion.feedback())
    ) {
        val meterStatus = onStatus ?: lastOnStatus
        if (meterStatus != null) {
            Column {
                DailyBudgetMeter(status = meterStatus, symbol = symbol)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GhostTextAction(text = "Change", onClick = onStartEdit)
                    GhostTextAction(text = "Turn off", onClick = onClear, danger = true)
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }

    when {
        // Compact invite — do not eat half the screen when budget is off.
        !budgetOn && !editingBudget -> {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = palette.surfaceCard,
                border = BorderStroke(1.dp, palette.gold.copy(alpha = 0.28f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Eyebrow("DAILY BUDGET", color = palette.gilt, size = 10)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "Optional. Off until you set a limit.",
                            style = AppType.bodySm,
                            color = palette.textSecondary
                        )
                    }
                    GhostTextAction(text = "Set", onClick = onRequestSetBudget)
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        editingBudget -> {
            // Editing form (set or update).
            if (budgetOn) {
                DailyBudgetMeter(status = budgetStatus as BudgetStatus.On, symbol = symbol)
                Spacer(Modifier.height(8.dp))
            }
            PremiumSurface(raised = false) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Eyebrow(
                            if (budgetOn) "UPDATE LIMIT" else "DAILY BUDGET",
                            color = palette.gilt
                        )
                        GhostTextAction(text = "Cancel", onClick = onCancelEdit)
                    }
                    if (!budgetOn) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Optional. Off until you set an amount.",
                            style = AppType.bodySm,
                            color = palette.textSecondary
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    LedgerField(
                        value = budgetAmount,
                        onValueChange = onBudgetAmountChange,
                        label = "Amount",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = budgetError,
                        supportingText = if (budgetError) "Enter a valid amount" else null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))
                    GiltButton(
                        onClick = onSave,
                        text = if (budgetOn) "Update budget" else "Save budget",
                        height = 46.dp,
                        enabled = budgetAmount.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
