package com.needsvswants.app.ui.screens.paywall

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.ui.screens.auth.AuthViewModel
import com.needsvswants.app.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Pro/Max paywall.
 *
 * Google Sign-In is **not** offered while browsing free. It appears only after the user
 * taps Start trial or Upgrade while signed out; after sign-in, the pending purchase continues.
 */
@Composable
fun PaywallScreen(
    onClose: () -> Unit,
    viewModel: PaywallViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val palette = AppTheme.colors
    val isPro by viewModel.isPro.collectAsStateWithLifecycle()
    val hasMaxAccess by viewModel.hasMaxAccess.collectAsStateWithLifecycle()
    val busy by viewModel.busy.collectAsStateWithLifecycle()
    val lastResult by viewModel.lastResult.collectAsStateWithLifecycle()
    val isSignedIn by viewModel.isSignedIn.collectAsStateWithLifecycle()
    val signedInEmail by viewModel.signedInEmail.collectAsStateWithLifecycle()
    val pending by viewModel.pendingPurchase.collectAsStateWithLifecycle()
    val needsSignInForPurchase by viewModel.needsSignInForPurchase.collectAsStateWithLifecycle()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(lastResult) {
        if (lastResult != null) {
            delay(3000)
            viewModel.consumeResult()
        }
    }

    // After Google succeeds for a pending purchase, finish trial/upgrade.
    LaunchedEffect(isSignedIn, pending) {
        if (isSignedIn && pending != PendingPurchase.None) {
            viewModel.onSignedInForPurchase()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themedInkWash())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
                .padding(top = 20.dp, bottom = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Eyebrow("PRO  ·  MAX", color = palette.gilt)
                Spacer(Modifier.weight(1f))
                TextButton(onClick = {
                    viewModel.cancelPendingSignIn()
                    onClose()
                }) {
                    Text(
                        "Continue free",
                        style = MaterialTheme.typography.labelLarge,
                        color = palette.textMuted
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(palette.crimson.copy(alpha = 0.12f), RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = palette.crimson,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(Modifier.height(14.dp))

            Text(
                "Upgrade your spending trainer",
                style = MaterialTheme.typography.displayLarge,
                color = palette.textPrimary
            )
            Spacer(Modifier.height(6.dp))
            GiltRule(width = 40.dp)
            Spacer(Modifier.height(14.dp))
            Text(
                "Go Pro for unlimited logging and full history. Go Max for the AI Financial Advisor.",
                style = MaterialTheme.typography.bodyLarge,
                color = palette.textSecondary
            )

            Spacer(Modifier.height(22.dp))

            ProBenefit("Unlimited diary", "No more 20-entry cap on your log.")
            ProBenefit("Keep all history", "Free tier only retains the last 35 days.")
            ProBenefit("Full period analytics", "Summary across all time, not just this week.")
            ProBenefit("Max · AI Advisor", "Conversational coaching with cited economic studies.")

            Spacer(Modifier.height(18.dp))

            // Account status only — no free-standing Google button.
            if (isSignedIn) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = palette.inkElevated,
                    border = BorderStroke(1.dp, palette.gold.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            signedInEmail ?: "Signed in",
                            style = MaterialTheme.typography.bodyMedium,
                            color = palette.textPrimary,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2
                        )
                        Text(
                            "Purchases will apply to this account.",
                            style = MaterialTheme.typography.bodySmall,
                            color = palette.textMuted
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))
            }

            // Google only after user chose trial/upgrade while signed out.
            if (needsSignInForPurchase && !isSignedIn) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = palette.crimson.copy(alpha = 0.06f),
                    border = BorderStroke(1.dp, palette.crimson.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            when (pending) {
                                PendingPurchase.ProTrial -> "Sign in to start your Pro trial"
                                PendingPurchase.MaxUpgrade -> "Sign in to upgrade to Max"
                                else -> "Sign in to continue"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = palette.textPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Google is only used when you subscribe — free use stays private on this device.",
                            style = MaterialTheme.typography.bodySmall,
                            color = palette.textMuted
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { authViewModel.signInWithGoogle(context) },
                            enabled = !authState.busy && authState.googleAvailable && !busy,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = palette.crimson),
                            modifier = Modifier.fillMaxWidth().heightIn(min = 46.dp)
                        ) {
                            Text(
                                if (authState.busy) "Signing in…" else "Continue with Google",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        TextButton(
                            onClick = { viewModel.cancelPendingSignIn() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Cancel",
                                style = MaterialTheme.typography.labelLarge,
                                color = palette.textMuted
                            )
                        }
                        authState.error?.let { msg ->
                            Spacer(Modifier.height(6.dp))
                            Text(msg, style = MaterialTheme.typography.labelSmall, color = palette.crimson)
                        }
                        if (!authState.googleAvailable) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Google Sign-In is not configured on this build.",
                                style = MaterialTheme.typography.labelSmall,
                                color = palette.textMuted
                            )
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))
            }

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = palette.surfaceCard,
                border = BorderStroke(1.dp, palette.gold.copy(alpha = 0.5f)),
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Eyebrow("3-DAY FREE TRIAL", color = palette.gilt)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Start free for 3 days, then a simple monthly price.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textSecondary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "≈ US$4.99 / month",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = palette.textPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Cancel anytime.",
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.textMuted
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            GiltButton(
                onClick = { if (!isPro) viewModel.startTrial() },
                text = if (isPro) "You're Pro" else "Start Pro 3-day free trial (₱249/mo)",
                enabled = !busy && !isPro && !authState.busy
            )

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { if (!hasMaxAccess) viewModel.upgrade() },
                enabled = !busy && !hasMaxAccess && !authState.busy,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, palette.gold),
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
            ) {
                Text(
                    "Upgrade to Max Tier (₱499/mo · AI Advisor)",
                    color = palette.textPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
            }

            Spacer(Modifier.height(14.dp))

            when (lastResult) {
                BillingResult.Unavailable -> StatusText(
                    "Play billing isn't configured on this build yet.",
                    color = palette.textMuted
                )
                BillingResult.Pending -> StatusText("Your payment is processing…", color = palette.gilt)
                BillingResult.Success -> StatusText("Welcome to Pro!", color = palette.marketGreen)
                BillingResult.Failed -> StatusText("Payment didn't go through. Try again.", color = palette.crimson)
                null -> StatusText(
                    when {
                        needsSignInForPurchase && !isSignedIn ->
                            "Sign in with Google to finish your upgrade."
                        isPro && !hasMaxAccess ->
                            "You're on Pro — upgrade to Max for the AI Advisor."
                        isSignedIn ->
                            "Tap start to begin the 3-day free trial."
                        else ->
                            "Browse free. Google sign-in only appears when you start Pro or Max."
                    },
                    color = palette.textMuted
                )
            }

            Spacer(Modifier.height(18.dp))
            TextButton(
                onClick = {
                    viewModel.cancelPendingSignIn()
                    onClose()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Continue with free plan",
                    style = MaterialTheme.typography.titleSmall,
                    color = palette.textSecondary
                )
            }
        }
    }
}

@Composable
private fun ProBenefit(title: String, detail: String) {
    val palette = AppTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(palette.gold.copy(alpha = 0.16f), RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(palette.gold, RoundedCornerShape(3.dp))
            )
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                color = palette.textPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                detail,
                style = MaterialTheme.typography.bodySmall,
                color = palette.textMuted
            )
        }
    }
}

@Composable
private fun StatusText(text: String, color: Color) {
    Text(
        text,
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.2.sp),
        color = color
    )
}
