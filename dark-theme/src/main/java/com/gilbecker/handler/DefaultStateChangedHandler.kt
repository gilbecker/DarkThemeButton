package com.gilbecker.handler

import androidx.appcompat.app.AppCompatDelegate
import com.gilbecker.ButtonState
import com.gilbecker.DarkThemeButton

internal class DefaultStateChangedHandler : DarkThemeButton.StateChangedHandler {

    override fun onAnimationEnded(newState: ButtonState) {
        when (newState) {
            ButtonState.MOON -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ButtonState.SUN -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onInflate(currentState: ButtonState) {
        when (currentState) {
            ButtonState.MOON -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ButtonState.SUN -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}