package com.needsvswants.app.ui.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.Period
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.navigation.verticalScrollFirst
import com.needsvswants.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Share
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.needsvswants.app.domain.ImportUseCase
import com.needsvswants.app.domain.filterAmountInput
import com.needsvswants.app.domain.parseCents
import com.needsvswants.app.domain.toInputAmount
import android.content.Intent
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    onNavigateToInput: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val entries by viewModel.entries.collectAsStateWithLifecycle()
    val dayGroups by viewModel.dayGroups.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val typeFilter by viewModel.typeFilter.collectAsStateWithLifecycle()
    val periodFilter by viewModel.periodFilter.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val symbol by viewModel.currencySymbol.collectAsStateWithLifecycle()
    var editTarget by remember { mutableStateOf<Entry?>(null) }
    var actionTarget by remember { mutableStateOf<Entry?>(null) }
    // Row expanded into its entry slip (D195); one open slip at a time.
    var expandedEntryId by remember { mutableStateOf<Long?>(null) }
    var importPending by remember { mutableStateOf<ImportUseCase.Result?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val haptics = rememberAppHaptics()
    val context = LocalContext.current

    // Shared delete + undo (dialog confirm and perforated tear both land here).
    val deleteWithUndo: (Entry) -> Unit = { target ->
        actionTarget = null
        expandedEntryId = null
        haptics.warn()
        viewModel.deleteEntry(target)
        scope.launch {
            snackbarHostState.showSnackbar(
                message = "Deleted \"${target.item}\"",
                actionLabel = "Undo",
                duration = SnackbarDuration.Long
            ).let { result ->
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.restoreEntry(target)
                }
            }
        }
    }

    // Opens the system document picker (SAF) for a CSV file to import.
    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val text = runCatching {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                stream.bufferedReader().readText()
            }
        }.getOrNull()
        if (text == null) {
            scope.launch { snackbarHostState.showSnackbar("Couldn't read that file.") }
        } else {
            val result = ImportUseCase.parseCsv(text)
            if (result.entries.isEmpty()) {
                scope.launch { snackbarHostState.showSnackbar("No valid entries found in that CSV.") }
            } else {
                importPending = result
            }
        }
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().background(themedInkWash()).padding(horizontal = 20.dp).padding(top = 20.dp, bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("LEDGER", style = AppType.screenTitle, color = AppTheme.colors.textPrimary)
            }
            Row(
                modifier = Modifier.align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                            Icons.Outlined.Share,
                            contentDescription = null,
                            tint = AppTheme.colors.crimson,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                HeaderIconWell(
                    onClick = { importLauncher.launch(arrayOf("text/csv", "text/plain", "application/octet-stream")) },
                    contentDescription = "Import CSV"
                ) {
                    Icon(
                        Icons.Outlined.FileUpload,
                        contentDescription = null,
                        tint = AppTheme.colors.crimson,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        if (entries.isNotEmpty()) {
            val oldest = entries.lastOrNull()?.date
            val newest = entries.firstOrNull()?.date
            if (oldest != null && newest != null) {
                val oldestDate = dateFormat.parse(oldest)
                val newestDate = dateFormat.parse(newest)
                if (oldestDate != null && newestDate != null) {
                    Text(
                        "${displayFormat.format(oldestDate)}–${displayFormat.format(newestDate)}",
                        style = AppType.caption,
                        color = AppTheme.colors.textMuted
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (!loading && entries.isNotEmpty()) {
            LedgerField(
                value = searchQuery,
                onValueChange = viewModel::setSearchQuery,
                label = "Search item or date"
            )
            Spacer(Modifier.height(12.dp))
            // Row labels: two chip groups that both start with "All" otherwise
            // read as one bank with a duplicate (D190).
            Eyebrow("TYPE", color = AppTheme.colors.textMuted, size = 11)
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SelectChip(
                    label = "All",
                    selected = typeFilter == null,
                    color = AppTheme.colors.gilt,
                    onClick = { viewModel.setTypeFilter(null) },
                    modifier = Modifier.weight(1f)
                )
                SelectChip(
                    label = "Need",
                    selected = typeFilter == EntryType.NEED,
                    color = AppTheme.colors.need,
                    onClick = { viewModel.setTypeFilter(EntryType.NEED) },
                    modifier = Modifier.weight(1f)
                )
                SelectChip(
                    label = "Want",
                    selected = typeFilter == EntryType.WANT,
                    color = AppTheme.colors.want,
                    onClick = { viewModel.setTypeFilter(EntryType.WANT) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(12.dp))
            Eyebrow("PERIOD", color = AppTheme.colors.textMuted, size = 11)
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SelectChip(
                    label = "All",
                    selected = periodFilter == Period.ALL,
                    color = AppTheme.colors.gilt,
                    onClick = { viewModel.setPeriodFilter(Period.ALL) },
                    modifier = Modifier.weight(1f)
                )
                SelectChip(
                    label = "Day",
                    selected = periodFilter == Period.DAY,
                    color = AppTheme.colors.gilt,
                    onClick = { viewModel.setPeriodFilter(Period.DAY) },
                    modifier = Modifier.weight(1f)
                )
                SelectChip(
                    label = "Week",
                    selected = periodFilter == Period.WEEK,
                    color = AppTheme.colors.gilt,
                    onClick = { viewModel.setPeriodFilter(Period.WEEK) },
                    modifier = Modifier.weight(1f)
                )
                SelectChip(
                    label = "Month",
                    selected = periodFilter == Period.MONTH,
                    color = AppTheme.colors.gilt,
                    onClick = { viewModel.setPeriodFilter(Period.MONTH) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(14.dp))
        }

        if (loading) {
            // Cold first render — shimmer groups matching day-card height (zero CLS).
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repeat(3) {
                    HistorySkeletonGroup()
                }
            }
        } else if (entries.isEmpty() && dayGroups.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    EmptyDiaryIllustration(modifier = Modifier.size(160.dp, 120.dp))
                    Spacer(Modifier.height(20.dp))
                    Eyebrow("EMPTY DIARY", color = AppTheme.colors.textMuted)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "The page waits for ink.",
                        style = AppType.bodyMd,
                        color = AppTheme.colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    GiltButton(
                        onClick = onNavigateToInput,
                        text = "Log your first purchase",
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        } else if (dayGroups.isEmpty()) {
            // Entries exist but the search/filter matched nothing.
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Eyebrow("NO MATCHES", color = AppTheme.colors.textMuted)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Nothing in the ledger matches that search or filter.",
                        style = AppType.bodyMd,
                        color = AppTheme.colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    TextButton(onClick = {
                        haptics.tick()
                        viewModel.setSearchQuery("")
                        viewModel.setTypeFilter(null)
                        viewModel.setPeriodFilter(Period.ALL)
                    }) {
                        Text("Clear filters", color = AppTheme.colors.crimson)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .verticalScrollFirst(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(dayGroups, key = { it.date }) { day ->
                        val date = day.date
                        val dayEntries = day.entries
                        val displayDate = dateFormat.parse(date)?.let { displayFormat.format(it) } ?: date
                        val dayNeeds = dayEntries.filter { it.type == EntryType.NEED }.sumOf { it.costCents }
                        val dayWants = dayEntries.filter { it.type == EntryType.WANT }.sumOf { it.costCents }

                        val dayTotal = dayNeeds + dayWants
                        val entryLabel = when (dayEntries.size) {
                            0 -> "Budget day"
                            1 -> "1 entry"
                            else -> "${dayEntries.size} entries"
                        }
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
                                        Eyebrow(displayDate, color = AppTheme.colors.crimson, size = 11)
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
                                    Column(horizontalAlignment = Alignment.End) {
                                        day.budgetCents?.let { budgetCents ->
                                            Text(
                                                "BUDGET ${budgetCents.toMoney(symbol)}",
                                                style = AppType.eyebrowSm,
                                                color = AppTheme.colors.gilt,
                                                textAlign = TextAlign.End
                                            )
                                            Spacer(Modifier.height(5.dp))
                                        }
                                        Text(
                                            entryLabel.uppercase(),
                                            style = AppType.eyebrowSm,
                                            color = AppTheme.colors.textMuted,
                                            modifier = Modifier
                                                .border(
                                                    BorderStroke(1.dp, AppTheme.colors.dividerStrong),
                                                    AppShapes.r20
                                                )
                                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                                if (dayEntries.isEmpty()) {
                                    Spacer(Modifier.height(12.dp))
                                    Text(
                                        "No purchases logged for this budget day.",
                                        style = AppType.bodySm,
                                        color = AppTheme.colors.textSecondary
                                    )
                                } else {
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
                                        ExpandableTearEntry(
                                            entry = entry,
                                            symbol = symbol,
                                            expanded = expandedEntryId == entry.id,
                                            onToggle = {
                                                expandedEntryId =
                                                    if (expandedEntryId == entry.id) null else entry.id
                                            },
                                            onDelete = { actionTarget = entry },
                                            onDeleteConfirmed = { deleteWithUndo(entry) }
                                        )
                                    }
                                }
                            }
                        }
                }
            }
        }

        actionTarget?.let { entry ->
            EntryActionDialog(
                entry = entry,
                symbol = symbol,
                onDismiss = { actionTarget = null },
                onEdit = {
                    actionTarget = null
                    editTarget = entry
                },
                onDelete = { deleteWithUndo(entry) }
            )
        }

        editTarget?.let { entry ->
            EditEntryDialog(
                entry = entry,
                symbol = symbol,
                onDismiss = { editTarget = null },
                onSave = { updated ->
                    haptics.success()
                    viewModel.updateEntry(updated)
                    editTarget = null
                }
            )
        }

        importPending?.let { result ->
            val skipped = result.skippedCount
            PremiumDialog(
                onDismissRequest = { importPending = null },
                eyebrow = "RESTORE",
                eyebrowColor = AppTheme.colors.need,
                title = "Import ${result.entries.size} ${if (result.entries.size == 1) "entry" else "entries"}?",
                body = if (skipped > 0) {
                    "$skipped ${if (skipped == 1) "row was" else "rows were"} skipped."
                } else {
                    "They'll land in your diary as new lines."
                },
                confirmLabel = "Import",
                onConfirm = {
                    haptics.success()
                    viewModel.importEntries(result.entries)
                    importPending = null
                    scope.launch {
                        snackbarHostState.showSnackbar("Imported ${result.entries.size} ${if (result.entries.size == 1) "entry" else "entries"}.")
                    }
                },
                dismissLabel = "Cancel"
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        ) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
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
            .background(palette.surfaceSunken, AppShapes.r12)
            .border(BorderStroke(1.dp, color.copy(alpha = 0.28f)), AppShapes.r12)
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
/**
 * Branded action sheet for a sealed entry: Edit (primary) / Delete (danger) / Cancel.
 * Delete-undo happens in the caller via an Undo snackbar, so no confirm dialog is needed.
 */
@Composable
private fun EntryActionDialog(
    entry: Entry,
    symbol: String,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val c = AppTheme.colors
    val typeColor = if (entry.type == EntryType.NEED) c.need else c.want
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = AppShapes.r20,
            color = Color.Transparent,
            modifier = Modifier.paperSurface(
                rememberPaperSpec(PaperKind.RAISED, goldEdge = true),
                AppShapes.r20
            )
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Eyebrow(entry.type.name, color = typeColor)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = entry.item,
                    style = AppType.dialogTitle,
                    color = c.textPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${entry.time} · ${entry.costCents.toMoney(symbol)}",
                    style = AppType.body,
                    color = c.textSecondary
                )
                Spacer(Modifier.height(8.dp))
                GiltRule(width = 32.dp)
                Spacer(Modifier.height(18.dp))

                GiltButton(
                    onClick = onEdit,
                    text = "Edit entry",
                    height = 48.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onDelete,
                    shape = AppShapes.r12,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = c.danger.copy(alpha = 0.14f),
                        contentColor = c.danger
                    ),
                    border = BorderStroke(1.dp, c.danger.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete entry", fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Cancel", color = c.textMuted)
                }
            }
        }
    }
}
/**
 * In-place edit of a sealed entry. Reuses the ledger-field + type-chip language from Log
 * and the same amount parsing (filterAmountInput / parseCents / toInputAmount).
 */
@Composable
private fun EditEntryDialog(
    entry: Entry,
    symbol: String,
    onDismiss: () -> Unit,
    onSave: (Entry) -> Unit
) {
    val c = AppTheme.colors
    var item by remember { mutableStateOf(entry.item) }
    var amount by remember { mutableStateOf(entry.costCents.toInputAmount()) }
    var type by remember { mutableStateOf(entry.type) }
    var error by remember { mutableStateOf(false) }

    fun trySave() {
        val trimmed = item.trim()
        val cents = parseCents(amount)
        if (trimmed.isEmpty() || cents == null || cents < 0) {
            error = true
            return
        }
        onSave(
            entry.copy(
                item = trimmed,
                costCents = cents,
                type = type
            )
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = AppShapes.r20,
            color = Color.Transparent,
            modifier = Modifier.paperSurface(
                rememberPaperSpec(PaperKind.RAISED, goldEdge = true),
                AppShapes.r20
            )
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Eyebrow("EDIT ENTRY", color = c.crimson)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = entry.item,
                    style = AppType.dialogTitle,
                    color = c.textPrimary
                )
                Spacer(Modifier.height(8.dp))
                GiltRule(width = 32.dp)
                Spacer(Modifier.height(16.dp))

                LedgerField(
                    value = item,
                    onValueChange = { item = it; error = false },
                    label = "Item",
                    isError = error && item.isBlank()
                )
                Spacer(Modifier.height(14.dp))
                LedgerField(
                    value = amount,
                    onValueChange = { amount = filterAmountInput(it); error = false },
                    label = "Amount ($symbol)",
                    isError = error && parseCents(amount) == null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SelectChip(
                        label = "Need",
                        selected = type == EntryType.NEED,
                        color = c.need,
                        onClick = { type = EntryType.NEED; error = false },
                        modifier = Modifier.weight(1f)
                    )
                    SelectChip(
                        label = "Want",
                        selected = type == EntryType.WANT,
                        color = c.want,
                        onClick = { type = EntryType.WANT; error = false },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (error) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Enter an item and a valid amount.",
                        style = AppType.caption,
                        color = c.danger
                    )
                }

                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel", color = c.textMuted)
                    }
                    GiltButton(
                        onClick = { trySave() },
                        text = "Save",
                        height = 46.dp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}


/** Fraction of row width the perforated tear must pass to commit the delete (D195). */
internal const val TEAR_RELEASE_FRACTION = 0.42f

/** Pure paper shear: tear progress (0..1) to rotation degrees of the tearing flap. */
internal fun tearShearDegrees(fraction: Float): Float = fraction * 4.5f

/**
 * Expandable ledger entry (D195). Tap unfolds the row into its entry slip while
 * the elastic [animateContentSize] spring pushes the adjacent ledger lines away.
 * A horizontal perforated tear — paper shear, granular texture ticks, flutter
 * drop — dismisses the entry straight into the delete + undo flow.
 */
@Composable
private fun ExpandableTearEntry(
    entry: Entry,
    symbol: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (Motion.enabled) {
                    Modifier.animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMediumLow,
                            visibilityThreshold = IntSize.VisibilityThreshold
                        )
                    )
                } else {
                    Modifier
                }
            )
    ) {
        TearDismissRow(
            entry = entry,
            symbol = symbol,
            onOpen = onToggle,
            onDelete = onDelete,
            onDeleteConfirmed = onDeleteConfirmed
        )
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(Motion.state()),
            exit = fadeOut(Motion.feedback())
        ) {
            EntrySlip(entry = entry, symbol = symbol)
        }
    }
}

/**
 * Perforated tear-to-dismiss wrapper around [EntryLedgerRow]. Dragging right
 * tears the row off its ledger line: the desk shows through, a dotted
 * perforation rides the tear front, texture ticks fire along the path, and
 * past the release threshold the flap shears off with a flutter and the
 * entry is deleted (undo via snackbar). Under the threshold it springs back.
 */
@Composable
private fun TearDismissRow(
    entry: Entry,
    symbol: String,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    val palette = AppTheme.colors
    val haptics = rememberAppHaptics()
    val scope = rememberCoroutineScope()
    var rowWidth by remember { mutableIntStateOf(0) }
    var tearPx by remember { mutableFloatStateOf(0f) }
    var lastTickPx by remember { mutableFloatStateOf(0f) }
    var committing by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { rowWidth = it.width }
            .pointerInput(entry.id, rowWidth) {
                if (rowWidth <= 0) return@pointerInput
                detectHorizontalDragGestures(
                    onDragStart = {
                        lastTickPx = tearPx
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        if (committing) return@detectHorizontalDragGestures
                        change.consume()
                        val next = (tearPx + dragAmount)
                            .coerceIn(0f, rowWidth * 0.75f)
                        if (kotlin.math.abs(next - lastTickPx) >= 26f) {
                            lastTickPx = next
                            haptics.textureTick(0.42f)
                        }
                        tearPx = next
                    },
                    onDragEnd = {
                        val releasePx = rowWidth * TEAR_RELEASE_FRACTION
                        if (tearPx >= releasePx) {
                            committing = true
                            haptics.textureTick(0.9f)
                            scope.launch {
                                val flight = Animatable(tearPx)
                                flight.animateTo(rowWidth + 80f, Motion.receiptPrint())
                                tearPx = flight.value
                                onDeleteConfirmed()
                                committing = false
                            }
                        } else {
                            scope.launch {
                                val settle = Animatable(tearPx)
                                settle.animateTo(0f, Motion.spatialSpring())
                                tearPx = settle.value
                            }
                        }
                    },
                    onDragCancel = {
                        scope.launch {
                            val settle = Animatable(tearPx)
                            settle.animateTo(0f, Motion.spatialSpring())
                            tearPx = settle.value
                        }
                    }
                )
            }
    ) {
        // Desk under-layer: revealed as the flap tears away, perforation dots
        // riding the tear front.
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(palette.surfaceSunken, AppShapes.r12)
                .drawBehind {
                    val x = tearPx - 6.dp.toPx()
                    if (x > 2f) {
                        val r = 1.6f.dp.toPx()
                        var y = 4f
                        while (y < size.height) {
                            drawCircle(palette.surfaceCard.copy(alpha = 0.85f), radius = r, center = Offset(x, y))
                            y += 9f.dp.toPx()
                        }
                    }
                }
        )
        EntryLedgerRow(
            entry = entry,
            symbol = symbol,
            onDelete = onDelete,
            onClick = onOpen,
            modifier = Modifier.graphicsLayer {
                translationX = tearPx
                rotationZ = tearShearDegrees((tearPx / rowWidth.coerceAtLeast(1)).coerceIn(0f, 1f))
                transformOrigin = TransformOrigin(1f, 0.5f)
                if (tearPx > 1f) {
                    shadowElevation = 8.dp.toPx()
                    shape = AppShapes.r12
                }
            }
        )
    }
}

