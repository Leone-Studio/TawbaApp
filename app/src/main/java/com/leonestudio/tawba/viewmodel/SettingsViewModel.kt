package com.leonestudio.tawba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leonestudio.tawba.data.UserPreferences
import com.leonestudio.tawba.ui.theme.ThemeChoice
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val prefs: UserPreferences) : ViewModel() {

    val language: StateFlow<String> = prefs.languageFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "ar")

    val darkMode: StateFlow<Boolean> = prefs.darkModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val vibration: StateFlow<Boolean> = prefs.vibrationFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    val currency: StateFlow<String> = prefs.currencyFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "MAD")

    val theme: StateFlow<ThemeChoice> = prefs.themeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemeChoice.DIVINE_GRACE)

    val isOnboarded: StateFlow<Boolean> = prefs.isOnboardedFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val notificationsEnabled: StateFlow<Boolean> = prefs.notificationsEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val notificationHour: StateFlow<Int> = prefs.notificationHourFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 20)

    val notificationMinute: StateFlow<Int> = prefs.notificationMinuteFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun setLanguage(lang: String) {
        viewModelScope.launch { prefs.setLanguage(lang) }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { prefs.setDarkMode(enabled) }
    }

    fun setVibration(enabled: Boolean) {
        viewModelScope.launch { prefs.setVibration(enabled) }
    }

    fun setCurrency(currency: String) {
        viewModelScope.launch { prefs.setCurrency(currency) }
    }

    fun setTheme(choice: ThemeChoice) {
        viewModelScope.launch { prefs.setTheme(choice) }
    }

    fun setOnboarded(onboarded: Boolean) {
        viewModelScope.launch { prefs.setOnboarded(onboarded) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { prefs.setNotificationsEnabled(enabled) }
    }

    fun setNotificationTime(hour: Int, minute: Int) {
        viewModelScope.launch { prefs.setNotificationTime(hour, minute) }
    }

    class Factory(private val prefs: UserPreferences) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(prefs) as T
        }
    }
}