package com.gilbecker.repository

import android.content.Context
import com.gilbecker.ButtonState

private const val SHARED_PREFS_NAME = "dark_theme_button_prefs"
private const val DARK_THEME_SWITCHED = "dark_theme_switched"

// delegate for shared prefs
internal class DarkThemeRepository(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0)

    fun isDarkThemeButtonClicked() = sharedPreferences.contains(DARK_THEME_SWITCHED)

    fun getDarkThemeMode(): ButtonState {
        return if (sharedPreferences.getInt(DARK_THEME_SWITCHED, ButtonState.DARK.id) == ButtonState.DARK.id) {
            ButtonState.DARK
        } else {
            ButtonState.LIGHT
        }
    }

    fun setDarkThemeMode(buttonState: ButtonState) {
        sharedPreferences.edit().putInt(DARK_THEME_SWITCHED, buttonState.id).apply()
    }
}