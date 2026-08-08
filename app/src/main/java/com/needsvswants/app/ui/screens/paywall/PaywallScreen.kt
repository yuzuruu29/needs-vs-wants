package com.needsvswants.app.ui.screens.paywall

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.ui.screens.auth.AuthViewModel
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.Eyebrow
import com.needsvswants.app.ui.theme.GiltButton
import com.needsvswants.app.ui.theme.GiltRule
import com.needsvswants.app.ui.theme.MembershipPlan
import com.needsvswants.app.ui.theme.Motion
import com.needsvswants.app.ui.theme.NeedWantSealMark
import com.needsvswants.app.ui.theme.PaywallNoticeSurface
import com.needsvswants.app.ui.theme.PaywallType
import com.needsvswants.app.ui.theme.PlanTierCard
import com.needsvswants.app.ui.theme.TrialTimelineCard
import com.needsvswants.app.ui.theme.rememberAppHaptics
import com.needsvswants.app.ui.theme.themedInkWash
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay

/**
 * Pro/Max membership desk.
 *
 * Ledger / supermarket-receipt language aligned with website `#pro-pricing` (D55).
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
    val haptics = rememberAppHaptics()
    val isPro by viewModel.isPro.collectAsStateWithLifecycle()
    val hasMaxAccess by viewModel.hasMaxAccess.collectAsStateWithLifecycle()
    val busy by viewModel.busy.collectAsStateWithLifecycle()
    val lastResult by viewModel.lastResult.collectAsStateWithLifecycle()
    val checkoutSync by viewModel.checkoutSyncState.collectAsStateWithLifecycle()
    val isSignedIn by viewModel.isSignedIn.collectAsStateWithLifecycle()
    val signedInEmail by viewModel.signedInEmail.collectAsStateWithLifecycle()
    val pending by viewModel.pendingPurchase.collectAsStateWithLifecycle()
    val needsSignInForPurchase by viewModel.needsSignInForPurchase.collectAsStateWithLifecycle()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var selected by remember {
        mutableStateOf(
            when {
                hasMaxAccess -> MembershipPlan.Max
                isPro -> MembershipPlan.Pro
                else -> MembershipPlan.Pro
            }
        )
    }

    LaunchedEffect(lastResult) {
        when (val r = lastResult) {
            is BillingResult.OpenCheckout -> {
                runCatching {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(r.approvalUrl)).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                    )
                }
                // Keep a short "opened checkout" state, then clear so return can refresh.
                delay(1500)
                viewModel.consumeResult()
            }
            BillingResult.Success -> {
                haptics.success()
                delay(3000)
                viewModel.consumeResult()
            }
            BillingResult.Pending -> {
                delay(3000)
                viewModel.consumeResult()
            }
            // Failed / Unavailable persist until the user acts (retry, plan switch, or close).
            is BillingResult.Failed, BillingResult.Unavailable -> Unit
            null -> Unit
        }
    }

    // After Google succeeds for a pending purchase, finish trial/upgrade.
    // `lastResult == null` gates re-entry: a Failed/Unavailable result with a
    // still-pending intent (e.g. the Edge Function POST timed out after the
    // server already created the subscription) must NOT re-fire the pipeline on
    // recomposition / rotation, or the user would be offered a second approval
    // URL. Retry is always explicit ("Try PayPal again" / CTA re-tap), never
    // automatic; those paths call subscribePro/subscribeMax directly.
    LaunchedEffect(isSignedIn, pending) {
        if (isSignedIn && pending != PendingPurchase.None && lastResult == null) {
            viewModel.onSignedInForPurchase()
        }
    }

    // Returning from the checkout browser: re-fetch entitlement.
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.onReturnFromCheckout()
        }
    }

    fun closeFree() {
        viewModel.cancelPendingSignIn()
        onClose()
    }

    fun selectPlan(plan: MembershipPlan) {
        if (selected != plan) {
            selected = plan
            haptics.tick()
            viewModel.consumeResult()
            if (pending != PendingPurchase.None) {
                if (isSignedIn) {
                    // A deferred purchase must never cross a plan change: the
                    // user re-taps the CTA to start fresh for the new plan.
                    viewModel.cancelPendingSignIn()
                } else {
                    // Signed out: re-assert the deferred intent for the NEW
                    // selection so sign-in completes the plan the user just
                    // picked (subscribeX just sets pending when signed out).
                    // Free needs no sign-in — drop the intent entirely.
                    when (plan) {
                        MembershipPlan.Pro -> viewModel.subscribePro()
                        MembershipPlan.Max -> viewModel.subscribeMax()
                        MembershipPlan.Free -> viewModel.cancelPendingSignIn()
                    }
                }
            }
        }
    }

    fun retryPayPal() {
        when (selected) {
            MembershipPlan.Pro -> viewModel.subscribePro()
            MembershipPlan.Max -> viewModel.subscribeMax()
            MembershipPlan.Free -> Unit
        }
    }

    val ctaEnabled = !busy && !authState.busy
    val retryEnabled = !busy && when (selected) {
        MembershipPlan.Free -> false
        MembershipPlan.Pro -> !isPro
        MembershipPlan.Max -> !hasMaxAccess
    }
    val primaryLabel = when (selected) {
        MembershipPlan.Free -> "Continue free"
        MembershipPlan.Pro -> if (isPro) "You're on Pro" else "Continue with PayMongo · Pro"
        MembershipPlan.Max -> if (hasMaxAccess) "You're on Max" else "Continue with PayMongo · Max"
    }
    val primaryEnabled = when (selected) {
        MembershipPlan.Free -> true
        MembershipPlan.Pro -> ctaEnabled && !isPro
        MembershipPlan.Max -> ctaEnabled && !hasMaxAccess
    }
    val footerNote = when (selected) {
        MembershipPlan.Free -> "No account. No network. 20-sheet · 35-day trainer."
        MembershipPlan.Pro, MembershipPlan.Max ->
            "One-time payment via GCash or card. You pay each month when ready — access ends on your expiry date. No auto-charge."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themedInkWash())
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 18.dp, bottom = 12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Eyebrow("MEMBERSHIP", color = palette.gilt)
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { closeFree() }) {
                        Text(
                            "Continue free",
                            style = PaywallType.meta,
                            color = palette.textMuted
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                NeedWantSealMark()
                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Choose your sheet.",
                    style = PaywallType.screenHero,
                    color = palette.textPrimary,
                    maxLines = 2,
                    softWrap = true,
                    overflow = TextOverflow.Clip
                )
                Spacer(Modifier.height(8.dp))
                GiltRule(width = 40.dp)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Free is the 20-sheet, 35-day trainer. Pro lifts the caps. Max adds the AI Advisor with cited notebooks.",
                    style = PaywallType.screenLede,
                    color = palette.textSecondary,
                    softWrap = true
                )

                Spacer(Modifier.height(20.dp))

                // Free
                PlanTierCard(
                    plan = MembershipPlan.Free,
                    selected = selected == MembershipPlan.Free,
                    onClick = { selectPlan(MembershipPlan.Free) },
                    eyebrow = "Free",
                    title = "Trainer",
                    tag = "On this device",
                    price = "₱0",
                    priceSuffix = "forever",
                    subtitle = "Honest daily training. No account required.",
                    features = listOf(
                        "20 entries per sheet" to false,
                        "35-day retention window" to false,
                        "Daily budget meter" to false,
                        "Four appearance themes" to false
                    ),
                    statusNote = if (!isPro) "Active on this device" else null
                )

                Spacer(Modifier.height(12.dp))

                // Pro
                PlanTierCard(
                    plan = MembershipPlan.Pro,
                    selected = selected == MembershipPlan.Pro,
                    onClick = { selectPlan(MembershipPlan.Pro) },
                    eyebrow = "Pro",
                    title = "Unlimited",
                    tag = "One-time ₱199",
                    price = "₱199",
                    priceSuffix = "/ mo · renewed manually",
                    subtitle = "Unlimited sheets, full history, full period analytics.",
                    features = listOf(
                        "Unlimited entries per log sheet" to true,
                        "Lifetime history retention" to true,
                        "Full period summary analytics" to false,
                        "Everything in Free" to false
                    ),
                    statusNote = if (isPro && !hasMaxAccess) "You're on Pro" else null
                )

                Spacer(Modifier.height(12.dp))

                // Max — short title so it never clips on narrow screens
                PlanTierCard(
                    plan = MembershipPlan.Max,
                    selected = selected == MembershipPlan.Max,
                    onClick = { selectPlan(MembershipPlan.Max) },
                    eyebrow = "Max",
                    title = "AI Advisor",
                    tag = "Includes Pro",
                    price = "₱399",
                    priceSuffix = "/ mo · renewed manually",
                    subtitle = "Everything in Pro, plus cited AI coaching from economic study notebooks.",
                    features = listOf(
                        "Everything in Pro" to true,
                        "AI Financial Advisor with citations" to true,
                        "Footnotes from study notebooks" to false,
                        "Overspend recovery coaching" to false
                    ),
                    statusNote = if (hasMaxAccess) "You're on Max" else null
                )

                Spacer(Modifier.height(14.dp))

                AnimatedVisibility(
                    visible = selected == MembershipPlan.Pro || selected == MembershipPlan.Max,
                    enter = fadeIn(Motion.entrance()) + slideInVertically(Motion.entrance()) { it / 8 },
                    exit = fadeOut(Motion.feedback())
                ) {
                    TrialTimelineCard(forMax = selected == MembershipPlan.Max)
                }

                if (isSignedIn) {
                    Spacer(Modifier.height(14.dp))
                    PaywallNoticeSurface(accent = palette.gold) {
                        Text(
                            "Signed in as ${signedInEmail ?: "you"}",
                            style = PaywallType.planFeatureEmph,
                            color = palette.textPrimary,
                            maxLines = 2
                        )
                        Text(
                            "Tap continue to open PayMongo.",
                            style = PaywallType.planSub,
                            color = palette.textMuted
                        )
                    }
                }

                if (needsSignInForPurchase && !isSignedIn) {
                    Spacer(Modifier.height(14.dp))
                    PaywallNoticeSurface(accent = palette.crimson) {
                        Text(
                            "Step 1 of 2 · Sign in with Google",
                            style = PaywallType.planFeatureEmph,
                            color = palette.textPrimary
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Sign in with Google to start PayMongo checkout. Free use never needs an account.",
                            style = PaywallType.planSub,
                            color = palette.textMuted
                        )
                        Spacer(Modifier.height(12.dp))
                        GiltButton(
                            onClick = { authViewModel.signInWithGoogle(context) },
                            text = if (authState.busy) "Signing in…" else "Continue with Google",
                            enabled = !authState.busy && authState.googleAvailable && !busy,
                            modifier = Modifier.fillMaxWidth(),
                            height = 48.dp
                        )
                        TextButton(
                            onClick = { viewModel.cancelPendingSignIn() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Cancel",
                                style = PaywallType.meta,
                                color = palette.textMuted
                            )
                        }
                        authState.error?.let { msg ->
                            Spacer(Modifier.height(4.dp))
                            Text(msg, style = PaywallType.stickyNote, color = palette.crimson)
                        }
                        if (!authState.googleAvailable) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Google Sign-In is not configured on this build.",
                                style = PaywallType.stickyNote,
                                color = palette.textMuted
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                when (val r = lastResult) {
                    BillingResult.Unavailable -> StatusText(
                        "PayMongo isn't configured yet. Try again.",
                        color = palette.textMuted
                    )
                    BillingResult.Pending -> StatusText(
                        "Your payment is processing…",
                        color = palette.gilt
                    )
                    is BillingResult.OpenCheckout -> StatusText(
                        "Opening PayMongo… complete checkout, then return here.",
                        color = palette.gilt
                    )
                    BillingResult.Success -> when {
                        hasMaxAccess -> StatusText(
                            "Welcome to Max.",
                            color = palette.marketGreen
                        )
                        isPro -> StatusText(
                            "Welcome to Pro.",
                            color = palette.marketGreen
                        )
                        // Free here means a plain restore with no grant; the
                        // checkout-return states below carry the messaging.
                        else -> Unit
                    }
                    is BillingResult.Failed -> {
                        StatusText(
                            r.reason ?: "Payment didn't go through. Try again.",
                            color = palette.crimson
                        )
                        Spacer(Modifier.height(4.dp))
                        RetryPayPalButton(
                            onRetry = { retryPayPal() },
                            enabled = retryEnabled,
                            color = palette.textMuted
                        )
                    }
                    null -> Unit
                }

                // Checkout-return sync lifecycle (durable flag path): "Still
                // unlocking — retrying…" during backoff, then the
                // payment-recorded message instead of silent free after the
                // retry schedule exhausted. Independent of lastResult so the
                // exhausted message persists across result consumption.
                when (checkoutSync) {
                    CheckoutSyncState.Syncing -> StatusText(
                        "Still unlocking — retrying…",
                        color = palette.gilt
                    )
                    CheckoutSyncState.Exhausted -> StatusText(
                        "Payment recorded — tap Restore, or wait a moment.",
                        color = palette.textMuted
                    )
                    CheckoutSyncState.Idle -> Unit
                }

                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { viewModel.restore() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !busy
                ) {
                    Text(
                        "Restore purchases",
                        style = PaywallType.meta,
                        color = palette.textMuted
                    )
                }

                Spacer(Modifier.height(72.dp))
            }

            // Sticky CTA bar
            Surface(
                color = palette.surfaceCard,
                shadowElevation = 8.dp,
                tonalElevation = 0.dp,
                shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .height(3.dp)
                            .fillMaxWidth(0.12f)
                            .background(palette.gold.copy(alpha = 0.55f), RoundedCornerShape(2.dp))
                    )
                    GiltButton(
                        onClick = {
                            when (selected) {
                                MembershipPlan.Free -> closeFree()
                                MembershipPlan.Pro -> if (!isPro) viewModel.subscribePro()
                                MembershipPlan.Max -> if (!hasMaxAccess) viewModel.subscribeMax()
                            }
                        },
                        text = primaryLabel,
                        enabled = primaryEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 54.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = footerNote,
                        style = PaywallType.stickyNote,
                        color = palette.textMuted,
                        textAlign = TextAlign.Center,
                        softWrap = true
                    )
                    if (selected != MembershipPlan.Free) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = when {
                                needsSignInForPurchase && !isSignedIn ->
                                    "First Google, then PayMongo opens in your browser."
                                isPro && selected == MembershipPlan.Pro && !hasMaxAccess ->
                                    "You're on Pro. Select Max for the AI Advisor."
                                else ->
                                    "Browse free anytime. Sign-in only when you start Pro or Max."
                            },
                            style = PaywallType.stickyNote,
                            color = palette.textMuted.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            softWrap = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusText(text: String, color: Color) {
    Text(
        text,
        style = PaywallType.stickyNote,
        color = color,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        softWrap = true
    )
}

@Composable
private fun RetryPayPalButton(onRetry: () -> Unit, enabled: Boolean, color: Color) {
    TextButton(
        onClick = onRetry,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled
    ) {
        Text(
            "Try PayMongo again",
            style = PaywallType.meta,
            color = color
        )
    }
}
