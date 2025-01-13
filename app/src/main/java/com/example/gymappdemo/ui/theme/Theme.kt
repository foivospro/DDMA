package com.example.gymappdemo.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


@Composable
fun GymAppDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    appThemeType: AppThemeType = AppThemeType.DEFAULT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appThemeType) {
        AppThemeType.DEFAULT -> if (darkTheme) DarkColorScheme_Default else LightColorScheme_Default
        AppThemeType.BLUE -> if (darkTheme) DarkColorScheme_Blue else LightColorScheme_Blue
        AppThemeType.GREEN -> if (darkTheme) DarkColorScheme_Green else LightColorScheme_Green
        AppThemeType.RED -> if (darkTheme) DarkColorScheme_Red else LightColorScheme_Red


    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Light and Dark Color Schemes for Blue Theme
private val LightColorScheme_Blue = lightColorScheme(
    primary = primaryLight_Blue,
    onPrimary = onPrimaryLight_Blue,
    secondary = secondaryLight_Blue,
    onSecondary = onSecondaryLight_Blue,
    tertiary = tertiaryLight_Blue,
    onTertiary = onTertiaryLight_Blue,
    background = backgroundLight_Blue,
    onBackground = onBackgroundLight_Blue,
    surface = surfaceLight_Blue,
    onSurface = onSurfaceLight_Blue,
    error = errorLight_Default,
    onError = onErrorLight_Default
)

private val DarkColorScheme_Blue = darkColorScheme(
    primary = primaryDark_Blue,
    onPrimary = onPrimaryDark_Blue,
    secondary = secondaryDark_Blue,
    onSecondary = onSecondaryDark_Blue,
    tertiary = tertiaryDark_Blue,
    onTertiary = onTertiaryDark_Blue,
    background = backgroundDark_Blue,
    onBackground = onBackgroundDark_Blue,
    surface = surfaceDark_Blue,
    onSurface = onSurfaceDark_Blue,
    error = errorDark_Blue,
    onError = onErrorDark_Blue
)

// Light and Dark Color Schemes for Green Theme
private val LightColorScheme_Green = lightColorScheme(
    primary = primaryLight_Green,
    onPrimary = onPrimaryLight_Green,
    secondary = secondaryLight_Green,
    onSecondary = onSecondaryLight_Green,
    tertiary = tertiaryLight_Green,
    onTertiary = onTertiaryLight_Green,
    background = backgroundLight_Green,
    onBackground = onBackgroundLight_Green,
    surface = surfaceLight_Green,
    onSurface = onSurfaceLight_Green,
    error = errorLight_Default,
    onError = onErrorLight_Default
)

private val DarkColorScheme_Green = darkColorScheme(
    primary = primaryDark_Green,
    onPrimary = onPrimaryDark_Green,
    secondary = secondaryDark_Green,
    onSecondary = onSecondaryDark_Green,
    tertiary = tertiaryDark_Green,
    onTertiary = onTertiaryDark_Green,
    background = backgroundDark_Green,
    onBackground = onBackgroundDark_Green,
    surface = surfaceDark_Green,
    onSurface = onSurfaceDark_Green,
    error = errorDark_Green,
    onError = onErrorDark_Green
)

// Light and Dark Color Schemes for Red Theme
private val LightColorScheme_Red = lightColorScheme(
    primary = primaryLight_Red,
    onPrimary = onPrimaryLight_Red,
    secondary = secondaryLight_Red,
    onSecondary = onSecondaryLight_Red,
    tertiary = tertiaryLight_Red,
    onTertiary = onTertiaryLight_Red,
    background = backgroundLight_Red,
    onBackground = onBackgroundLight_Red,
    surface = surfaceLight_Red,
    onSurface = onSurfaceLight_Red,
    error = errorLight_Default,
    onError = onErrorLight_Default
)

private val DarkColorScheme_Red = darkColorScheme(
    primary = primaryDark_Red,
    onPrimary = onPrimaryDark_Red,
    secondary = secondaryDark_Red,
    onSecondary = onSecondaryDark_Red,
    tertiary = tertiaryDark_Red,
    onTertiary = onTertiaryDark_Red,
    background = backgroundDark_Red,
    onBackground = onBackgroundDark_Red,
    surface = surfaceDark_Red,
    onSurface = onSurfaceDark_Red,
    error = errorDark_Red,
    onError = onErrorDark_Red
)

// Light and Dark Color Schemes for Default Theme
private val LightColorScheme_Default = lightColorScheme(
    primary = primaryLight_Default,
    onPrimary = onPrimaryLight_Default,
    secondary = secondaryLight_Default,
    onSecondary = onSecondaryLight_Default,
    tertiary = tertiaryLight_Default,
    onTertiary = onTertiaryLight_Default,
    background = backgroundLight_Default,
    onBackground = onBackgroundLight_Default,
    surface = surfaceLight_Default,
    onSurface = onSurfaceLight_Default,
    error = errorLight_Default,
    onError = onErrorLight_Default
)

private val DarkColorScheme_Default = darkColorScheme(
    primary = primaryDark_Default,
    onPrimary = onPrimaryDark_Default,
    secondary = secondaryDark_Default,
    onSecondary = onSecondaryDark_Default,
    tertiary = tertiaryDark_Default,
    onTertiary = onTertiaryDark_Default,
    background = backgroundDark_Default,
    onBackground = onBackgroundDark_Default,
    surface = surfaceDark_Default,
    onSurface = onSurfaceDark_Default,
    error = errorDark_Default,
    onError = onErrorDark_Default
)
