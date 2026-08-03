package com.needsvswants.app.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.entitlement.EntitlementRepository
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
 */
@HiltViewModel
class LaunchPaywallViewModel @Inject constructor(
    repository: EntitlementRepository
) : ViewModel() {

    private val dismissedThisSession = MutableStateFlow(false)

    /** True when the user is free and has not dismissed the launch paywall this session. */
    val shouldOfferSoftPaywall: StateFlow<Boolean> = combine(
        repository.hasProAccess,
        dismissedThisSession
    ) { hasPro, dismissed ->
        !hasPro && !dismissed
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun dismissSoftPaywallForSession() {
        dismissedThisSession.value = true
    }
}