/** Expanded receipt-slip detail for one entry (D195). */
@Composable
private fun EntrySlip(entry: Entry, symbol: String) {
    val palette = AppTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .paperSurface(rememberPaperSpec(PaperKind.RECEIPT, goldEdge = true), AppShapes.r12)
            .padding(horizontal = scaledSpacing(16f), vertical = scaledSpacing(12f))
    ) {
        Eyebrow("ENTRY SLIP", color = palette.gilt)
        Spacer(Modifier.height(6.dp))
        Text(
            text = entry.item,
            style = AppType.titleSm,
            color = palette.textPrimary
        )
        Spacer(Modifier.height(8.dp))
        SlipLine("DATE", entry.date)
        SlipLine("TIME", entry.time)
        SlipLine(
            "TYPE",
            if (entry.type == EntryType.NEED) "NEED" else "WANT",
            valueColor = if (entry.type == EntryType.NEED) palette.need else palette.want
        )
        SlipLine("COST", entry.costCents.toMoney(symbol))
    }
}

@Composable
private fun SlipLine(label: String, value: String, valueColor: Color = AppTheme.colors.textSecondary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = label,
            style = AppType.eyebrowSm,
            color = AppTheme.colors.textMuted,
            modifier = Modifier.width(72.dp)
        )
        Text(
            text = value,
            style = AppType.bodySm,
            color = valueColor
        )
    }
}

