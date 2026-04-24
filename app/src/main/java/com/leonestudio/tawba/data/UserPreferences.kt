package com.leonestudio.tawba.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leonestudio.tawba.ui.theme.ThemeChoice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "tawba_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
        private val KEY_VIBRATION = booleanPreferencesKey("vibration")
        private val KEY_CURRENCY = stringPreferencesKey("currency")
        private val KEY_THEME = stringPreferencesKey("theme_choice")
        private val KEY_ONBOARDED = booleanPreferencesKey("is_onboarded")
        private val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val KEY_NOTIFICATION_HOUR = intPreferencesKey("notification_hour")
        private val KEY_NOTIFICATION_MINUTE = intPreferencesKey("notification_minute")
    }

    val languageFlow: Flow<String> = context.dataStore.data.map {
        it[KEY_LANGUAGE] ?: "ar"
    }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_DARK_MODE] ?: false
    }

    val vibrationFlow: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_VIBRATION] ?: true
    }

    val currencyFlow: Flow<String> = context.dataStore.data.map {
        it[KEY_CURRENCY] ?: "MAD"
    }

    val themeFlow: Flow<ThemeChoice> = context.dataStore.data.map {
        ThemeChoice.fromId(it[KEY_THEME])
    }

    val isOnboardedFlow: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_ONBOARDED] ?: false
    }

    val notificationsEnabledFlow: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_NOTIFICATIONS_ENABLED] ?: false
    }

    val notificationHourFlow: Flow<Int> = context.dataStore.data.map {
        it[KEY_NOTIFICATION_HOUR] ?: 20
    }

    val notificationMinuteFlow: Flow<Int> = context.dataStore.data.map {
        it[KEY_NOTIFICATION_MINUTE] ?: 0
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[KEY_LANGUAGE] = lang }
        context.getSharedPreferences("tawba_prefs_sync", 0)
            .edit().putString("lang", lang).apply()
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[KEY_DARK_MODE] = enabled }
    }

    suspend fun setVibration(enabled: Boolean) {
        context.dataStore.edit { it[KEY_VIBRATION] = enabled }
    }

    suspend fun setCurrency(currency: String) {
        context.dataStore.edit { it[KEY_CURRENCY] = currency }
    }

    suspend fun setTheme(theme: ThemeChoice) {
        context.dataStore.edit { it[KEY_THEME] = theme.id }
    }

    suspend fun setOnboarded(onboarded: Boolean) {
        context.dataStore.edit { it[KEY_ONBOARDED] = onboarded }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[KEY_NOTIFICATIONS_ENABLED] = enabled }
    }

    suspend fun setNotificationTime(hour: Int, minute: Int) {
        context.dataStore.edit {
            it[KEY_NOTIFICATION_HOUR] = hour
            it[KEY_NOTIFICATION_MINUTE] = minute
        }
    }
}