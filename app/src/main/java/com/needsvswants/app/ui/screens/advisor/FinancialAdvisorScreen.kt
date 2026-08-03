package com.needsvswants.app.ui.screens.advisor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.needsvswants.app.domain.ChatMessage
import com.needsvswants.app.domain.ChatSender
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.Eyebrow
import com.needsvswants.app.ui.theme.GiltButton
import com.needsvswants.app.ui.theme.GiltRule

@Composable
fun FinancialAdvisorScreen(
    onOpenPaywall: () -> Unit = {},
    viewModel: FinancialAdvisorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var inputText by remember { mutableStateOf("") }
    val palette = AppTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.background)
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 12.dp)
            .verticalScroll(scrollState)
    ) {
        Eyebrow("MAX  ·  AI ADVISOR", color = palette.gilt)
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Financial Advisor",
            style = MaterialTheme.typography.displayLarge,
            color = palette.textPrimary
        )
        Spacer(Modifier.height(8.dp))
        GiltRule(width = 40.dp)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Grounded economic guidance for Need vs Want decisions.",
            style = MaterialTheme.typography.bodyLarge,
            color = palette.textSecondary
        )

        Spacer(Modifier.height(22.dp))

        if (!uiState.hasMaxAccess) {
            MaxLockedGate(onOpenPaywall = onOpenPaywall)
            return@Column
        }

        // ── Unlocked Max content ──────────────────────────────────────────
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = palette.surfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, palette.gold.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SOURCE OF TRUTH",
                    style = MaterialTheme.typography.labelSmall,
                    color = palette.marketGreen
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = uiState.sourceOfTruthTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = palette.textPrimary
                )
                Text(
                    text = "Recommendations cite your economic study notebooks.",
                    style = MaterialTheme.typography.bodySmall,
                    color = palette.textSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        uiState.insight?.let { insight ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = palette.surfaceCard,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (insight.isWarning) palette.crimson.copy(alpha = 0.5f)
                    else palette.marketGreen.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (insight.isWarning) "ADVISOR ALERT" else "RECOMMENDATION",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (insight.isWarning) palette.crimson else palette.marketGreen
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = insight.headline,
                        style = MaterialTheme.typography.titleLarge,
                        color = palette.textPrimary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = insight.advice,
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textPrimary
                    )
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = palette.gold.copy(alpha = 0.3f))
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "CITATION",
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.gold
                    )
                    Text(
                        text = insight.citation.title,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = palette.textPrimary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = insight.citation.section,
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.textSecondary
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = palette.surfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, palette.divider),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ASK YOUR ADVISOR",
                    style = MaterialTheme.typography.labelSmall,
                    color = palette.gold
                )
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.chatMessages.forEach { msg -> ChatBubble(message = msg) }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    QuickChip("Want today?") { viewModel.sendUserQuery("Can I buy a Want item today?") }
                    QuickChip("Overspend") { viewModel.sendUserQuery("What is my overspend status?") }
                    QuickChip("Need ratio") { viewModel.sendUserQuery("How is my Need to Want ratio?") }
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                "Ask your Advisor…",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendUserQuery(inputText)
                                inputText = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(palette.marketGreen)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = palette.surfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, palette.divider),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ECONOMIC STUDY NOTEBOOKS",
                    style = MaterialTheme.typography.labelSmall,
                    color = palette.gold
                )
                Spacer(Modifier.height(8.dp))
                StudyTopicRow("Notebook #1", "Budgetary equilibrium & Need/Want ratio")
                StudyTopicRow("Notebook #2", "Real-time transaction behavioral control")
                StudyTopicRow("Notebook #3", "Impulse recovery & compensatory sinking")
            }
        }
    }
}

@Composable
private fun MaxLockedGate(onOpenPaywall: () -> Unit) {
    val palette = AppTheme.colors
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = palette.surfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, palette.gold.copy(alpha = 0.45f)),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(palette.crimson.copy(alpha = 0.10f), RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = palette.crimson,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Surface(
                color = palette.crimson.copy(alpha = 0.10f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = palette.crimson,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "MAX TIER  ·  LOCKED",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = palette.crimson
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = "Unlock your AI Financial Advisor",
                style = MaterialTheme.typography.headlineSmall,
                color = palette.textPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Max Tier enables conversational budget analysis grounded in economic study notebooks — only for subscribers.",
                style = MaterialTheme.typography.bodyMedium,
                color = palette.textSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(22.dp))
            GiltButton(
                onClick = onOpenPaywall,
                text = "View Pro & Max plans",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Free users keep Log, Summary, and History. Advisor is Max-only.",
                style = MaterialTheme.typography.bodySmall,
                color = palette.textMuted,
                textAlign = TextAlign.Center
            )
        }
    }

    Spacer(Modifier.height(18.dp))

    // Soft teaser list (not interactive)
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = palette.inkElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, palette.inkDivider),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "INCLUDED WITH MAX",
                style = MaterialTheme.typography.labelSmall,
                color = palette.textMuted
            )
            Spacer(Modifier.height(10.dp))
            LockedFeatureRow("Real-time Need vs Want coaching")
            LockedFeatureRow("Overspend recovery prompts")
            LockedFeatureRow("Cited notebook recommendations")
        }
    }
}

@Composable
private fun LockedFeatureRow(text: String) {
    val palette = AppTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(
            Icons.Default.Lock,
            contentDescription = null,
            tint = palette.gold.copy(alpha = 0.7f),
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = palette.textSecondary)
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
            color = if (isUser) palette.marketGreen.copy(alpha = 0.15f) else palette.surfaceRaised,
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = if (isUser) "YOU" else "ADVISOR",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = if (isUser) palette.marketGreen else palette.gold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodySmall,
                    color = palette.textPrimary
                )
                message.citation?.let { cit ->
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Source: ${cit.title}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = palette.textSecondary
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
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(1.dp, palette.gold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp),
            color = palette.textPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun StudyTopicRow(title: String, description: String) {
    val palette = AppTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(palette.marketGreen)
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall, color = palette.textPrimary)
            Text(description, style = MaterialTheme.typography.bodySmall, color = palette.textSecondary)
        }
    }
}
