// ThemePreferences.kt
package com.pmdk.gymapp.data.preferences

import android.content.Context
import com.pmdk.gymapp.ui.theme.AppThemeType

class ThemePreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("theme_preferences", Context.MODE_PRIVATE)

    fun saveTheme(themeType: AppThemeType) {
        sharedPreferences.edit().putString("ACCENT_THEME", themeType.name).apply()
    }

    fun getTheme(): AppThemeType {
        val stored = sharedPreferences.getString("ACCENT_THEME", AppThemeType.DEFAULT.name)
        return AppThemeType.valueOf(stored ?: AppThemeType.DEFAULT.name)
    }

    fun saveDarkModeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("IS_DARK_MODE", enabled).apply()
    }
}
