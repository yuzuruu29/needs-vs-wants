package com.needsvswants.app.ui.screens.input

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import com.needsvswants.app.ui.theme.AppShapes
import com.needsvswants.app.ui.theme.SelectChip
import com.needsvswants.app.ui.theme.pressRecoil
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.AdsConfig
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
import com.needsvswants.app.ui.theme.PaperKind
import com.needsvswants.app.ui.theme.paperSurface
import com.needsvswants.app.ui.theme.rememberPaperSpec
import com.needsvswants.app.ui.navigation.verticalScrollFirst
import com.needsvswants.app.ui.theme.rememberAppHaptics
import com.needsvswants.app.ui.theme.rememberAppSfx
import com.needsvswants.app.ui.theme.themedInkWash
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

private val STARTER_CHIPS = listOf(
    "Jeepney" to EntryType.NEED,
    "Lunch" to EntryType.NEED,
    "Coffee" to EntryType.WANT
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun InputScreen(
    viewModel: InputViewModel = hiltViewModel(),
    onOpenPaywall: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    val entries by viewModel.sheetEntries.collectAsStateWithLifecycle()
    val symbol by viewModel.currencySymbol.collectAsStateWithLifecycle()
    val item by viewModel.activeItem.collectAsStateWithLifecycle()
    val cost by viewModel.activeCost.collectAsStateWithLifecycle()
    val type by viewModel.activeType.collectAsStateWithLifecycle()
    val pendingOverspendCost by viewModel.overspendConfirmCostCents.collectAsStateWithLifecycle()
    val quotaBlocked by viewModel.quotaBlocked.collectAsStateWithLifecycle()
    val canWatchAdToday by viewModel.canWatchAdToday.collectAsStateWithLifecycle()
    val adState by viewModel.adState.collectAsStateWithLifecycle()
    val sealHour by viewModel.sealHourOverride.collectAsStateWithLifecycle()
    val lastItemChips by viewModel.lastItemChips.collectAsStateWithLifecycle()
    val backupNudgeVisible by viewModel.backupNudgeVisible.collectAsStateWithLifecycle()
    val budgetStatus by viewModel.budgetStatus.collectAsStateWithLifecycle()
    val dailyBudgetCents by viewModel.dailyBudgetCents.collectAsStateWithLifecycle()
    val currentDayKey by viewModel.currentDayKey.collectAsStateWithLifecycle()
    val budgetNudgePending by viewModel.budgetNudgePending.collectAsStateWithLifecycle()
    val entitlement by viewModel.entitlement.collectAsStateWithLifecycle()
    val coachHold by viewModel.coachHold.collectAsStateWithLifecycle()
    val receiptScanState by viewModel.receiptScanState.collectAsStateWithLifecycle()
    var showCoachDialog by remember { mutableStateOf(false) }
    var showProGateDialog by remember { mutableStateOf(false) }
    var showPhotoSourceDialog by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    val imageScope = rememberCoroutineScope()
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
    var sealStampLabel by remember { mutableStateOf("SEALED") }
    var sealStampCaption by remember { mutableStateOf<String?>(null) }
    var showBackupNudge by remember { mutableStateOf(false) }
    LaunchedEffect(backupNudgeVisible) {
        if (backupNudgeVisible) showBackupNudge = true
    }

    // A budget belongs to the current local day. Clear the form when the day
    // key rolls so yesterday's amount can never be mistaken for today's.
    LaunchedEffect(currentDayKey) {
        budgetAmount = ""
        budgetError = false
        editingBudget = false
    }
    val haptics = rememberAppHaptics()
    val sfx = rememberAppSfx()
    val listState = rememberLazyListState()
    val palette = AppTheme.colors
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        ReceiptImageLoader.clearCache(context.cacheDir)
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = pendingCameraUri
        pendingCameraUri = null
        if (success && uri != null) {
            imageScope.launch {
                // The capture is a temporary private-cache file; it must be
                // deleted on every path — decode failure or OCR crash included
                // (Data Safety: images never outlive processing).
                try {
                    val bitmap = runCatching {
                        withContext(Dispatchers.IO) {
                            ReceiptImageLoader.decode(context.contentResolver, uri)
                        }
                    }.getOrNull()
                    viewModel.scanReceipt(bitmap)
                } finally {
                    runCatching { context.contentResolver.delete(uri, null, null) }
                }
            }
        } else if (uri != null) {
            context.contentResolver.delete(uri, null, null)
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageScope.launch {
                val bitmap = runCatching {
                    withContext(Dispatchers.IO) {
                        ReceiptImageLoader.decode(context.contentResolver, uri)
                    }
                }.getOrNull()
                viewModel.scanReceipt(bitmap)
            }
        }
    }

    fun launchReceiptCamera() {
        val directory = File(context.cacheDir, "receipt-images").apply { mkdirs() }
        ReceiptImageLoader.clearCache(context.cacheDir)
        val file = File(directory, "receipt-${UUID.randomUUID()}.jpg")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        pendingCameraUri = uri
        takePictureLauncher.launch(uri)
    }

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
                // Deep thud + hum on the NEED/WANT seal hit (D195); the constant
                // fallback chain lives inside AppHaptics.
                haptics.sealThud()
                // Newest entry is at the top of the list.
                listState.animateScrollToItem(0)
                // Peak beats (D191): the sheet-complete stamp stays; the very
                // first seal and the first seal of each day land their own
                // restrained stamp so the streak moment happens where the
                // seal happens, not on a later Summary visit.
                var label: String? = null
                var caption: String? = null
                when {
                    event.sheetComplete -> {
                        haptics.success()
                        label = "SEALED"
                    }
                    event.firstEver -> {
                        haptics.success()
                        label = "DAY 1"
                        caption = "First entry sealed · the diary is live"
                    }
                    event.firstOfDay -> {
                        label = "DAY ${event.streakDay}"
                        caption = "First seal of the day · streak day ${event.streakDay}"
                    }
                }
                if (label != null) {
                    sealStampLabel = label
                    sealStampCaption = caption
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

    // A sealed or edited-away row clears the pending consult; drop any open dialog.
    LaunchedEffect(coachHold) {
        if (coachHold == null) showCoachDialog = false
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
                            Eyebrow("SHEET", color = palette.textMuted, size = 11)
                            Spacer(Modifier.height(2.dp))
                            Text(
                                if (hasProAccess) "$filled · unlimited" else "$filled / 20",
                                color = if (!hasProAccess && filled >= 18) palette.danger else palette.textPrimary,
                                style = AppType.moneyMd
                            )
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (hasProAccess) {
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
                        } else {
                            Spacer(Modifier.width(1.dp))
                        }

                        // Scan Receipt Button
                        val scanInteraction = remember { MutableInteractionSource() }
                        Row(
                            modifier = Modifier
                                .clip(AppShapes.r20)
                                .background(palette.surfaceCard)
                                .border(
                                    BorderStroke(1.dp, palette.gold.copy(alpha = if (hasProAccess) 0.8f else 0.45f)),
                                    AppShapes.r20
                                )
                                .heightIn(min = 44.dp)
                                .pressRecoil(scanInteraction)
                                .clickable(
                                    interactionSource = scanInteraction,
                                    indication = null,
                                    role = Role.Button
                                ) {
                                    haptics.tick()
                                    if (hasProAccess) {
                                        showPhotoSourceDialog = true
                                    } else {
                                        showProGateDialog = true
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = "Scan Receipt",
                                tint = palette.gold,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Scan Receipt",
                                style = AppType.caption.copy(fontWeight = FontWeight.SemiBold),
                                color = palette.textPrimary
                            )
                            TierTag(
                                text = "PRO",
                                color = palette.gold
                            )
                        }
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
                        // Container transform (D195): the quick-log input card and the
                        // sheet-complete card share one morphing paper surface.
                        SharedTransitionLayout {
                            AnimatedContent(
                                targetState = isFull,
                                transitionSpec = {
                                    fadeIn(Motion.state()) togetherWith fadeOut(Motion.state())
                                },
                                label = "logFormOrComplete"
                            ) { sheetFull ->
                                val morphModifier = Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "log-seal-card"),
                                    animatedVisibilityScope = this,
                                    enter = fadeIn(Motion.state()),
                                    exit = fadeOut(Motion.state())
                                )
                                if (!sheetFull) {
                                    PremiumSurface(modifier = morphModifier, raised = false) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        if (item.isBlank()) {
                                            val chipsToShow = if (lastItemChips.isNotEmpty()) lastItemChips else STARTER_CHIPS
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .horizontalScroll(rememberScrollState()),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                chipsToShow.forEach { (replayItem, replayType) ->
                                                    SelectChip(
                                                        label = replayItem,
                                                        selected = false,
                                                        color = if (replayType == EntryType.NEED) palette.need else palette.want,
                                                        compact = true,
                                                        onClick = {
                                                            haptics.tick()
                                                            sfx.tap()
                                                            viewModel.replayLastItem(replayItem, replayType)
                                                        }
                                                    )
                                                }
                                            }
                                            Spacer(Modifier.height(12.dp))
                                        }
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
                                            SelectChip("NEED", type == EntryType.NEED, palette.need) {
                                                if (type != EntryType.NEED) {
                                                    haptics.tick()
                                                    sfx.tap()
                                                }
                                                viewModel.activeType.value = EntryType.NEED
                                                viewModel.trySeal()
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            SelectChip("WANT", type == EntryType.WANT, palette.want) {
                                                if (type != EntryType.WANT) {
                                                    haptics.tick()
                                                    sfx.tap()
                                                }
                                                viewModel.activeType.value = EntryType.WANT
                                                viewModel.trySeal()
                                            }
                                        }
                                        // Seal as earlier today (top of the chosen hour; Now = real clock).
                                        Spacer(Modifier.height(10.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .horizontalScroll(rememberScrollState()),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            SelectChip(
                                                label = "Now",
                                                selected = sealHour == null,
                                                color = palette.gilt,
                                                compact = true,
                                                onClick = {
                                                    haptics.tick()
                                                    viewModel.setSealHour(null)
                                                }
                                            )
                                            val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                                            val startHour = if (currentHour < 6) 0 else 6
                                            for (hour in startHour..currentHour) {
                                                SelectChip(
                                                    label = String.format(Locale.US, "%02d:00", hour),
                                                    selected = sealHour == hour,
                                                    color = palette.gilt,
                                                    compact = true,
                                                    onClick = {
                                                        haptics.tick()
                                                        viewModel.setSealHour(hour)
                                                    }
                                                )
                                            }
                                        }
                                        // Max-only pre-seal Want coach (Task 3): quiet chip
                                        // while a draft Want consult is pending. Needs never
                                        // produce this state; Free/Pro see nothing.
                                        if (coachHold != null) {
                                            Spacer(Modifier.height(10.dp))
                                            CoachChip(
                                                label = "Ask Max before sealing",
                                                onClick = {
                                                    haptics.tick()
                                                    showCoachDialog = true
                                                }
                                            )
                                        }
                                    }
                                }
                            } else {
                                PremiumSurface(modifier = morphModifier, goldEdge = true) {
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
                            Eyebrow("SEALED TODAY", color = palette.gilt, size = 11)
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                            .paperSurface(
                                rememberPaperSpec(PaperKind.CHIP, goldEdge = true),
                                AppShapes.r16
                            )
                            .padding(horizontal = 18.dp, vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Eyebrow("SEAL YOUR FIRST ENTRY", color = palette.gilt, size = 11)
                            Spacer(Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("1. Name item", style = AppType.caption, color = palette.textPrimary)
                                Text("→", style = AppType.caption, color = palette.gilt)
                                Text("2. Enter cost", style = AppType.caption, color = palette.textPrimary)
                                Text("→", style = AppType.caption, color = palette.gilt)
                                Text("3. NEED or WANT", style = AppType.caption, color = palette.textPrimary)
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "The row seals immediately upon choosing type.",
                                style = AppType.bodyMd,
                                color = palette.textSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
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
                        if (AdsConfig.ENABLED && canWatchAdToday) {
                            Spacer(Modifier.height(14.dp))
                            when (val state = adState) {
                                is AdState.Failed -> {
                                    Text(
                                        state.message,
                                        color = palette.danger,
                                        style = AppType.caption
                                    )
                                    Spacer(Modifier.height(10.dp))
                                }
                                else -> Unit
                            }
                            val activity = context as? Activity
                            Text(
                                "Watch a short ad for ${AdsConfig.EXTRA_LOGS_PER_REWARD} extra logs today.",
                                color = palette.textMuted,
                                style = AppType.caption
                            )
                            Spacer(Modifier.height(10.dp))
                            GiltButton(
                                onClick = { activity?.let(viewModel::onWatchAd) },
                                enabled = adState !is AdState.Loading && activity != null,
                                text = if (adState is AdState.Loading) "Loading ad…" else "Watch ad",
                                height = 48.dp
                            )
                        }
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

        if (showBackupNudge) {
            PremiumDialog(
                onDismissRequest = {
                    showBackupNudge = false
                    viewModel.dismissBackupNudge()
                },
                eyebrow = "DIARY",
                eyebrowColor = palette.gilt,
                title = "Keep a copy?",
                body = "Your diary lives on this device. Set up a backup folder in Settings to keep an automatic daily copy.",
                confirmLabel = "Open Settings",
                onConfirm = {
                    showBackupNudge = false
                    viewModel.dismissBackupNudge()
                    onOpenSettings()
                },
                dismissLabel = "Not now"
            )
        }

        // Max-only pre-seal Want hold gate (Task 3): the soft gate dialog.
        // "Hold" only closes the dialog: the seal never fired, so the draft
        // stays. Keyed on the consult instance so an edited row replaces the
        // verdict instead of showing a stale one (reduced-motion safe: the
        // PremiumDialog chrome owns all animation via Motion tokens).
        coachHold?.let { hold ->
            if (showCoachDialog) {
                key(hold) {
                    PremiumDialog(
                        onDismissRequest = { showCoachDialog = false },
                        eyebrow = "MAX COACH",
                        eyebrowColor = palette.crimson,
                        title = "24h hold suggested",
                        bodyContent = {
                            Column {
                                Text(
                                    text = hold.reason,
                                    color = palette.textPrimary,
                                    style = AppType.body
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = hold.citation,
                                    color = palette.textMuted,
                                    style = AppType.caption
                                )
                            }
                        },
                        confirmLabel = "Seal anyway",
                        onConfirm = {
                            showCoachDialog = false
                            viewModel.confirmCoachSeal()
                        },
                        dismissLabel = "Hold"
                    )
                }
            }
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

        if (showProGateDialog) {
            ReceiptProGateDialog(
                onDismiss = { showProGateDialog = false },
                onOpenPaywall = onOpenPaywall
            )
        }

        if (showPhotoSourceDialog) {
            PremiumDialog(
                onDismissRequest = { showPhotoSourceDialog = false },
                eyebrow = "RECEIPT SCANNER",
                eyebrowColor = palette.gold,
                title = "Select Photo Source",
                bodyContent = {
                    Column {
                        Text(
                            "Take a photo of your paper receipt or choose an image from your device gallery.",
                            style = AppType.body,
                            color = palette.textSecondary
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val cameraInteraction = remember { MutableInteractionSource() }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .pressRecoil(cameraInteraction)
                                    .clip(AppShapes.r8)
                                    .background(palette.surfaceRaised)
                                    .border(BorderStroke(1.dp, palette.dividerStrong), AppShapes.r8)
                                    .clickable(
                                        interactionSource = cameraInteraction,
                                        indication = null,
                                        role = Role.Button
                                    ) {
                                        haptics.tick()
                                        showPhotoSourceDialog = false
                                        launchReceiptCamera()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.CameraAlt,
                                        contentDescription = null,
                                        tint = palette.textPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        "Camera",
                                        style = AppType.meta,
                                        color = palette.textPrimary
                                    )
                                }
                            }

                            val galleryInteraction = remember { MutableInteractionSource() }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .pressRecoil(galleryInteraction)
                                    .clip(AppShapes.r8)
                                    .background(palette.surfaceRaised)
                                    .border(BorderStroke(1.dp, palette.dividerStrong), AppShapes.r8)
                                    .clickable(
                                        interactionSource = galleryInteraction,
                                        indication = null,
                                        role = Role.Button
                                    ) {
                                        haptics.tick()
                                        showPhotoSourceDialog = false
                                        pickImageLauncher.launch("image/*")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.PhotoLibrary,
                                        contentDescription = null,
                                        tint = palette.textPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        "Gallery",
                                        style = AppType.meta,
                                        color = palette.textPrimary
                                    )
                                }
                            }
                        }
                    }
                },
                confirmLabel = "Cancel",
                onConfirm = { showPhotoSourceDialog = false },
                dismissLabel = "Close"
            )
        }

        when (val state = receiptScanState) {
            is ReceiptScanUiState.Scanning -> {
                PremiumDialog(
                    onDismissRequest = { viewModel.dismissReceiptScan() },
                    eyebrow = "ON-DEVICE OCR",
                    eyebrowColor = palette.gold,
                    title = "Scanning Receipt…",
                    bodyContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            CircularProgressIndicator(
                                color = palette.gold,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                "Extracting purchase items and amounts locally…",
                                style = AppType.body,
                                color = palette.textSecondary
                            )
                        }
                    },
                    confirmLabel = "Cancel",
                    onConfirm = { viewModel.dismissReceiptScan() },
                    dismissLabel = "Close"
                )
            }
            is ReceiptScanUiState.Ready -> {
                ReceiptSorterModal(
                    scanResult = state.result,
                    currencySymbol = symbol,
                    onDismiss = { viewModel.dismissReceiptScan() },
                    onSealBatch = { items, dateUtc ->
                        viewModel.sealScannedBatch(items, dateUtc)
                    }
                )
            }
            is ReceiptScanUiState.Error -> {
                PremiumDialog(
                    onDismissRequest = { viewModel.dismissReceiptScan() },
                    eyebrow = "RECEIPT SCANNER",
                    eyebrowColor = palette.danger,
                    title = "Scan Notice",
                    body = state.message,
                    confirmLabel = "OK",
                    onConfirm = { viewModel.dismissReceiptScan() },
                    dismissLabel = "Close"
                )
            }
            else -> Unit
        }

        SealStampOverlay(
            visible = showSealStamp,
            label = sealStampLabel,
            caption = sealStampCaption,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Quiet Max coach affordance for a pending Want consult (Task 3). Static
 * surface (reduced-motion safe by construction); tick haptic on tap.
 */
@Composable
private fun CoachChip(label: String, onClick: () -> Unit) {
    val palette = AppTheme.colors
    val interaction = remember { MutableInteractionSource() }
    Surface(
        onClick = onClick,
        interactionSource = interaction,
        shape = AppShapes.r20,
        color = palette.surfaceRaised,
        border = BorderStroke(1.dp, palette.gold.copy(alpha = 0.40f)),
        modifier = Modifier
            .heightIn(min = 44.dp)
            .pressRecoil(interaction)
    ) {
        Text(
            text = label,
            style = AppType.meta,
            color = palette.textPrimary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )
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
                shape = AppShapes.r14,
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
                        Eyebrow("DAILY BUDGET", color = palette.gilt, size = 11)
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
