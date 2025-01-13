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
        AppThemeType.ORANGE -> if (darkTheme) DarkColorScheme_Orange else LightColorScheme_Orange
        AppThemeType.PURPLE -> if (darkTheme) DarkColorScheme_Purple else LightColorScheme_Purple
        AppThemeType.YELLOW -> if (darkTheme) DarkColorScheme_Yellow else LightColorScheme_Yellow


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
private val LightColorScheme_Orange = lightColorScheme(
    primary = primaryLight_Orange,
    onPrimary = onPrimaryLight_Orange,
    secondary = secondaryLight_Orange,
    onSecondary = onSecondaryLight_Orange,
    tertiary = tertiaryLight_Orange,
    onTertiary = onTertiaryLight_Orange,
    background = backgroundLight_Orange,
    onBackground = onBackgroundLight_Orange,
    surface = surfaceLight_Orange,
    onSurface = onSurfaceLight_Orange,
    error = errorLight_Default,
    onError = onErrorLight_Default
)

private val DarkColorScheme_Orange = darkColorScheme(
    primary = primaryDark_Orange,
    onPrimary = onPrimaryDark_Orange,
    secondary = secondaryDark_Orange,
    onSecondary = onSecondaryDark_Orange,
    tertiary = tertiaryDark_Orange,
    onTertiary = onTertiaryDark_Orange,
    background = backgroundDark_Orange,
    onBackground = onBackgroundDark_Orange,
    surface = surfaceDark_Orange,
    onSurface = onSurfaceDark_Orange,
    error = errorDark_Default,
    onError = onErrorDark_Default
)

// Light and Dark Color Schemes for Green Theme
private val LightColorScheme_Purple = lightColorScheme(
    primary = primaryLight_Purple,
    onPrimary = onPrimaryLight_Purple,
    secondary = secondaryLight_Purple,
    onSecondary = onSecondaryLight_Purple,
    tertiary = tertiaryLight_Purple,
    onTertiary = onTertiaryLight_Purple,
    background = backgroundLight_Purple,
    onBackground = onBackgroundLight_Purple,
    surface = surfaceLight_Purple,
    onSurface = onSurfaceLight_Purple,
    error = errorLight_Default,
    onError = onErrorLight_Default
)

private val DarkColorScheme_Purple = darkColorScheme(
    primary = primaryDark_Purple,
    onPrimary = onPrimaryDark_Purple,
    secondary = secondaryDark_Purple,
    onSecondary = onSecondaryDark_Purple,
    tertiary = tertiaryDark_Purple,
    onTertiary = onTertiaryDark_Purple,
    background = backgroundDark_Purple,
    onBackground = onBackgroundDark_Purple,
    surface = surfaceDark_Purple,
    onSurface = onSurfaceDark_Purple,
    error = errorDark_Default,
    onError = onErrorDark_Default
)

// Light and Dark Color Schemes for Red Theme
private val LightColorScheme_Yellow = lightColorScheme(
    primary = primaryLight_Yellow,
    onPrimary = onPrimaryLight_Yellow,
    secondary = secondaryLight_Yellow,
    onSecondary = onSecondaryLight_Yellow,
    tertiary = tertiaryLight_Yellow,
    onTertiary = onTertiaryLight_Yellow,
    background = backgroundLight_Yellow,
    onBackground = onBackgroundLight_Yellow,
    surface = surfaceLight_Yellow,
    onSurface = onSurfaceLight_Yellow,
    error = errorLight_Default,
    onError = onErrorLight_Default
)

private val DarkColorScheme_Yellow = darkColorScheme(
    primary = primaryDark_Yellow,
    onPrimary = onPrimaryDark_Yellow,
    secondary = secondaryDark_Yellow,
    onSecondary = onSecondaryDark_Yellow,
    tertiary = tertiaryDark_Yellow,
    onTertiary = onTertiaryDark_Yellow,
    background = backgroundDark_Yellow,
    onBackground = onBackgroundDark_Yellow,
    surface = surfaceDark_Yellow,
    onSurface = onSurfaceDark_Yellow,
    error = errorDark_Default,
    onError = onErrorDark_Default
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
