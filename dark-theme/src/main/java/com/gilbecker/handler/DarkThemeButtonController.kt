package com.gilbecker.handler

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.gilbecker.ButtonState
import com.gilbecker.DarkThemeButton
import com.gilbecker.repository.DarkThemeRepository

internal class DarkThemeButtonController(
    val context: Context
) : DarkThemeButton.StateChangedHandler {

    private val repo = DarkThemeRepository(context)

    override var buttonState: ButtonState = ButtonState.LIGHT

    override fun onAnimationEnded() {
        buttonState = buttonState.getOpposite()
        repo.setDarkThemeMode(buttonState)

        when (buttonState) {
            ButtonState.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ButtonState.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onInflate() {
        buttonState = when {
            repo.isDarkThemeButtonClicked() -> {
                repo.getDarkThemeMode()
            }
            else -> {
                if (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
                    ButtonState.DARK
                } else {
                    ButtonState.LIGHT
                }
            }
        }

        when (buttonState) {
            ButtonState.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ButtonState.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun ButtonState.getOpposite(): ButtonState {
        return when (this) {
            ButtonState.DARK -> ButtonState.LIGHT
            ButtonState.LIGHT -> ButtonState.DARK
        }
    }
}