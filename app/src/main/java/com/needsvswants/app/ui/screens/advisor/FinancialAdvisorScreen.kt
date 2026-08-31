package com.needsvswants.app.ui.screens.advisor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.needsvswants.app.domain.AdvisorProtocols
import com.needsvswants.app.domain.ChatMessage
import com.needsvswants.app.domain.ChatSender
import com.needsvswants.app.domain.advisorProtocolQuery
import com.needsvswants.app.ui.theme.AppShapes
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
import com.needsvswants.app.ui.theme.PaywallCopy
import com.needsvswants.app.ui.theme.PremiumSurface
import com.needsvswants.app.ui.theme.ReceiptFeatureLine
import com.needsvswants.app.ui.theme.SelectChip
import com.needsvswants.app.ui.theme.TierTag
import com.needsvswants.app.ui.theme.rememberAppHaptics
import com.needsvswants.app.ui.theme.rememberIdleBreathAlpha
import com.needsvswants.app.ui.theme.themedInkWash

@Composable
fun FinancialAdvisorScreen(
    onOpenPaywall: () -> Unit = {},
    viewModel: FinancialAdvisorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var recoveryDismissed by rememberSaveable { mutableStateOf(false) }
    val palette = AppTheme.colors
    val haptics = rememberAppHaptics()
    val listState = rememberLazyListState()
    val gateScroll = rememberScrollState()
    val screenW = LocalConfiguration.current.screenWidthDp.dp

    // Full-height chat surface (D191): header + insight stay fixed, the
    // conversation fills the rest, and the input never scrolls away. The
    // locked gate keeps its own scroll for small screens and XL text.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themedInkWash())
            .imePadding()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 12.dp)
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
        Spacer(Modifier.height(12.dp))
        Text(
            text = if (uiState.hasMaxAccess) {
                "Cited coaching from ${uiState.sourceOfTruthTitle} — everything works offline."
            } else {
                "Conversational coaching with footnotes. Max tier only."
            },
            style = AppType.body,
            color = palette.textSecondary
        )

        if (!uiState.hasMaxAccess) {
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(gateScroll)
            ) {
                MaxLockedGate(onOpenPaywall = onOpenPaywall)
            }
            return@Column
        }

        Spacer(Modifier.height(16.dp))

        uiState.insight?.let { insight ->
            val edge = if (insight.isWarning) palette.crimson.copy(alpha = 0.45f)
            else palette.marketGreen.copy(alpha = 0.45f)
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(Motion.entrance()),
                label = "todayInsightCard"
            ) {
                Surface(
                    shape = AppShapes.r16,
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
                            size = 11
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
                        Eyebrow("CITATION", color = palette.gilt, size = 11)
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

        // Conversation (D191): reverse layout anchors the newest message, the
        // thinking indicator reads as the advisor's reply-in-progress, and the
        // protocol suggestions + input stay pinned below.
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            if (uiState.isLoading) {
                item(key = "advisor-thinking") { ThinkingRow() }
            }
            if (uiState.chatMessages.isEmpty()) {
                item(key = "advisor-empty") {
                    EmptyChatHint(
                        onSelectStarterPrompt = { prompt ->
                            viewModel.sendUserQuery(prompt)
                        }
                    )
                }
            } else {
                items(uiState.chatMessages.asReversed()) { msg ->
                    ChatBubble(message = msg, maxBubbleWidth = screenW * 0.85f)
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdvisorProtocols.ALL.forEach { protocol ->
                SelectChip(
                    label = protocol,
                    selected = false,
                    color = palette.gold,
                    compact = true,
                    onClick = {
                        haptics.tick()
                        // Re-surfaces the live recovery plan (rebuilt from current context in the VM).
                        if (protocol == AdvisorProtocols.OVERSPEND) recoveryDismissed = false
                        viewModel.sendUserQuery(advisorProtocolQuery(protocol))
                    }
                )
            }
        }
        Spacer(Modifier.height(10.dp))

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
                        haptics.tick()
                        viewModel.sendUserQuery(inputText)
                        inputText = ""
                    }
                },
                contentDescription = "Send",
                enabled = inputText.isNotBlank(),
                filled = true,
                // Action accent, not the Need semantic (D190). Ink is the
                // same card-white GiltButton uses on crimson fills.
                fillColor = palette.crimson
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = null,
                    tint = palette.surfaceCard,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun MaxLockedGate(onOpenPaywall: () -> Unit) {
    val palette = AppTheme.colors
    PremiumSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.r20,
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
                text = "${PaywallCopy.MAX_MONTHLY}/mo · includes everything in Pro",
                style = AppType.bodySm,
                color = palette.textMuted,
                textAlign = TextAlign.Center
            )
        }
    }

    Spacer(Modifier.height(16.dp))

    PremiumSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.r16,
        goldEdge = false,
        raised = false
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Eyebrow("INCLUDED WITH MAX", color = palette.gilt, size = 11)
            Spacer(Modifier.height(10.dp))
            ReceiptFeatureLine("Real-time Need vs Want coaching", palette.crimson)
            ReceiptFeatureLine("Overspend recovery prompts", palette.crimson)
            ReceiptFeatureLine("Cited notebook recommendations", palette.crimson, emphasize = true)
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage, maxBubbleWidth: Dp) {
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
                bottomStart = if (isUser) 14.dp else 6.dp,
                bottomEnd = if (isUser) 6.dp else 14.dp
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isUser) palette.marketGreen.copy(alpha = 0.28f) else palette.gold.copy(alpha = 0.28f)
            ),
            // Proportional cap: a fixed 280dp wrapped long answers into narrow
            // towers at XL text; 85% of the screen scales with the surface (D191).
            modifier = Modifier.widthIn(max = maxBubbleWidth)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                // No YOU/ADVISOR labels — alignment and tint already say who
                // speaks, and the label was louder than the message (D190).
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

/** The advisor's reply-in-progress, styled as the last turn in the conversation. */
@Composable
private fun ThinkingRow() {
    val palette = AppTheme.colors
    val breathAlpha = rememberIdleBreathAlpha()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(AppShapes.r12)
            .background(palette.gold.copy(alpha = 0.08f * breathAlpha))
            .border(
                androidx.compose.foundation.BorderStroke(1.dp, palette.gold.copy(alpha = 0.28f * breathAlpha)),
                AppShapes.r12
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp,
            color = palette.gilt
        )
        Spacer(Modifier.width(10.dp))
        Text(
            "Reading your ledger…",
            style = AppType.bodySm.copy(fontWeight = FontWeight.Medium),
            color = palette.gilt
        )
    }
}

/** Quiet first-run prompt when the conversation has no messages yet. */
@Composable
private fun EmptyChatHint(
    onSelectStarterPrompt: (String) -> Unit = {}
) {
    val palette = AppTheme.colors
    val haptics = rememberAppHaptics()
    val starterPrompts = listOf(
        "What is the 30-day rule?",
        "How do I curb impulse Wants?",
        "Explain my spending split"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Eyebrow("ASK YOUR ADVISOR", color = palette.gilt, size = 11)
        Spacer(Modifier.height(6.dp))
        Text(
            "Ask about your ledger, a Want you're weighing, or the day's budget. Answers cite the study notes.",
            style = AppType.bodyMd,
            color = palette.textSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(14.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            starterPrompts.forEach { prompt ->
                SelectChip(
                    label = prompt,
                    selected = false,
                    color = palette.gilt,
                    compact = true,
                    onClick = {
                        haptics.tick()
                        onSelectStarterPrompt(prompt)
                    }
                )
            }
        }
    }
}
