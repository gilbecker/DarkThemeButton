package com.gilbecker.repository

import android.content.Context
import android.content.SharedPreferences
import com.gilbecker.ButtonState
import com.gilbecker.DarkThemeButton

private const val SHARED_PREFS_NAME = "dark_theme_button_prefs"
private const val DARK_THEME_SWITCHED = "dark_theme_switched"

// delegate for shared prefs
internal class DarkThemeRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, 0)

    fun isDarkThemeButtonClicked() = sharedPreferences.contains(DARK_THEME_SWITCHED)

    fun isDarkThemeEnabled() = sharedPreferences.getInt(DARK_THEME_SWITCHED, ButtonState.MOON.id) == ButtonState.MOON.id

    fun setDarkThemeMode(buttonState: ButtonState) {
        sharedPreferences.edit().putInt(DARK_THEME_SWITCHED, buttonState.id).apply()
    }
}