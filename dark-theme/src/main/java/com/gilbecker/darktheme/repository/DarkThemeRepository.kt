package com.gilbecker.repository

import android.content.Context
import com.gilbecker.darktheme.Theme

private const val SHARED_PREFS_NAME = "dark_theme_button_prefs"
private const val DARK_THEME_SWITCHED = "dark_theme_switched"

internal class DarkThemeRepository(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0)

    var darkThemeMode: Theme
        get() =
            when (sharedPreferences.getInt(
                DARK_THEME_SWITCHED,
                Theme.DARK.ordinal
            )) {
                Theme.DARK.ordinal -> Theme.DARK
                else -> Theme.LIGHT
            }
        set(value) =
            sharedPreferences.edit().putInt(DARK_THEME_SWITCHED, value.ordinal).apply()


    fun wasDarkThemeButtonClicked() = sharedPreferences.contains(DARK_THEME_SWITCHED)
}