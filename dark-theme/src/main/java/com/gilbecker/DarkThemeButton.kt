package com.gilbecker

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import com.gilbecker.animator.DarkThemeButtonAnimator
import com.gilbecker.repo.DarkThemeRepository
import kotlinx.android.synthetic.main.dark_theme_button.view.*

class DarkThemeButton(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs), View.OnClickListener, View.OnTouchListener {

    private var onThemeChangedListener: OnThemeChangedListener? = null
    private var onAnimationFinishedListener: OnAnimationFinishedListener? = null

    private var isDark: Boolean = false
    private var sunColor = Color.parseColor("#ffdc00")
    private var moonColor = Color.parseColor("#dddddd")
    private var animator: DarkThemeButtonAnimator
    private var switchThemeOnClick: Boolean = true
    private val darkThemeRepo = DarkThemeRepository(context)

    init {
        LayoutInflater.from(context).inflate(R.layout.dark_theme_button, this, true)

        animator = DarkThemeButtonAnimator(this, moon, sun, sunRays, moonColor, sunColor)

        context.obtainStyledAttributes(attrs, R.styleable.DarkThemeButton).apply {
            // maybe it's redundant to call these setters
            setMoonColor(getColor(R.styleable.DarkThemeButton_moonColor, moonColor))
            setSunColor(getColor(R.styleable.DarkThemeButton_sunColor, sunColor))
            setSwitchThemeOnClick(
                getBoolean(R.styleable.DarkThemeButton_switchThemeOnClick, switchThemeOnClick)
            )
            recycle()
        }

        if (isDarkThemeEnabled()) {
            isDark = true
            hideSun()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            isDark = false
            hideMoon()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setOnClickListener(this)
        setOnTouchListener(this)
    }

    override fun onClick(v: View) {
        isDark = if (isDark) {
            letThereBeLight()
            false
        } else {
            goodNight()
            true
        }

        darkThemeRepo.setDarkThemeMode(isDark)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return when (event.action) {
            ACTION_DOWN -> {
                animator.pushDown()
                true
            }
            ACTION_UP -> {
                animator.pushUp()
                if (event.eventTime - event.downTime < 800) {
                    performClick()
                }

                true
            }
            ACTION_CANCEL -> {
                animator.pushUp()
                true
            }
            else -> {
                false
            }
        }
    }

    fun setSunColor(@ColorInt color: Int) {
        sunColor = color
        sun.setColorFilter(sunColor)
        sunRays.setColorFilter(sunColor)
        animator.updateSunColor(sunColor)
        invalidate()
        requestLayout()
    }

    fun setMoonColor(@ColorInt color: Int) {
        moonColor = color
        moon.setColorFilter(moonColor)
        animator.updateMoonColor(moonColor)
        invalidate()
        requestLayout()
    }

    fun setSwitchThemeOnClick(switchThemeOnClick: Boolean): DarkThemeButton {
        this.switchThemeOnClick = switchThemeOnClick
        return this
    }

    fun setOnThemeChangedListener(listener: OnThemeChangedListener): DarkThemeButton {
        this.onThemeChangedListener = listener
        return this
    }

    private fun isDarkThemeEnabled(): Boolean {
        return if (darkThemeRepo.isDarkThemeButtonClicked()) {
            darkThemeRepo.isDarkThemeActive()
        } else {
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
    }

    private fun letThereBeLight() {
        animator.moonToSun { isDark ->
            if (switchThemeOnClick) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                onThemeChangedListener?.onThemeChanged(isDark)
            }

            onAnimationFinishedListener?.onFinish()
        }
    }

    private fun goodNight() {
        animator.sunToMoon { isDark ->
            if (switchThemeOnClick) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                onThemeChangedListener?.onThemeChanged(isDark)
            }

            onAnimationFinishedListener?.onFinish()
        }
    }


    private fun hideSun() {
        sun.alpha = 0f
        sunRays.alpha = 0f
    }

    private fun hideMoon() {
        moon.alpha = 0f
    }

    fun interface OnThemeChangedListener {
        fun onThemeChanged(isDark: Boolean)
    }

    fun interface OnAnimationFinishedListener {
        fun onFinish()
    }
//
//    companion object {
//        fun setPersistDarkTheme(enabled: Boolean) {
//            DarkThemeRepository.setDarkThemeSwitched(enabled)
//        }
//    }
}