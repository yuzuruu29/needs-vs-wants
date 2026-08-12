package com.needsvswants.app.ui.screens.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CardMembership
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.BuildConfig
import com.needsvswants.app.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.needsvswants.app.domain.FontScaleStep
import com.needsvswants.app.domain.ThemeId
import com.needsvswants.app.ui.navigation.verticalScrollFirst
import com.needsvswants.app.ui.screens.auth.AuthViewModel
import com.needsvswants.app.ui.theme.*

@Composable
fun SettingsScreen(
    onOpenPaywall: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentCode by viewModel.currentCode.collectAsStateWithLifecycle()
    val themeId by viewModel.themeId.collectAsStateWithLifecycle()
    val fontScaleStep by viewModel.fontScaleStep.collectAsStateWithLifecycle()
    val symbol by viewModel.currentSymbol.collectAsStateWithLifecycle()
    val reminderEnabled by viewModel.reminderEnabled.collectAsStateWithLifecycle()
    val sfxEnabled by viewModel.sfxEnabled.collectAsStateWithLifecycle()
    val hapticsEnabled by viewModel.hapticsEnabled.collectAsStateWithLifecycle()
    val reducedMotion by viewModel.reducedMotion.collectAsStateWithLifecycle()
    val dailyFreeLogs by viewModel.dailyFreeLogs.collectAsStateWithLifecycle()
    val membership by viewModel.membership.collectAsStateWithLifecycle()
    val refreshBusy by viewModel.refreshBusy.collectAsStateWithLifecycle()
    val refreshFeedback by viewModel.refreshFeedback.collectAsStateWithLifecycle()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val reminderHour by viewModel.reminderHour.collectAsStateWithLifecycle()
    val crashReportsEnabled by viewModel.crashReportsEnabled.collectAsStateWithLifecycle()
    val backupFolderUri by viewModel.backupFolderUri.collectAsStateWithLifecycle()
    val autoBackupEnabled by viewModel.autoBackupEnabled.collectAsStateWithLifecycle()
    val lastBackupAt by viewModel.lastBackupAt.collectAsStateWithLifecycle()
    val backupBusy by viewModel.backupBusy.collectAsStateWithLifecycle()
    val backupFeedback by viewModel.backupFeedback.collectAsStateWithLifecycle()
    val updateAvailable by viewModel.updateAvailable.collectAsStateWithLifecycle()
    val updateCheckBusy by viewModel.updateCheckBusy.collectAsStateWithLifecycle()
    val updateFeedback by viewModel.updateFeedback.collectAsStateWithLifecycle()
    val paid = membership.hasProAccessAt(System.currentTimeMillis())
    var showWipeConfirm by remember { mutableStateOf(false) }
    var showReminderTimePicker by remember { mutableStateOf(false) }
    var pendingRestoreUri by remember { mutableStateOf<Uri?>(null) }
    val palette = AppTheme.colors
    val haptics = rememberAppHaptics()
    val sfx = rememberAppSfx()
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.setReminderEnabled(true, context)
        } else {
            // User denied — keep toggle off
            viewModel.setReminderEnabled(false, context)
        }
    }

    val backupFolderLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            viewModel.setBackupFolder(uri.toString())
        }
    }

    val restoreFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) pendingRestoreUri = uri
    }

    val backupFolderLabel = remember(backupFolderUri) {
        backupFolderUri?.let { stored ->
            runCatching { DocumentFile.fromTreeUri(context, Uri.parse(stored))?.name }.getOrNull()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themedInkWash())
            .verticalScrollFirst()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 12.dp)
    ) {
        Eyebrow("PREFERENCES", color = palette.crimson, maxLines = 1)
        Spacer(Modifier.height(6.dp))
        Text(
            "SETTINGS",
            style = AppType.screenTitle,
            color = palette.textPrimary,
            maxLines = 2
        )
        Spacer(Modifier.height(8.dp))
        GiltRule(width = 40.dp)

        Spacer(Modifier.height(28.dp))

        // Membership Desk for Pro/Max; compact free invite otherwise. The `plain`
        // test flavor strips all Pro/Max surfaces — no plan invite, no desk.
        if (!BuildConfig.PLAIN_FREE) {
            SectionLabelWithIcon(Icons.Outlined.CardMembership, if (paid) "MEMBERSHIP" else "ACCOUNT", AppTheme.colors.gold)
            Spacer(Modifier.height(10.dp))
            if (paid) {
                MembershipDesk(
                    entitlement = membership,
                    authState = authState,
                    refreshBusy = refreshBusy,
                    refreshFeedback = refreshFeedback,
                    onRenew = onOpenPaywall,
                    onUpgradeToMax = onOpenPaywall,
                    onRefresh = { viewModel.refreshMembership() },
                    onSignOut = { authViewModel.signOut() },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                PremiumSurface(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "No account on free plan",
                            style = AppType.bodyMd,
                            color = palette.textPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Google sign-in only appears when you start Pro or Max. Free logging stays private on this device.",
                            style = AppType.bodySm,
                            color = palette.textMuted
                        )
                        Spacer(Modifier.height(12.dp))
                        GiltButton(
                            onClick = onOpenPaywall,
                            text = "View Pro & Max plans",
                            modifier = Modifier.fillMaxWidth(),
                            height = 48.dp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabelWithIcon(Icons.Outlined.CurrencyExchange, "CURRENCY", AppTheme.colors.crimson)
        Spacer(Modifier.height(10.dp))
        SettingsPanel {
            Column(modifier = Modifier.padding(6.dp)) {
                currencies.forEachIndexed { i, option ->
                    val selected = option.code == currentCode
                    PreferenceRow(
                        selected = selected,
                        onClick = { if (!selected) haptics.tick(); viewModel.setCurrency(option.symbol, option.code) },
                        leading = {
                            Text(
                                option.symbol,
                                style = AppType.titleMd,
                                color = if (selected) palette.gilt else palette.textSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.width(14.dp))
                            Text(
                                option.label,
                                style = AppType.bodyMd,
                                color = if (selected) palette.textPrimary else palette.textSecondary
                            )
                        }
                    )
                    if (i < currencies.lastIndex) {
                        HorizontalDivider(
                            color = palette.inkDivider,
                            modifier = Modifier.padding(horizontal = 12.dp),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.settings_currency_caption),
            style = AppType.caption,
            color = palette.textMuted
        )

        Spacer(Modifier.height(28.dp))

        SectionLabelWithIcon(Icons.Outlined.TextFields, "TEXT SIZE", AppTheme.colors.textSecondary)
        Spacer(Modifier.height(10.dp))
        SettingsPanel {
            Column(modifier = Modifier.padding(6.dp)) {
                fontScaleOptions.forEachIndexed { i, option ->
                    val selected = option.step == fontScaleStep
                    PreferenceRow(
                        selected = selected,
                        onClick = { if (!selected) haptics.tick(); viewModel.setFontScaleStep(option.step) },
                        leading = {
                            Text(
                                option.label,
                                style = AppType.bodyMd,
                                color = if (selected) palette.textPrimary else palette.textSecondary,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    )
                    if (i < fontScaleOptions.lastIndex) {
                        HorizontalDivider(
                            color = palette.inkDivider,
                            modifier = Modifier.padding(horizontal = 12.dp),
                            thickness = 1.dp
                        )
                    }
                }
                HorizontalDivider(color = palette.inkDivider, modifier = Modifier.padding(horizontal = 12.dp))
                Text(
                    "Sample: ${symbol}1,250.00  Need",
                    style = AppType.body,
                    color = palette.textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabelWithIcon(Icons.Outlined.Palette, "APPEARANCE", AppTheme.colors.textSecondary)
        Spacer(Modifier.height(10.dp))
        SettingsPanel {
            Column(modifier = Modifier.padding(6.dp)) {
                themeOptions.forEachIndexed { i, option ->
                    val selected = option.id == themeId
                    // Preview chips: System shows light as the representative swatch.
                    val swatch = when (option.id) {
                        ThemeId.MARKET_DARK -> AppPalette.marketDark()
                        ThemeId.HIGH_CONTRAST -> AppPalette.highContrast()
                        else -> AppPalette.marketLight()
                    }
                    PreferenceRow(
                        selected = selected,
                        onClick = { if (!selected) haptics.tick(); viewModel.setThemeId(option.id) },
                        leading = {
                            ThemeSwatches(swatch)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                option.label,
                                style = AppType.bodyMd,
                                color = if (selected) palette.textPrimary else palette.textSecondary,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    )
                    if (i < themeOptions.lastIndex) {
                        HorizontalDivider(
                            color = palette.inkDivider,
                            modifier = Modifier.padding(horizontal = 12.dp),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // Manage plan — opens the paywall for renew/upgrade (redundant actions live on
// the Membership Desk for paid users; kept for free users to discover plans).
        // The `plain` test flavor strips it entirely.
        if (!BuildConfig.PLAIN_FREE) {
            SectionLabelWithIcon(Icons.Outlined.WorkspacePremium, "PLAN", AppTheme.colors.gold)
            Spacer(Modifier.height(10.dp))
            SettingsPanel {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onOpenPaywall)
                        .padding(horizontal = 12.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            if (paid) "Manage membership" else "Go Pro & Max",
                            style = AppType.bodyMd,
                            color = palette.textPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            if (paid) "Renew, upgrade to Max, or review plans." else "Unlimited log, lifetime history, Max AI Advisor.",
                            style = AppType.bodySm,
                            color = palette.textMuted
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        if (paid) "manage membership" else "upgrade to pro",
                        tint = palette.gold.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        val freeLogs = dailyFreeLogs
        // Only render the section (title + panel) when the quota applies —
        // i.e. the user is Free. Pro/Max get no DAILY FREE LOGS header at all.
        if (freeLogs != null) {
            SectionLabelWithIcon(Icons.Outlined.Today, "DAILY FREE LOGS", AppTheme.colors.marketGreen)
            Spacer(Modifier.height(10.dp))
            SettingsPanel {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    QuotaStatRow(
                        label = "Free allowance",
                        value = "${freeLogs.allowancePerDay} / day",
                        valueColor = palette.gilt
                    )
                    HorizontalDivider(color = palette.inkDivider)
                    QuotaStatRow(
                        label = "Left today",
                        value = "${freeLogs.remainingToday}",
                        valueColor = if (freeLogs.remainingToday > 0) palette.marketGreen else palette.crimson
                    )
                    if (freeLogs.carriedLogs > 0) {
                        HorizontalDivider(color = palette.inkDivider)
                        QuotaStatRow(
                            label = "Carried in",
                            value = "+${freeLogs.carriedLogs}",
                            valueColor = palette.gilt
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Unused Free logs carry to the next day while your logging streak stays active. Pro & Max have no daily limit.",
                        style = AppType.caption,
                        color = palette.textMuted
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabelWithIcon(Icons.Outlined.Feedback, "FEEDBACK", AppTheme.colors.crimson)
        Spacer(Modifier.height(10.dp))
        SettingsPanel {
            FeedbackToggleRow(
                title = "Sound effects",
                body = "Tap, long-press, and orb sounds. Uses media volume.",
                checked = sfxEnabled,
                onCheckedChange = { checked ->
                    sfx.enabled = checked
                    viewModel.setSfxEnabled(checked)
                    if (checked) sfx.tap()
                    if (hapticsEnabled) haptics.tick()
                },
                palette = palette
            )
            HorizontalDivider(color = palette.inkDivider, modifier = Modifier.padding(horizontal = 16.dp))
            FeedbackToggleRow(
                title = "Vibration",
                body = "Haptic ticks for seals, chips, deletes, and warnings.",
                checked = hapticsEnabled,
                onCheckedChange = { checked ->
                    haptics.enabled = checked
                    viewModel.setHapticsEnabled(checked)
                    if (checked) haptics.tick()
                },
                palette = palette
            )
            HorizontalDivider(color = palette.inkDivider, modifier = Modifier.padding(horizontal = 16.dp))
            FeedbackToggleRow(
                title = "Reduced motion",
                body = "Instant transitions — no page flip, ink wave, or portal land.",
                checked = reducedMotion,
                onCheckedChange = { checked ->
                    viewModel.setReducedMotion(checked)
                    if (hapticsEnabled) haptics.tick()
                },
                palette = palette
            )
        }

        Spacer(Modifier.height(28.dp))

        SectionLabelWithIcon(Icons.Outlined.Notifications, stringResource(R.string.settings_notifications_label), AppTheme.colors.textSecondary)
        Spacer(Modifier.height(10.dp))
        SettingsPanel {
            FeedbackToggleRow(
                title = stringResource(R.string.settings_reminder_title),
                body = stringResource(R.string.settings_reminder_body, formatHour12(reminderHour)),
                checked = reminderEnabled,
                onCheckedChange = { checked ->
                    if (hapticsEnabled) haptics.tick()
                    if (!checked) {
                        viewModel.setReminderEnabled(false, context)
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val granted = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                        if (granted) {
                            viewModel.setReminderEnabled(true, context)
                        } else {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        viewModel.setReminderEnabled(true, context)
                    }
                },
                palette = palette
            )
            HorizontalDivider(color = palette.inkDivider, modifier = Modifier.padding(horizontal = 16.dp))
            SettingsNavRow(
                title = stringResource(R.string.settings_reminder_time_title),
                body = stringResource(R.string.settings_reminder_time_body),
                onClick = {
                    if (hapticsEnabled) haptics.tick()
                    showReminderTimePicker = true
                },
                trailing = {
                    Text(
                        formatHour12(reminderHour),
                        style = AppType.bodyMd,
                        color = palette.gilt,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }

        Spacer(Modifier.height(28.dp))

        SectionLabelWithIcon(Icons.Outlined.Save, stringResource(R.string.settings_backup_label), AppTheme.colors.marketGreen)
        Spacer(Modifier.height(10.dp))
        SettingsPanel {
            SettingsNavRow(
                title = stringResource(R.string.settings_backup_folder_title),
                body = backupFolderLabel
                    ?: stringResource(R.string.settings_backup_folder_unset),
                onClick = {
                    if (hapticsEnabled) haptics.tick()
                    runCatching { backupFolderLauncher.launch(null) }
                }
            )
            HorizontalDivider(color = palette.inkDivider, modifier = Modifier.padding(horizontal = 16.dp))
            FeedbackToggleRow(
                title = stringResource(R.string.settings_backup_auto_title),
                body = stringResource(R.string.settings_backup_auto_body),
                checked = autoBackupEnabled && backupFolderUri != null,
                onCheckedChange = { checked ->
                    if (hapticsEnabled) haptics.tick()
                    if (backupFolderUri == null) {
                        runCatching { backupFolderLauncher.launch(null) }
                    } else {
                        viewModel.setAutoBackupEnabled(checked)
                    }
                },
                palette = palette
            )
            HorizontalDivider(color = palette.inkDivider, modifier = Modifier.padding(horizontal = 16.dp))
            SettingsNavRow(
                title = if (backupBusy) {
                    stringResource(R.string.settings_backup_now_busy)
                } else {
                    stringResource(R.string.settings_backup_now_title)
                },
                body = if (lastBackupAt > 0L) {
                    stringResource(R.string.settings_backup_last, formatBackupStamp(lastBackupAt))
                } else {
                    stringResource(R.string.settings_backup_now_body)
                },
                onClick = { if (!backupBusy) viewModel.backupNow() }
            )
            HorizontalDivider(color = palette.inkDivider, modifier = Modifier.padding(horizontal = 16.dp))
            SettingsNavRow(
                title = stringResource(R.string.settings_backup_restore_title),
                body = stringResource(R.string.settings_backup_restore_body),
                onClick = {
                    if (!backupBusy) {
                        runCatching {
                            restoreFileLauncher.launch(
                                arrayOf("application/json", "application/octet-stream", "text/plain")
                            )
                        }
                    }
                }
            )
            backupFeedback?.let { feedback ->
                Text(
                    feedback,
                    style = AppType.caption,
                    color = palette.textSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabelWithIcon(Icons.Outlined.DeleteSweep, stringResource(R.string.settings_data_label), AppTheme.colors.want)
        Spacer(Modifier.height(10.dp))
        SettingsPanel {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showWipeConfirm = true }
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        stringResource(R.string.settings_wipe_title),
                        style = AppType.bodyMd,
                        color = palette.danger,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        stringResource(R.string.settings_wipe_body),
                        style = AppType.caption,
                        color = palette.textMuted
                    )
                }
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    "wipe",
                    tint = palette.danger.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabelWithIcon(Icons.Outlined.Shield, stringResource(R.string.settings_privacy_label), AppTheme.colors.textSecondary)
        Spacer(Modifier.height(10.dp))
        SettingsPanel {
            FeedbackToggleRow(
                title = stringResource(R.string.settings_crash_title),
                body = stringResource(R.string.settings_crash_body),
                checked = crashReportsEnabled,
                onCheckedChange = { checked ->
                    if (hapticsEnabled) haptics.tick()
                    viewModel.setCrashReportsEnabled(checked)
                },
                palette = palette
            )
            HorizontalDivider(color = palette.inkDivider, modifier = Modifier.padding(horizontal = 16.dp))
            SettingsNavRow(
                title = stringResource(R.string.settings_privacy_policy_title),
                body = stringResource(R.string.settings_privacy_policy_body),
                onClick = { openUrl(context, "https://needs-vs-wants.vercel.app/privacy.html") }
            )
            HorizontalDivider(color = palette.inkDivider, modifier = Modifier.padding(horizontal = 16.dp))
            SettingsNavRow(
                title = stringResource(R.string.settings_terms_title),
                body = stringResource(R.string.settings_terms_body),
                onClick = { openUrl(context, "https://needs-vs-wants.vercel.app/terms.html") }
            )
        }

        Spacer(Modifier.height(28.dp))

        SectionLabelWithIcon(Icons.Outlined.Info, "ABOUT", AppTheme.colors.textMuted)
        Spacer(Modifier.height(10.dp))
        SettingsPanel {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NeedWantSealMark()
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Needs vs Wants",
                            style = AppType.titleSm,
                            color = palette.textPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            if (paid) "Pro / Max spending diary" else "30-day spending trainer",
                            style = AppType.caption,
                            color = palette.textMuted
                        )
                    }
                    Text("v${BuildConfig.VERSION_NAME}", style = AppType.caption, color = palette.textMuted)
                }
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(palette.divider)
                )
                Spacer(Modifier.height(10.dp))
                ReceiptFeatureLine(
                    text = "Every purchase seals as Need or Want",
                    accent = palette.marketGreen,
                    emphasize = true
                )
                ReceiptFeatureLine(
                    text = if (paid) "Unlimited sheets, lifetime diary history" else "20 entries per sheet, 30-day diary window",
                    accent = palette.gold
                )
                ReceiptFeatureLine(
                    text = "Optional daily budget on Log",
                    accent = palette.crimson
                )
                ReceiptFeatureLine(
                    text = "Offline first. No cloud for free use.",
                    accent = palette.textMuted
                )
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(palette.divider)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "CREDITS",
                    style = AppType.caption,
                    color = palette.textMuted,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(6.dp))
                SfxCredits.ABOUT_LINES.forEach { line ->
                    Text(
                        line,
                        style = AppType.caption,
                        color = palette.textSecondary
                    )
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "Follow on TikTok · @expenseneedswants",
                    style = AppType.caption,
                    color = palette.textSecondary,
                    modifier = Modifier
                        .clickable {
                            runCatching {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.tiktok.com/@expenseneedswants")
                                    )
                                )
                            }
                        }
                        .padding(vertical = 4.dp)
                )
                Spacer(Modifier.height(6.dp))
                updateAvailable?.let { update ->
                    Text(
                        "Update available — v${update.versionName}. Tap to download.",
                        style = AppType.caption,
                        color = palette.marketGreen,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { openUrl(context, update.apkUrl) }
                            .padding(vertical = 4.dp)
                    )
                }
                Text(
                    if (updateCheckBusy) "Checking for updates…" else "Check for updates",
                    style = AppType.caption,
                    color = palette.textSecondary,
                    modifier = Modifier
                        .clickable { if (!updateCheckBusy) viewModel.checkForUpdates() }
                        .padding(vertical = 4.dp)
                )
                updateFeedback?.let { feedback ->
                    Text(feedback, style = AppType.caption, color = palette.textMuted)
                }
            }
        }
    }

    if (showWipeConfirm) {
        PremiumDialog(
            onDismissRequest = { showWipeConfirm = false },
            eyebrow = "DANGER",
            eyebrowColor = palette.danger,
            title = stringResource(R.string.settings_wipe_dialog_title),
            body = stringResource(R.string.settings_wipe_dialog_body),
            confirmLabel = stringResource(R.string.settings_wipe_dialog_confirm),
            onConfirm = {
                haptics.warn()
                viewModel.wipeData()
                showWipeConfirm = false
            },
            dismissLabel = stringResource(R.string.common_cancel),
            confirmDanger = true
        )
    }

    if (showReminderTimePicker) {
        AlertDialog(
            onDismissRequest = { showReminderTimePicker = false },
            containerColor = palette.surfaceCard,
            title = {
                Text(
                    stringResource(R.string.settings_reminder_time_dialog_title),
                    style = AppType.titleSm,
                    color = palette.textPrimary
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .heightIn(max = 360.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    (0..23).forEach { hour ->
                        val selected = hour == reminderHour
                        Text(
                            formatHour12(hour),
                            style = AppType.bodyMd,
                            color = if (selected) palette.gilt else palette.textPrimary,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (hapticsEnabled) haptics.tick()
                                    viewModel.setReminderHour(hour, context)
                                    showReminderTimePicker = false
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showReminderTimePicker = false }) {
                    Text(stringResource(R.string.common_cancel), color = palette.textSecondary)
                }
            }
        )
    }

    pendingRestoreUri?.let { restoreUri ->
        PremiumDialog(
            onDismissRequest = { pendingRestoreUri = null },
            eyebrow = "RESTORE",
            eyebrowColor = palette.marketGreen,
            title = stringResource(R.string.settings_restore_dialog_title),
            body = stringResource(R.string.settings_restore_dialog_body),
            confirmLabel = stringResource(R.string.settings_restore_dialog_confirm),
            onConfirm = {
                viewModel.restoreFrom(restoreUri)
                pendingRestoreUri = null
            },
            dismissLabel = stringResource(R.string.common_cancel)
        )
    }
}

/** 24h → "8:00 PM" style label for the reminder rows. */
private fun formatHour12(hour: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val display = ((hour + 11) % 12) + 1
    return "$display:00 $amPm"
}

private fun formatBackupStamp(atMillis: Long): String =
    SimpleDateFormat("MMM d, yyyy · h:mm a", Locale.getDefault()).format(Date(atMillis))

private fun openUrl(context: android.content.Context, url: String) {
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

@Composable
private fun FeedbackToggleRow(
    title: String,
    body: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    palette: AppPalette,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = AppType.bodyMd,
                color = palette.textPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                body,
                style = AppType.bodySm,
                color = palette.textMuted
            )
        }
        Spacer(Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = palette.surfaceCard,
                checkedTrackColor = palette.crimson,
                uncheckedThumbColor = palette.textMuted,
                uncheckedTrackColor = palette.inkDivider
            )
        )
    }
}

/** Clickable settings row: title + optional body, trailing slot (chevron by default). */
@Composable
private fun SettingsNavRow(
    title: String,
    body: String? = null,
    titleColor: Color? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    val palette = AppTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = AppType.bodyMd,
                color = titleColor ?: palette.textPrimary,
                fontWeight = FontWeight.SemiBold
            )
            if (body != null) {
                Spacer(Modifier.height(2.dp))
                Text(body, style = AppType.bodySm, color = palette.textMuted)
            }
        }
        Spacer(Modifier.width(12.dp))
        if (trailing != null) {
            trailing()
        } else {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = palette.textMuted.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun PreferenceRow(
    selected: Boolean,
    onClick: () -> Unit,
    leading: @Composable RowScope.() -> Unit
) {
    val palette = AppTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val fillAlpha by animateFloatAsState(
            targetValue = if (selected) 1f else 0f,
            animationSpec = Motion.state(),
            label = "prefIndicatorFill"
        )
        val dotScale by animateFloatAsState(
            targetValue = if (selected) 1f else 0f,
            animationSpec = Motion.selectionSpring(),
            label = "prefIndicatorDot"
        )
        Box(
            modifier = Modifier
                .size(18.dp)
                .border(1.dp, palette.inkDividerStrong, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(listOf(palette.gilt, palette.giltSoft)),
                        RoundedCornerShape(9.dp)
                    )
                    .graphicsLayer { alpha = fillAlpha }
            )
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize()
                    .background(palette.background, RoundedCornerShape(3.dp))
                    .graphicsLayer {
                        scaleX = dotScale
                        scaleY = dotScale
                    }
            )
        }
        Spacer(Modifier.width(14.dp))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            content = leading
        )
    }
}

@Composable
private fun ThemeSwatches(swatch: AppPalette) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(swatch.background, RoundedCornerShape(3.dp))
                .border(1.dp, swatch.divider, RoundedCornerShape(3.dp))
        )
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(swatch.need, RoundedCornerShape(3.dp))
        )
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(swatch.want, RoundedCornerShape(3.dp))
        )
    }
}

@Composable
private fun QuotaStatRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = AppType.bodyMd,
            color = AppTheme.colors.textSecondary
        )
        Text(
            value,
            style = AppType.bodyMd,
            color = valueColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Eyebrow(text, color = AppTheme.colors.textMuted, size = 12)
}

/** Section label with a small tinted icon (design audit #15) — breaks Settings monotony. */
@Composable
private fun SectionLabelWithIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    tint: Color = AppTheme.colors.crimson
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        SectionLabel(text)
    }
}
