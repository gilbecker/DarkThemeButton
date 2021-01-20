package com.gilbecker.darktheme.mvc

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.gilbecker.darktheme.DarkThemeButton
import com.gilbecker.darktheme.Theme
import com.gilbecker.repository.DarkThemeRepository

internal class DarkThemeButtonController(
    val context: Context,
    private val darkThemeButton: DarkThemeButton
) : Contract.Controller {

    private val repo = DarkThemeRepository(context)

    private lateinit var theme: Theme

    override fun onCreate() {
        theme = when {
            repo.wasDarkThemeButtonClicked() -> {
                repo.darkThemeMode
            }
            else -> {
                if (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
                    Theme.DARK
                } else {
                    Theme.LIGHT
                }
            }
        }

        changeTheme(theme)

        if (theme == Theme.DARK) {
            darkThemeButton.hideSun()
        } else {
            darkThemeButton.hideMoon()
        }
    }

    override fun onAnimationEnded() {
        theme = theme.getOpposite()

        changeTheme(theme)

        repo.darkThemeMode = theme
    }

    override fun onButtonClicked() {
        if (theme == Theme.DARK) {
            darkThemeButton.startMoonToSunAnimation()
        } else {
            darkThemeButton.startSunToMoonAnimation()
        }
    }


    private fun changeTheme(theme: Theme) {
        val themeMode = if (theme == Theme.DARK) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    private fun Theme.getOpposite(): Theme {
        return when (this) {
            Theme.DARK -> Theme.LIGHT
            Theme.LIGHT -> Theme.DARK
        }
    }
}