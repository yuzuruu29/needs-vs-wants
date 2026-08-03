package com.needsvswants.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.domain.FontScaleStep
import com.needsvswants.app.domain.ThemeId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppAppearanceViewModel @Inject constructor(
    preferences: AppPreferences
) : ViewModel() {
    val themeId: StateFlow<ThemeId> = preferences.themeId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeId.MARKET_LIGHT)

    val fontScaleStep: StateFlow<FontScaleStep> = preferences.fontScaleStep
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FontScaleStep.DEFAULT)
}
