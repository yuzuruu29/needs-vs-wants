package com.needsvswants.app.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.prefs.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Drives the soft-launch Pro/Max paywall shown once per cold start for free users.
 * Dismissing ("Continue free") suppresses it for the rest of the process lifetime only.
 *
 * First-run How It Works (Summary instructions) runs **before** the soft paywall so the
 * trainer story is not buried under membership (D75).
 */
@HiltViewModel
class LaunchPaywallViewModel @Inject constructor(
    repository: EntitlementRepository,
    preferences: AppPreferences
) : ViewModel() {

    private val dismissedThisSession = MutableStateFlow(false)

    /**
     * True when the user is free, has finished first-launch instructions, and has not
     * dismissed the launch paywall this session.
     */
    val shouldOfferSoftPaywall: StateFlow<Boolean> = combine(
        repository.hasProAccess,
        dismissedThisSession,
        preferences.isFirstLaunch
    ) { hasPro, dismissed, firstLaunch ->
        !hasPro && !dismissed && !firstLaunch
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun dismissSoftPaywallForSession() {
        dismissedThisSession.value = true
    }
}
