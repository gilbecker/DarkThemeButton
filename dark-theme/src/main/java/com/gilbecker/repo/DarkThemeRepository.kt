package com.gilbecker.repo

import android.content.Context
import android.content.SharedPreferences

private const val SHARED_PREFS_NAME = "DARK_THEME_BUTTON_PREFS"
private const val DARK_THEME_SWITCHED = "dark_theme_switched"

internal class DarkThemeRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, 0)

    fun isDarkThemeButtonClicked() = sharedPreferences.contains(DARK_THEME_SWITCHED)

    fun isDarkThemeActive() = sharedPreferences.getBoolean(DARK_THEME_SWITCHED, false)

    fun setDarkThemeMode(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME_SWITCHED, enabled).apply()
    }
}