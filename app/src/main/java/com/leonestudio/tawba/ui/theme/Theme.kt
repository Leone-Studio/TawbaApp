package com.leonestudio.tawba.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private fun divineGraceScheme(): ColorScheme = lightColorScheme(
    primary = DG_Primary,
    onPrimary = DG_Surface,
    primaryContainer = DG_SurfaceAlt,
    onPrimaryContainer = DG_PrimaryDeep,
    secondary = DG_Secondary,
    onSecondary = DG_TextPrimary,
    secondaryContainer = Color_PinkSoft,
    onSecondaryContainer = DG_SecondaryDeep,
    tertiary = DG_Accent,
    onTertiary = DG_Surface,
    tertiaryContainer = DG_AccentSoft,
    onTertiaryContainer = DG_PrimaryDeep,
    background = DG_Bg,
    onBackground = DG_TextPrimary,
    surface = DG_Surface,
    onSurface = DG_TextPrimary,
    surfaceVariant = DG_SurfaceAlt,
    onSurfaceVariant = DG_TextSecondary,
    outline = DG_Primary,
    outlineVariant = DG_PrimarySoft,
    error = ErrorRed,
    onError = DG_Surface
)

private fun midnightNoorScheme(): ColorScheme = darkColorScheme(
    primary = MN_Primary,
    onPrimary = MN_Bg,
    primaryContainer = MN_SurfaceAlt,
    onPrimaryContainer = MN_PrimaryDeep,
    secondary = MN_Secondary,
    onSecondary = MN_Bg,
    secondaryContainer = MN_SurfaceAlt,
    onSecondaryContainer = MN_SecondaryDeep,
    tertiary = MN_Accent,
    onTertiary = MN_Bg,
    tertiaryContainer = MN_SurfaceAlt,
    onTertiaryContainer = MN_PrimaryDeep,
    background = MN_Bg,
    onBackground = MN_TextPrimary,
    surface = MN_Surface,
    onSurface = MN_TextPrimary,
    surfaceVariant = MN_SurfaceAlt,
    onSurfaceVariant = MN_TextSecondary,
    outline = MN_Primary,
    outlineVariant = MN_PrimarySoft,
    error = ErrorRed,
    onError = MN_TextPrimary
)

private fun roseGardenScheme(): ColorScheme = lightColorScheme(
    primary = RG_Primary,
    onPrimary = RG_Surface,
    primaryContainer = RG_SurfaceAlt,
    onPrimaryContainer = RG_PrimaryDeep,
    secondary = RG_Secondary,
    onSecondary = RG_TextPrimary,
    secondaryContainer = RG_SurfaceAlt,
    onSecondaryContainer = RG_SecondaryDeep,
    tertiary = RG_Accent,
    onTertiary = RG_Surface,
    tertiaryContainer = RG_AccentSoft,
    onTertiaryContainer = RG_PrimaryDeep,
    background = RG_Bg,
    onBackground = RG_TextPrimary,
    surface = RG_Surface,
    onSurface = RG_TextPrimary,
    surfaceVariant = RG_SurfaceAlt,
    onSurfaceVariant = RG_TextSecondary,
    outline = RG_Primary,
    outlineVariant = RG_PrimarySoft,
    error = ErrorRed,
    onError = RG_Surface
)

private fun greenSerenityScheme(): ColorScheme = lightColorScheme(
    primary = GS_Primary,
    onPrimary = GS_Surface,
    primaryContainer = GS_SurfaceAlt,
    onPrimaryContainer = GS_PrimaryDeep,
    secondary = GS_Secondary,
    onSecondary = GS_TextPrimary,
    secondaryContainer = GS_SurfaceAlt,
    onSecondaryContainer = GS_SecondaryDeep,
    tertiary = GS_Accent,
    onTertiary = GS_Surface,
    tertiaryContainer = GS_AccentSoft,
    onTertiaryContainer = GS_PrimaryDeep,
    background = GS_Bg,
    onBackground = GS_TextPrimary,
    surface = GS_Surface,
    onSurface = GS_TextPrimary,
    surfaceVariant = GS_SurfaceAlt,
    onSurfaceVariant = GS_TextSecondary,
    outline = GS_Primary,
    outlineVariant = GS_PrimarySoft,
    error = ErrorRed,
    onError = GS_Surface
)

private val Color_PinkSoft = androidx.compose.ui.graphics.Color(0x33F4B6C2)

@Composable
fun TawbaTheme(
    themeChoice: ThemeChoice = ThemeChoice.DIVINE_GRACE,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeChoice) {
        ThemeChoice.DIVINE_GRACE -> divineGraceScheme()
        ThemeChoice.MIDNIGHT_NOOR -> midnightNoorScheme()
        ThemeChoice.ROSE_GARDEN -> roseGardenScheme()
        ThemeChoice.GREEN_SERENITY -> greenSerenityScheme()
    }

    val isDarkTheme = themeChoice == ThemeChoice.MIDNIGHT_NOOR

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = TawbaTypography,
        content = content
    )
}