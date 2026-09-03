package com.needsvswants.app.ui.screens.paywall

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.needsvswants.app.data.billing.BillingPeriod
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.data.billing.PaymentProvider
import com.needsvswants.app.ui.theme.AppShapes
import com.needsvswants.app.ui.screens.auth.AuthViewModel
import com.needsvswants.app.ui.screens.auth.EmailOtpState
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.Eyebrow
import com.needsvswants.app.ui.theme.GiltButton
import com.needsvswants.app.ui.theme.GiltRule
import com.needsvswants.app.ui.theme.LedgerField
import com.needsvswants.app.ui.theme.MembershipPlan
import com.needsvswants.app.ui.theme.MembershipSealBadge
import com.needsvswants.app.ui.theme.Motion
import com.needsvswants.app.ui.theme.NeedWantSealMark
import com.needsvswants.app.ui.theme.origamiUnfold
import com.needsvswants.app.ui.theme.PaywallCopy
import com.needsvswants.app.ui.theme.PaywallNoticeSurface
import com.needsvswants.app.ui.theme.PaywallType
import com.needsvswants.app.ui.theme.PlanTierCard
import com.needsvswants.app.ui.theme.TrialTimelineCard
import com.needsvswants.app.ui.theme.unfoldSealProgress
import com.needsvswants.app.ui.theme.unfoldSheetProgress
import com.needsvswants.app.ui.theme.rememberAppHaptics
import com.needsvswants.app.ui.theme.themedInkWash
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.lerp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val emailOtp by authViewModel.emailOtp.collectAsStateWithLifecycle()
    val selectedProvider by viewModel.selectedProvider.collectAsStateWithLifecycle()
    val selectedPeriod by viewModel.selectedPeriod.collectAsStateWithLifecycle()
    val payPalAvailable = viewModel.payPalAvailable
    val payMongoAvailable = viewModel.payMongoAvailable
    val context = LocalContext.current
    val isPlay = viewModel.isPlayStoreBuild
    val activity = context as? android.app.Activity

    LaunchedEffect(activity) {
        viewModel.setActivity(activity)
    }

    val playController = viewModel.playBillingController
    val proMonthlyPricePlay by playController?.proMonthlyPrice?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(null) }
    val proAnnualPricePlay by playController?.proAnnualPrice?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(null) }
    val maxMonthlyPricePlay by playController?.maxMonthlyPrice?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(null) }
    val maxAnnualPricePlay by playController?.maxAnnualPrice?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(null) }

    /** The provider the CTA/footer/timeline actually describe: the user's
     *  choice when available, otherwise the one provider this build has
     *  (PayMongo wins the fully-unconfigured fallback, matching the
     *  pre-selector default). */
    val effectiveProvider = when {
        isPlay -> PaymentProvider.GOOGLE_PLAY
        selectedProvider == PaymentProvider.PAYPAL && !payPalAvailable -> PaymentProvider.PAYMONGO
        selectedProvider == PaymentProvider.PAYMONGO && !payMongoAvailable -> PaymentProvider.PAYPAL
        !payPalAvailable && payMongoAvailable -> PaymentProvider.PAYMONGO
        !payMongoAvailable && payPalAvailable -> PaymentProvider.PAYPAL
        else -> selectedProvider
    }

    /** Billing cycle shorthand for the copy matrix below (₱49/₱99 monthly, ₱490/₱990 annual). */
    val isAnnual = selectedPeriod == BillingPeriod.ANNUAL
    val proTag = when {
        isPlay -> if (isAnnual) "Billed yearly via Google Play" else "Auto-renews via Google Play"
        effectiveProvider == PaymentProvider.PAYPAL && !isAnnual -> "3-day free trial"
        effectiveProvider == PaymentProvider.PAYPAL -> "Billed yearly"
        else -> "One-time ${PaywallCopy.proPrice(isAnnual)}"
    }
    val proPriceDisplay = when {
        isPlay && isAnnual && proAnnualPricePlay != null -> proAnnualPricePlay!!
        isPlay && !isAnnual && proMonthlyPricePlay != null -> proMonthlyPricePlay!!
        else -> PaywallCopy.proPrice(isAnnual)
    }
    val maxPriceDisplay = when {
        isPlay && isAnnual && maxAnnualPricePlay != null -> maxAnnualPricePlay!!
        isPlay && !isAnnual && maxMonthlyPricePlay != null -> maxMonthlyPricePlay!!
        else -> PaywallCopy.maxPrice(isAnnual)
    }
    val proPriceSuffix = when {
        isPlay -> if (isAnnual) "/ yr" else "/ mo"
        effectiveProvider == PaymentProvider.PAYPAL -> if (isAnnual) "/ yr" else "/ mo after trial"
        effectiveProvider == PaymentProvider.PAYMONGO -> if (isAnnual) "/ yr · renewed manually" else "/ mo · renewed manually"
        else -> if (isAnnual) "/ yr" else "/ mo"
    }
    val maxPriceSuffix = when {
        isPlay -> if (isAnnual) "/ yr" else "/ mo"
        effectiveProvider == PaymentProvider.PAYPAL -> if (isAnnual) "/ yr" else "/ mo"
        effectiveProvider == PaymentProvider.PAYMONGO -> if (isAnnual) "/ yr · renewed manually" else "/ mo · renewed manually"
        else -> if (isAnnual) "/ yr" else "/ mo"
    }

    var selected by remember {
        mutableStateOf(
            when {
                hasMaxAccess -> MembershipPlan.Max
                isPro -> MembershipPlan.Pro
                else -> MembershipPlan.Pro
            }
        )
    }

    // One-shot activation seal (D136): owns the Success moment for a real grant.
    var showActivation by remember { mutableStateOf(false) }
    var activationCopy by remember { mutableStateOf<ActivationCopy?>(null) }

    LaunchedEffect(lastResult) {
        when (val r = lastResult) {
            is BillingResult.OpenCheckout -> {
                try {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(r.approvalUrl)).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                    )
                } catch (t: Throwable) {
                    // No browser / malformed approval URL: surface the failure
                    // and drop the durable pending flag (no checkout actually
                    // started) instead of silently swallowing it — the user
                    // gets a retryable error, not a silent no-op.
                    viewModel.reportCheckoutOpenFailure()
                    return@LaunchedEffect
                }
                // Keep a short "opened checkout" state, then clear so return can refresh.
                delay(1500)
                viewModel.consumeResult()
            }
            BillingResult.Success -> {
                // Owned by the activation seal (D136): a granted Success must NOT
                // auto-clear while the celebration can show. Free restore (no
                // grant) clears quietly via the effect below.
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

    // Activation seal (D136). Keyed on entitlement state too: a Success can
    // arrive a frame before isPro/hasMaxAccess updates — when they flip, this
    // effect re-runs and the dialog shows. Re-running cancels the previous
    // block, so a pending free-restore auto-clear never races a late grant.
    LaunchedEffect(lastResult, isPro, hasMaxAccess) {
        if (lastResult is BillingResult.Success) {
            val copy = ActivationCopy.forEntitlement(isPro, hasMaxAccess)
            if (copy != null) {
                if (!showActivation) {
                    activationCopy = copy
                    showActivation = true
                }
            } else {
                // Free restore — no grant. Clear quietly so the strip does not stick.
                delay(if (Motion.enabled) 2500L else 400L)
                viewModel.consumeResult()
            }
        }
    }

    // After Max activates, keep the selected plan + CTA label in sync ("You're on Max").
    LaunchedEffect(hasMaxAccess) {
        if (hasMaxAccess) selected = MembershipPlan.Max
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

    fun selectProvider(provider: PaymentProvider) {
        if (provider != selectedProvider) {
            haptics.tick()
            viewModel.selectProvider(provider)
        }
    }

    fun selectPeriod(period: BillingPeriod) {
        if (period != selectedPeriod) {
            haptics.tick()
            viewModel.selectPeriod(period)
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
        MembershipPlan.Pro -> if (isPro) "You're on Pro" else when (effectiveProvider) {
            PaymentProvider.GOOGLE_PLAY -> "Subscribe on Google Play"
            PaymentProvider.PAYPAL -> "Continue with PayPal"
            PaymentProvider.PAYMONGO -> "Continue with PayMongo"
        }
        MembershipPlan.Max -> if (hasMaxAccess) "You're on Max" else when (effectiveProvider) {
            PaymentProvider.GOOGLE_PLAY -> "Subscribe on Google Play"
            PaymentProvider.PAYPAL -> "Continue with PayPal"
            PaymentProvider.PAYMONGO -> "Continue with PayMongo"
        }
    }
    val primaryEnabled = when (selected) {
        MembershipPlan.Free -> true
        MembershipPlan.Pro -> ctaEnabled && !isPro
        MembershipPlan.Max -> ctaEnabled && !hasMaxAccess
    }
    val footerNote = when (selected) {
        MembershipPlan.Free -> "No account. Diary stays on this device. 20 entries per sheet · 30-day window."
        MembershipPlan.Pro -> when (effectiveProvider) {
            PaymentProvider.GOOGLE_PLAY ->
                "Subscribed through Google Play. Auto-renews until cancelled. Cancel anytime in Google Play Subscriptions."
            PaymentProvider.PAYPAL -> PaywallCopy.paypalProFooter(isAnnual)
            PaymentProvider.PAYMONGO -> PaywallCopy.PAYMONGO_FOOTER
        }
        MembershipPlan.Max -> when (effectiveProvider) {
            PaymentProvider.GOOGLE_PLAY ->
                "Subscribed through Google Play. Auto-renews until cancelled. Cancel anytime in Google Play Subscriptions."
            PaymentProvider.PAYPAL -> PaywallCopy.paypalMaxFooter(isAnnual)
            PaymentProvider.PAYMONGO -> PaywallCopy.PAYMONGO_FOOTER
        }
    }
    /** Payment-method selector is meaningful only when both providers are configured. */
    val showProviderSelector = (selected == MembershipPlan.Pro || selected == MembershipPlan.Max) &&
        payPalAvailable && payMongoAvailable

    var backScale by remember { mutableFloatStateOf(1f) }
    var backAlpha by remember { mutableFloatStateOf(1f) }
    PredictiveBackHandler(enabled = true) {
        it.collect { event ->
            backScale = 1f - (event.progress * 0.08f)
            backAlpha = 1f - (event.progress * 0.3f)
        }
        closeFree()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = backScale
                scaleY = backScale
                alpha = backAlpha
            }
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
                Spacer(Modifier.height(14.dp))
                Text(
                    text = "Free keeps 20 entries per sheet over a 30-day window. Pro lifts the caps. Max adds the AI Financial Advisor with cited notebooks.",
                    style = PaywallType.screenLede,
                    color = palette.textSecondary,
                    softWrap = true
                )

                Spacer(Modifier.height(20.dp))

                // Max — the flagship (D191): featured treatment, first on the desk.
                // One hierarchy reads Max, Pro, Free instead of three equal cards.
                PlanTierCard(
                    plan = MembershipPlan.Max,
                    selected = selected == MembershipPlan.Max,
                    onClick = { selectPlan(MembershipPlan.Max) },
                    featured = true,
                    eyebrow = "Max",
                    title = "AI Advisor",
                    tag = "Includes Pro",
                    price = maxPriceDisplay,
                    priceSuffix = maxPriceSuffix,
                    subtitle = "Everything in Pro, plus cited AI coaching from economic study notebooks.",
                    features = listOf(
                        "Live insight card from your sealed ledger" to false,
                        "Pre-seal Want coach + hold suggestions" to false,
                        "3-day overspend recovery plans" to false,
                        "Grounded citations on every answer" to true
                    ),
                    statusNote = if (hasMaxAccess) "You're on Max" else null
                )

                Spacer(Modifier.height(12.dp))

                // Pro — the everyday unlimited plan.
                PlanTierCard(
                    plan = MembershipPlan.Pro,
                    selected = selected == MembershipPlan.Pro,
                    onClick = { selectPlan(MembershipPlan.Pro) },
                    eyebrow = "Pro",
                    title = "Unlimited",
                    tag = proTag,
                    price = proPriceDisplay,
                    priceSuffix = proPriceSuffix,
                    subtitle = "Unlimited sheets, full history, full period analytics.",
                    features = listOf(
                        "Unlimited entries per log sheet" to true,
                        "Receipt scanner & line-item sorter" to true,
                        "Lifetime history retention" to true,
                        "Full period summary analytics" to false
                    ),
                    statusNote = if (isPro && !hasMaxAccess) "You're on Pro" else null
                )

                Spacer(Modifier.height(12.dp))

                // Free — quiet compact card; the lede already explains the caps.
                PlanTierCard(
                    plan = MembershipPlan.Free,
                    selected = selected == MembershipPlan.Free,
                    onClick = { selectPlan(MembershipPlan.Free) },
                    compact = true,
                    eyebrow = "Free",
                    title = "Trainer",
                    tag = "On this device",
                    price = PaywallCopy.FREE_PRICE,
                    priceSuffix = "forever",
                    subtitle = "Honest daily training. No account required.",
                    features = listOf(
                        "20 entries per sheet" to false,
                        "30-day retention window" to false,
                        "Daily budget meter" to false
                    ),
                    statusNote = if (!isPro) "Active on this device" else null
                )

                Spacer(Modifier.height(12.dp))

                if (selected == MembershipPlan.Pro || selected == MembershipPlan.Max) {
                    BillingPeriodSelector(
                        period = selectedPeriod,
                        // PayPal annual needs configured plan ids; when missing,
                        // the Annual option is disabled instead of letting the
                        // checkout fail at runtime.
                        annualEnabled = viewModel.isAnnualAvailable(effectiveProvider),
                        onSelect = ::selectPeriod
                    )
                    Spacer(Modifier.height(12.dp))
                }

                if (showProviderSelector) {
                    PaymentMethodSelector(
                        provider = effectiveProvider,
                        onSelect = ::selectProvider,
                        forMax = selected == MembershipPlan.Max,
                        period = selectedPeriod
                    )
                    Spacer(Modifier.height(14.dp))
                }

                AnimatedVisibility(
                    visible = selected == MembershipPlan.Pro || selected == MembershipPlan.Max,
                    enter = fadeIn(Motion.entrance()),
                    exit = fadeOut(Motion.feedback())
                ) {
                    // Two-stage origami unfold (D195): the billing slip unfolds
                    // down from its top hinge, then the tier seal stamps home.
                    val slipKey = selected to effectiveProvider
                    val unfold = remember { Animatable(0f) }
                    LaunchedEffect(slipKey) {
                        if (Motion.enabled) {
                            unfold.snapTo(0f)
                            unfold.animateTo(1f, Motion.unfold())
                            haptics.seal()
                        } else {
                            unfold.snapTo(1f)
                        }
                    }
                    Column {
                        TrialTimelineCard(
                            forMax = selected == MembershipPlan.Max,
                            provider = effectiveProvider,
                            period = selectedPeriod,
                            modifier = Modifier.origamiUnfold(unfoldSheetProgress(unfold.value))
                        )
                        val sealProgress = unfoldSealProgress(unfold.value)
                        if (sealProgress > 0f) {
                            Spacer(Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        alpha = sealProgress
                                        val s = lerp(1.45f, 1f, sealProgress)
                                        scaleX = s
                                        scaleY = s
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                MembershipSealBadge(
                                    label = if (selected == MembershipPlan.Max) "MAX" else "PRO",
                                    size = 40.dp,
                                    crimsonRing = selected == MembershipPlan.Max
                                )
                            }
                        }
                    }
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
                            "Tap continue to open checkout.",
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
                            "Sign in with Google to start checkout. Free use never needs an account.",
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
                        if (authState.emailAvailable) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Can't use Google? Sign in with an email code",
                                style = PaywallType.stickyNote,
                                color = palette.crimson,
                                modifier = Modifier
                                    .clickable(
                                        enabled = !authState.busy,
                                        role = Role.Button
                                    ) { authViewModel.openEmailOtp() }
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                if (emailOtp.visible) {
                    EmailOtpDialog(
                        state = emailOtp,
                        onSend = authViewModel::sendEmailCode,
                        onVerify = authViewModel::verifyEmailCode,
                        onDismiss = authViewModel::dismissEmailOtp
                    )
                }

                Spacer(Modifier.height(12.dp))
                when (val r = lastResult) {
                    BillingResult.Unavailable -> StatusText(
                        "Checkout isn't configured yet. Try again.",
                        color = palette.textMuted
                    )
                    BillingResult.Pending -> StatusText(
                        "Your payment is processing…",
                        color = palette.gilt
                    )
                    is BillingResult.OpenCheckout -> StatusText(
                        "Opening checkout… complete it, then return here.",
                        color = palette.gilt
                    )
                    BillingResult.Success -> {
                        // Quiet strip only when the activation dialog is not on
                        // screen — the dialog carries the celebration itself.
                        val copy = ActivationCopy.forEntitlement(isPro, hasMaxAccess)
                        if (copy != null && !showActivation) {
                            StatusText(
                                ActivationCopy.quietStatusLine(copy.tier),
                                color = palette.marketGreen
                            )
                        }
                    }
                    is BillingResult.Failed -> {
                        StatusText(
                            r.reason ?: "Payment didn't go through. Try again.",
                            color = palette.crimson
                        )
                        Spacer(Modifier.height(4.dp))
                        RetryCheckoutButton(
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
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
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
                            .background(palette.gold.copy(alpha = 0.55f), AppShapes.r6)
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
                        // Context note only where it carries real information
                        // (D191 cut the third "browse free anytime" reassurance).
                        val contextNote = when {
                            needsSignInForPurchase && !isSignedIn ->
                                "First Google, then checkout opens in your browser."
                            isPro && selected == MembershipPlan.Pro && !hasMaxAccess ->
                                "You're on Pro. Select Max for the AI Financial Advisor."
                            else -> null
                        }
                        if (contextNote != null) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = contextNote,
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

    // One-shot activation seal (D136). Dismiss consumes the Success so the
    // paywall below shows the new card state (pills + disabled CTA labels).
    if (showActivation) {
        activationCopy?.let { copy ->
            ActivationSealDialog(
                copy = copy,
                onDismiss = {
                    showActivation = false
                    activationCopy = null
                    viewModel.consumeResult()
                }
            )
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
private fun RetryCheckoutButton(onRetry: () -> Unit, enabled: Boolean, color: Color) {
    TextButton(
        onClick = onRetry,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled
    ) {
        Text(
            "Try again",
            style = PaywallType.meta,
            color = color
        )
    }
}

/** Payment-method picker (PayPal vs PayMongo) for the selected paid plan. */
@Composable
private fun PaymentMethodSelector(
    provider: PaymentProvider,
    onSelect: (PaymentProvider) -> Unit,
    forMax: Boolean,
    period: BillingPeriod
) {
    val isAnnual = period == BillingPeriod.ANNUAL
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ProviderOption(
            title = "PayPal",
            detail = if (forMax) {
                PaywallCopy.paypalMaxDetail(isAnnual)
            } else {
                PaywallCopy.paypalProDetail(isAnnual)
            },
            selected = provider == PaymentProvider.PAYPAL,
            onClick = { onSelect(PaymentProvider.PAYPAL) }
        )
        ProviderOption(
            title = "PayMongo",
            detail = PaywallCopy.paymongoDetail(isAnnual),
            selected = provider == PaymentProvider.PAYMONGO,
            onClick = { onSelect(PaymentProvider.PAYMONGO) }
        )
    }
}

/** Monthly/Annual billing-period picker (12-for-10 annual price point). */
@Composable
private fun BillingPeriodSelector(
    period: BillingPeriod,
    onSelect: (BillingPeriod) -> Unit,
    annualEnabled: Boolean = true
) {
    val c = AppTheme.colors
    Surface(
        shape = AppShapes.r12,
        color = c.surfaceSunken,
        border = BorderStroke(1.dp, c.gold.copy(alpha = 0.28f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        // IntrinsicSize.Min keeps both options the same height so the selected
        // pill always fills the row, even when one detail wraps at large text scales.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            PeriodOption(
                title = "Monthly",
                detail = "flexible",
                selected = period == BillingPeriod.MONTHLY,
                onClick = { onSelect(BillingPeriod.MONTHLY) },
                modifier = Modifier.weight(1f)
            )
            PeriodOption(
                title = "Annual",
                detail = "2 months free",
                selected = period == BillingPeriod.ANNUAL,
                onClick = { onSelect(BillingPeriod.ANNUAL) },
                modifier = Modifier.weight(1f),
                enabled = annualEnabled
            )
        }
    }
}

@Composable
private fun PeriodOption(
    title: String,
    detail: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val c = AppTheme.colors
    Surface(
        shape = AppShapes.r8,
        color = if (selected) c.surfaceCard else Color.Transparent,
        border = BorderStroke(1.dp, if (selected) c.gold else c.gold.copy(alpha = 0.18f)),
        modifier = modifier
            .fillMaxHeight()
            .selectable(
                selected = selected,
                enabled = enabled,
                role = Role.Tab,
                onClick = onClick
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .heightIn(min = 48.dp)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                title,
                style = PaywallType.planFeatureEmph,
                color = when {
                    // Disabled option (e.g. PayPal annual without configured
                    // plan ids): faded like the unselected state, but untappable.
                    !enabled -> c.textMuted.copy(alpha = 0.45f)
                    selected -> c.textPrimary
                    else -> c.textMuted
                }
            )
            Text(
                detail,
                style = PaywallType.planSub,
                color = when {
                    !enabled -> c.textMuted.copy(alpha = 0.35f)
                    selected -> c.gilt
                    else -> c.textMuted.copy(alpha = 0.7f)
                }
            )
        }
    }
}

@Composable
private fun ProviderOption(
    title: String,
    detail: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val c = AppTheme.colors
    Surface(
        shape = AppShapes.r12,
        color = if (selected) c.surfaceCard else c.surfaceSunken,
        border = BorderStroke(1.dp, if (selected) c.gold else c.gold.copy(alpha = 0.28f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = PaywallType.planFeatureEmph, color = c.textPrimary)
                Text(detail, style = PaywallType.planSub, color = c.textSecondary)
            }
            Spacer(Modifier.weight(0.1f))
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .border(1.dp, if (selected) c.gold else c.textMuted.copy(alpha = 0.4f), AppShapes.r8)
                    .padding(3.dp)
            ) {
                if (selected) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(c.gold, AppShapes.r6)
                    )
                }
            }
        }
    }
}

/**
 * Email-code sign-in fallback (account recovery — audit gap: Google-only).
 * Step 1: enter email → send code. Step 2: enter the 6-digit code → verify.
 * On success the ViewModel closes the dialog and the pending purchase flow
 * continues exactly as after Google sign-in.
 */
@Composable
private fun EmailOtpDialog(
    state: EmailOtpState,
    onSend: (String) -> Unit,
    onVerify: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val c = AppTheme.colors
    var email by remember(state.visible) { mutableStateOf(state.email) }
    var code by remember(state.codeSent) { mutableStateOf("") }

    Dialog(onDismissRequest = { if (!state.busy) onDismiss() }) {
        Surface(
            shape = AppShapes.r20,
            color = c.surfaceCard,
            border = BorderStroke(1.dp, c.gold.copy(alpha = 0.35f))
        ) {
            Column(modifier = Modifier.imePadding().padding(22.dp)) {
                Eyebrow("EMAIL SIGN-IN", color = c.crimson)
                Spacer(Modifier.height(6.dp))
                Text(
                    if (state.codeSent) "Enter your code" else "Sign in with an email code",
                    style = PaywallType.planTitle,
                    color = c.textPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    if (state.codeSent) {
                        "We emailed a 6-digit code to ${state.email}. It expires in about an hour."
                    } else {
                        "Use the email on your account — handy when Google isn't available on this phone."
                    },
                    style = PaywallType.planSub,
                    color = c.textSecondary
                )
                Spacer(Modifier.height(16.dp))

                if (!state.codeSent) {
                    LedgerField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email address",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(Modifier.height(14.dp))
                    GiltButton(
                        onClick = { onSend(email) },
                        text = if (state.busy) "Sending…" else "Email me a code",
                        enabled = !state.busy,
                        modifier = Modifier.fillMaxWidth(),
                        height = 48.dp
                    )
                } else {
                    LedgerField(
                        value = code,
                        onValueChange = { code = it.filter { ch -> ch.isDigit() }.take(6) },
                        label = "6-digit code",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(14.dp))
                    GiltButton(
                        onClick = { onVerify(code) },
                        text = if (state.busy) "Verifying…" else "Verify & sign in",
                        enabled = !state.busy,
                        modifier = Modifier.fillMaxWidth(),
                        height = 48.dp
                    )
                    TextButton(
                        onClick = { onSend(state.email) },
                        enabled = !state.busy,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Send a new code", style = PaywallType.meta, color = c.textSecondary)
                    }
                }

                state.error?.let { msg ->
                    Spacer(Modifier.height(8.dp))
                    Text(msg, style = PaywallType.stickyNote, color = c.crimson)
                }

                Spacer(Modifier.height(4.dp))
                TextButton(
                    onClick = onDismiss,
                    enabled = !state.busy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel", style = PaywallType.meta, color = c.textMuted)
                }
            }
        }
    }
}
