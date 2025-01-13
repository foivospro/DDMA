// ThemePreferences.kt
package com.example.gymappdemo.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.gymappdemo.ui.theme.AppThemeType

class ThemePreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("theme_preferences", Context.MODE_PRIVATE)

    fun saveTheme(themeType: AppThemeType) {
        sharedPreferences.edit().putString("ACCENT_THEME", themeType.name).apply()
    }

    fun getTheme(): AppThemeType {
        val stored = sharedPreferences.getString("ACCENT_THEME", AppThemeType.DEFAULT.name)
        return AppThemeType.valueOf(stored ?: AppThemeType.DEFAULT.name)
    }

    // Αν θέλεις dark mode κι εδώ:
    fun saveDarkModeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("IS_DARK_MODE", enabled).apply()
    }

    fun getDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean("IS_DARK_MODE", false)
    }
}
