package com.needsvswants.app.ui.screens.advisor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.needsvswants.app.domain.AdvisorProtocols
import com.needsvswants.app.domain.ChatMessage
import com.needsvswants.app.domain.ChatSender
import com.needsvswants.app.domain.advisorProtocolQuery
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.AppType
import com.needsvswants.app.ui.theme.Eyebrow
import com.needsvswants.app.ui.theme.GiltButton
import com.needsvswants.app.ui.theme.GiltRule
import com.needsvswants.app.ui.theme.HeaderIconWell
import com.needsvswants.app.ui.theme.LedgerField
import com.needsvswants.app.ui.theme.LockedBookIllustration
import com.needsvswants.app.ui.theme.MaxSealBadge
import com.needsvswants.app.ui.theme.Motion
import com.needsvswants.app.ui.theme.NeedWantSealMark
import com.needsvswants.app.ui.theme.PremiumSurface
import com.needsvswants.app.ui.theme.ReceiptFeatureLine
import com.needsvswants.app.ui.navigation.verticalScrollFirst
import com.needsvswants.app.ui.theme.TierTag
import com.needsvswants.app.ui.theme.themedInkWash

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FinancialAdvisorScreen(
    onOpenPaywall: () -> Unit = {},
    viewModel: FinancialAdvisorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var inputText by remember { mutableStateOf("") }
    var recoveryDismissed by rememberSaveable { mutableStateOf(false) }
    val palette = AppTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themedInkWash())
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 12.dp)
            .verticalScrollFirst()
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Eyebrow(
                    if (uiState.hasMaxAccess) "MAX  ·  AI ADVISOR" else "MAX  ·  LOCKED",
                    color = if (uiState.hasMaxAccess) palette.gilt else palette.crimson
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Financial Advisor",
                    style = AppType.screenTitle,
                    color = palette.textPrimary
                )
            }
            if (uiState.hasMaxAccess) {
                Column(horizontalAlignment = Alignment.End) {
                    MaxSealBadge(label = "MAX")
                    Spacer(Modifier.height(8.dp))
                    TierTag(text = "Unlocked", color = palette.marketGreen)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        GiltRule(width = 40.dp)
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (uiState.hasMaxAccess) {
                "Cited coaching from the app's built-in economic study notes."
            } else {
                "Conversational coaching with footnotes. Max tier only."
            },
            style = AppType.body,
            color = palette.textSecondary
        )

        Spacer(Modifier.height(22.dp))

        if (!uiState.hasMaxAccess) {
            MaxLockedGate(onOpenPaywall = onOpenPaywall)
            return@Column
        }

        // ── Unlocked Max content ──────────────────────────────────────────
        PremiumSurface(goldEdge = true) {
            Column(modifier = Modifier.padding(16.dp)) {
                Eyebrow("SOURCE OF TRUTH", color = palette.marketGreen, size = 10)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = uiState.sourceOfTruthTitle,
                    style = AppType.titleMd,
                    color = palette.textPrimary
                )
                Text(
                    text = "Recommendations cite the built-in study notes — everything works offline.",
                    style = AppType.bodySm,
                    color = palette.textSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        if (uiState.isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = palette.gilt
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Reading your ledger…",
                    style = AppType.bodySm,
                    color = palette.textMuted
                )
            }
            Spacer(Modifier.height(8.dp))
        }

        uiState.insight?.let { insight ->
            val edge = if (insight.isWarning) palette.crimson.copy(alpha = 0.45f)
            else palette.marketGreen.copy(alpha = 0.45f)
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(Motion.entrance()),
                label = "todayInsightCard"
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = palette.surfaceCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, edge),
                    shadowElevation = 2.dp,
                    tonalElevation = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Eyebrow(
                            "TODAY'S INSIGHT",
                            color = if (insight.isWarning) palette.crimson else palette.marketGreen,
                            size = 10
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = insight.headline,
                            style = AppType.sectionTitle,
                            color = palette.textPrimary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = insight.advice,
                            style = AppType.bodyMd,
                            color = palette.textPrimary
                        )
                        Spacer(Modifier.height(12.dp))
                        GiltRule(width = 28.dp)
                        Spacer(Modifier.height(10.dp))
                        Eyebrow("CITATION", color = palette.gilt, size = 10)
                        Text(
                            text = insight.citation.title,
                            style = AppType.bodySmEmph,
                            color = palette.textPrimary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = insight.citation.section,
                            style = AppType.bodySm,
                            color = palette.textSecondary
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        uiState.recoveryPlan?.let { plan ->
            if (!recoveryDismissed) {
                RecoveryPlanCard(
                    plan = plan,
                    currencySymbol = uiState.currencySymbol,
                    onDismiss = { recoveryDismissed = true }
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        PremiumSurface(goldEdge = false, raised = false) {
            Column(modifier = Modifier.padding(16.dp)) {
                Eyebrow("QUICK PROTOCOLS", color = palette.gilt, size = 10)
                Spacer(Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AdvisorProtocols.ALL.forEach { protocol ->
                        QuickChip(protocol) {
                            if (protocol == AdvisorProtocols.OVERSPEND) {
                                // Re-surfaces the live recovery plan (rebuilt from current context in the VM).
                                recoveryDismissed = false
                            }
                            viewModel.sendUserQuery(advisorProtocolQuery(protocol))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        PremiumSurface(goldEdge = true) {
            Column(modifier = Modifier.padding(16.dp)) {
                Eyebrow("ASK YOUR ADVISOR", color = palette.gilt, size = 10)
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.chatMessages.forEach { msg ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(Motion.state()),
                            label = "chatBubble"
                        ) {
                            ChatBubble(message = msg)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LedgerField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = "Ask",
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp)
                    )
                    HeaderIconWell(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendUserQuery(inputText)
                                inputText = ""
                            }
                        },
                        contentDescription = "Send",
                        enabled = inputText.isNotBlank(),
                        filled = true,
                        fillColor = palette.marketGreen
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        PremiumSurface(goldEdge = false, raised = false) {
            Column(modifier = Modifier.padding(16.dp)) {
                Eyebrow("ECONOMIC STUDY NOTEBOOKS", color = palette.gilt, size = 10)
                Spacer(Modifier.height(10.dp))
                StudyTopicRow("Notebook 1", "Budgetary equilibrium and Need/Want ratio")
                StudyTopicRow("Notebook 2", "Real-time transaction behavioral control")
                StudyTopicRow("Notebook 3", "Impulse recovery and compensatory sinking")
            }
        }
    }
}

@Composable
private fun MaxLockedGate(onOpenPaywall: () -> Unit) {
    val palette = AppTheme.colors
    PremiumSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        goldEdge = true,
        raised = true
    ) {
        // Crimson flag strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .background(palette.crimson)
        )
        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NeedWantSealMark()
                MaxSealBadge(label = "MAX")
            }
            Spacer(Modifier.height(14.dp))
            LockedBookIllustration(modifier = Modifier.size(160.dp, 120.dp))
            Spacer(Modifier.height(14.dp))
            TierTag(text = "Max tier · Locked", color = palette.crimson)
            Spacer(Modifier.height(14.dp))
            Text(
                text = "AI Financial Advisor",
                style = AppType.dialogTitle,
                color = palette.textPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            GiltRule(width = 32.dp)
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Max adds conversational coaching grounded in economic study notebooks with footnotes. Free keeps Log, Summary, and History.",
                style = AppType.bodyMd,
                color = palette.textSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
            GiltButton(
                onClick = onOpenPaywall,
                text = "View Pro & Max plans",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "₱99 / mo · includes everything in Pro",
                style = AppType.bodySm,
                color = palette.textMuted,
                textAlign = TextAlign.Center
            )
        }
    }

    Spacer(Modifier.height(16.dp))

    PremiumSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        goldEdge = false,
        raised = false
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Eyebrow("INCLUDED WITH MAX", color = palette.gilt, size = 10)
            Spacer(Modifier.height(10.dp))
            ReceiptFeatureLine("Real-time Need vs Want coaching", palette.crimson)
            ReceiptFeatureLine("Overspend recovery prompts", palette.crimson)
            ReceiptFeatureLine("Cited notebook recommendations", palette.crimson, emphasize = true)
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUser = message.sender == ChatSender.USER
    val palette = AppTheme.colors
    Row(
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            color = if (isUser) palette.marketGreen.copy(alpha = 0.12f) else palette.surfaceCard,
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 14.dp
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isUser) palette.marketGreen.copy(alpha = 0.28f) else palette.gold.copy(alpha = 0.28f)
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                Text(
                    text = if (isUser) "YOU" else "ADVISOR",
                    style = AppType.eyebrowSm,
                    color = if (isUser) palette.marketGreen else palette.gold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = message.text,
                    style = AppType.bodySm,
                    color = palette.textPrimary
                )
                message.citation?.let { cit ->
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Source: ${cit.title}",
                        style = AppType.caption,
                        color = palette.textMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickChip(label: String, onClick: () -> Unit) {
    val palette = AppTheme.colors
    Surface(
        color = palette.surfaceRaised,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, palette.gold.copy(alpha = 0.40f)),
        modifier = Modifier
            .heightIn(min = 40.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            style = AppType.meta,
            color = palette.textPrimary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun StudyTopicRow(title: String, description: String) {
    val palette = AppTheme.colors
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(palette.marketGreen)
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                title,
                style = AppType.titleSm,
                color = palette.textPrimary
            )
            Text(description, style = AppType.bodySm, color = palette.textSecondary)
        }
    }
}
