package com.needsvswants.app.ui.screens.settings

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.domain.FontScaleStep
import com.needsvswants.app.domain.ThemeId
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
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    var showWipeConfirm by remember { mutableStateOf(false) }
    val palette = AppTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themedInkWash())
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 12.dp)
    ) {
        Eyebrow("PREFERENCES", color = palette.crimson, maxLines = 1)
        Spacer(Modifier.height(6.dp))
        Text(
            "SETTINGS",
            style = MaterialTheme.typography.displayLarge,
            color = palette.textPrimary,
            maxLines = 2
        )
        Spacer(Modifier.height(8.dp))
        GiltRule(width = 40.dp)

        Spacer(Modifier.height(28.dp))

        // Account: free users cannot sign in here — Google is only on the Pro/Max paywall.
        SectionLabel("ACCOUNT")
        Spacer(Modifier.height(10.dp))
        PremiumSurface(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (authState.signedIn) {
                    Text(
                        authState.email ?: "Signed in",
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Linked for Pro / Max purchases and entitlement sync.",
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.textMuted
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { authViewModel.signOut() },
                        enabled = !authState.busy,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, palette.inkDivider),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign out", color = palette.textPrimary)
                    }
                } else {
                    Text(
                        "No account on free plan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Google sign-in unlocks only when you start Pro or Max — free logging stays private on-device.",
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.textMuted
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onOpenPaywall,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, palette.gold.copy(alpha = 0.55f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Pro & Max plans", color = palette.textPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabel("CURRENCY")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = palette.inkElevated,
            border = BorderStroke(1.dp, palette.inkDivider)
        ) {
            Column(modifier = Modifier.padding(6.dp)) {
                currencies.forEachIndexed { i, option ->
                    val selected = option.code == currentCode
                    PreferenceRow(
                        selected = selected,
                        onClick = { viewModel.setCurrency(option.symbol, option.code) },
                        leading = {
                            Text(
                                option.symbol,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (selected) palette.gilt else palette.textSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.width(14.dp))
                            Text(
                                option.label,
                                style = MaterialTheme.typography.bodyMedium,
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

        Spacer(Modifier.height(28.dp))

        SectionLabel("TEXT SIZE")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = palette.inkElevated,
            border = BorderStroke(1.dp, palette.inkDivider)
        ) {
            Column(modifier = Modifier.padding(6.dp)) {
                fontScaleOptions.forEachIndexed { i, option ->
                    val selected = option.step == fontScaleStep
                    PreferenceRow(
                        selected = selected,
                        onClick = { viewModel.setFontScaleStep(option.step) },
                        leading = {
                            Text(
                                option.label,
                                style = MaterialTheme.typography.bodyMedium,
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
                    "Sample: ${symbol}1,250.00 · Need",
                    style = MaterialTheme.typography.bodyLarge,
                    color = palette.textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabel("APPEARANCE")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = palette.inkElevated,
            border = BorderStroke(1.dp, palette.inkDivider)
        ) {
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
                        onClick = { viewModel.setThemeId(option.id) },
                        leading = {
                            ThemeSwatches(swatch)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                option.label,
                                style = MaterialTheme.typography.bodyMedium,
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

        SectionLabel("SUBSCRIPTION")
        Spacer(Modifier.height(10.dp))
        PremiumSurface(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenPaywall)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Pro & Max plans",
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Sign in with Google only when you upgrade — unlimited log, full history, Max AI Advisor.",
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.textMuted
                    )
                }
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    "upgrade to pro",
                    tint = palette.gold.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionLabel("DATA")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = palette.inkElevated,
            border = BorderStroke(1.dp, palette.inkDivider)
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
                    Text(
                        "Wipe diary",
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.danger,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Permanently delete all entries & reset settings",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.2.sp),
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

        SectionLabel("ABOUT")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = palette.inkElevated,
            border = BorderStroke(1.dp, palette.inkDivider),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(palette.crimson, RoundedCornerShape(2.dp))
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Needs vs. Wants Expense Tracker",
                        style = MaterialTheme.typography.titleSmall,
                        color = palette.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.weight(1f))
                    Text("v1.4.0", style = MaterialTheme.typography.labelSmall, color = palette.textMuted)
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "This app allows you to record all of your daily expenses, helping you become more aware of your spending habits and tendencies. By consistently tracking every expense, you can better distinguish between your needs and wants, make smarter financial decisions, and develop stronger self-discipline. The key is to be honest with yourself\u2014every expense counts. Start tracking today and take control of your finances.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textSecondary
                )
            }
        }
    }

    if (showWipeConfirm) {
        AlertDialog(
            onDismissRequest = { showWipeConfirm = false },
            containerColor = palette.inkElevated,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column {
                    Eyebrow("DANGER", color = palette.danger)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Wipe all data?",
                        color = palette.textPrimary,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            },
            text = {
                Text(
                    "This will permanently delete all entries and reset settings.\nThere is no recovery.",
                    color = palette.textSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.wipeData()
                        showWipeConfirm = false
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = palette.danger,
                        contentColor = Color.White
                    )
                ) { Text("Wipe", fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                TextButton(onClick = { showWipeConfirm = false }) {
                    Text("Cancel", color = palette.textMuted)
                }
            }
        )
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
        Box(
            modifier = Modifier
                .size(18.dp)
                .then(
                    if (selected) {
                        Modifier.background(
                            Brush.horizontalGradient(listOf(palette.gilt, palette.giltSoft)),
                            RoundedCornerShape(9.dp)
                        )
                    } else {
                        Modifier.border(1.dp, palette.inkDividerStrong, RoundedCornerShape(9.dp))
                    }
                )
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(palette.background, RoundedCornerShape(3.dp))
                        .fillMaxSize()
                )
            }
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
private fun SectionLabel(text: String) {
    Eyebrow(text, color = AppTheme.colors.textMuted, size = 12)
}
