package com.leonestudio.tawba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.leonestudio.tawba.ui.navigation.TawbaNavHost
import com.leonestudio.tawba.ui.theme.TawbaTheme
import com.leonestudio.tawba.ui.theme.ThemeChoice
import com.leonestudio.tawba.util.LocaleHelper

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: android.content.Context) {
        val prefs = newBase.getSharedPreferences("tawba_prefs_sync", 0)
        val lang = prefs.getString("lang", "ar") ?: "ar"
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = applicationContext as TawbaApplication
            val theme by app.userPreferences.themeFlow.collectAsState(initial = ThemeChoice.DIVINE_GRACE)
            val isOnboarded by app.userPreferences.isOnboardedFlow.collectAsState(initial = true)

            TawbaTheme(themeChoice = theme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TawbaNavHost(showOnboarding = !isOnboarded)
                }
            }
        }
    }
}