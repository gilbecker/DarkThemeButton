package com.gilbecker.darktheme

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.gilbecker.R
import com.gilbecker.animator.DarkThemeButtonAnimator
import com.gilbecker.darktheme.mvc.Contract
import com.gilbecker.darktheme.mvc.DarkThemeButtonController
import kotlinx.android.synthetic.main.dark_theme_button.view.*

class DarkThemeButton(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs), Contract.View {

    private var animator: DarkThemeButtonAnimator

    private var controller: DarkThemeButtonController

    var sunColor
        get() = ContextCompat.getColor(context, R.color.sunColor)
        set(color) {
            sunCircle.setColorFilter(color)
            sunRays.setColorFilter(color)
            animator.updateSunColor(color)
        }

    var moonColor
        get() = ContextCompat.getColor(context, R.color.moonColor)
        set(color) {
            moon.setColorFilter(color)
            animator.updateMoonColor(color)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.dark_theme_button, this)

        animator = DarkThemeButtonAnimator(this, moon, sunCircle, sunRays, moonColor, sunColor)

        context.obtainStyledAttributes(attrs, R.styleable.DarkThemeButton).apply {
            moonColor = getColor(R.styleable.DarkThemeButton_moonColor, moonColor)
            sunColor = getColor(R.styleable.DarkThemeButton_sunColor, sunColor)
            recycle()
        }

        controller = DarkThemeButtonController(context, this)

        controller.onCreate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            ACTION_DOWN -> {
                animator.pushButtonDown()
                true
            }
            ACTION_UP -> {
                animator.pushButtonUp()
                if (event.eventTime - event.downTime < 800) {
                    performClick()
                }
                true
            }
            ACTION_CANCEL -> {
                animator.pushButtonUp()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun performClick(): Boolean {
        controller.onButtonClicked()
        return super.performClick()
    }

    override fun startSunToMoonAnimation() {
        animator.playSunToMoon {
            controller.onAnimationEnded()
        }
    }

    override fun startMoonToSunAnimation() {
        animator.playMoonToSun {
            controller.onAnimationEnded()
        }
    }

    override fun hideMoon() {
        moon.alpha = 0f
    }

    override fun hideSun() {
        sunRays.alpha = 0f
        sunCircle.alpha = 0f
    }
}

enum class Theme {
    DARK, LIGHT
}