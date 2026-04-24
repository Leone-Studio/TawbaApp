package com.leonestudio.tawba.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.leonestudio.tawba.R
import com.leonestudio.tawba.ui.screens.AdhkarListScreen
import com.leonestudio.tawba.ui.screens.AdvancedSettingsScreen
import com.leonestudio.tawba.ui.screens.OnboardingScreen
import com.leonestudio.tawba.ui.screens.QuranScreen
import com.leonestudio.tawba.ui.screens.SadaqaScreen
import com.leonestudio.tawba.ui.screens.SettingsScreen
import com.leonestudio.tawba.ui.screens.StatsScreen

sealed class Screen(val route: String, val labelRes: Int, val icon: ImageVector) {
    data object Adhkar : Screen("adhkar", R.string.nav_adhkar, Icons.AutoMirrored.Filled.MenuBook)
    data object Quran : Screen("quran", R.string.nav_quran, Icons.Filled.Book)
    data object Sadaqa : Screen("sadaqa", R.string.nav_sadaqa, Icons.Filled.Favorite)
    data object Stats : Screen("stats", R.string.nav_stats, Icons.Filled.BarChart)
    data object Settings : Screen("settings", R.string.nav_settings, Icons.Filled.Settings)
}

private val bottomNavItems = listOf(
    Screen.Adhkar,
    Screen.Quran,
    Screen.Sadaqa,
    Screen.Stats,
    Screen.Settings
)

private const val ONBOARDING_ROUTE = "onboarding"
private const val MAIN_ROUTE = "main"
private const val ADVANCED_SETTINGS_ROUTE = "advanced_settings"

@Composable
fun TawbaNavHost(showOnboarding: Boolean = false) {
    val rootNav = rememberNavController()

    NavHost(
        navController = rootNav,
        startDestination = if (showOnboarding) ONBOARDING_ROUTE else MAIN_ROUTE
    ) {
        composable(ONBOARDING_ROUTE) {
            OnboardingScreen(
                onFinished = {
                    rootNav.navigate(MAIN_ROUTE) {
                        popUpTo(ONBOARDING_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(MAIN_ROUTE) {
            MainTabs(onNavigateToAdvanced = {
                rootNav.navigate(ADVANCED_SETTINGS_ROUTE)
            })
        }
        composable(ADVANCED_SETTINGS_ROUTE) {
            AdvancedSettingsScreen(onBack = { rootNav.popBackStack() })
        }
    }
}

@Composable
private fun MainTabs(onNavigateToAdvanced: () -> Unit) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    val selected = currentRoute == item.route ||
                            backStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(Screen.Adhkar.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(stringResource(item.labelRes)) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Adhkar.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Adhkar.route) { AdhkarListScreen() }
            composable(Screen.Quran.route) { QuranScreen() }
            composable(Screen.Sadaqa.route) { SadaqaScreen() }
            composable(Screen.Stats.route) { StatsScreen() }
            composable(Screen.Settings.route) {
                SettingsScreen(onNavigateToAdvanced = onNavigateToAdvanced)
            }
        }
    }
}