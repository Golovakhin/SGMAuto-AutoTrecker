package com.example.sgmautotreckerapp.data.preferences

import android.content.Context
import android.content.SharedPreferences

class ThemePreferences(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    fun isDarkTheme(): Boolean {
        return prefs.getBoolean("is_dark_theme", false)
    }

    fun setDarkTheme(isDark: Boolean) {
        prefs.edit().putBoolean("is_dark_theme", isDark).apply()
    }
}

