package com.gilbecker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.gilbecker.animator.DarkThemeButtonAnimator
import com.gilbecker.handler.DarkThemeButtonController
import kotlinx.android.synthetic.main.dark_theme_button.view.*

class DarkThemeButton(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    private var animator: DarkThemeButtonAnimator

    private var controller: StateChangedHandler = DarkThemeButtonController(context)

    var sunColor
        get() = ContextCompat.getColor(context, R.color.sunColor)
        set(color) {
            sun.setColorFilter(color)
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

        animator = DarkThemeButtonAnimator(this, moon, sun, sunRays, moonColor, sunColor)

        context.obtainStyledAttributes(attrs, R.styleable.DarkThemeButton).apply {
            moonColor = getColor(R.styleable.DarkThemeButton_moonColor, moonColor)
            sunColor = getColor(R.styleable.DarkThemeButton_sunColor, sunColor)
            recycle()
        }

        controller.onInflate()

        updateButton()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
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

    override fun performClick(): Boolean {
        playAnimation()
        return super.performClick()
    }

    fun setButtonChangeHandler(handler: StateChangedHandler): DarkThemeButton {
        controller = handler
        return this
    }

    private fun playAnimation() {
        val animationEndListener = DarkThemeButtonAnimator.AnimationEndListener {
            controller.onAnimationEnded()
        }

        if (controller.buttonState == ButtonState.LIGHT) {
            animator.sunToMoon(animationEndListener)
        } else {
            animator.moonToSun(animationEndListener)
        }

    }

    private fun updateButton() {
        if (controller.buttonState == ButtonState.DARK) {
            sun.alpha = 0f
            sunRays.alpha = 0f
        } else {
            moon.alpha = 0f
        }
    }

    interface StateChangedHandler {
        var buttonState: ButtonState

        fun onInflate()
        fun onAnimationEnded()
    }
}

enum class ButtonState(val id: Int) {
    DARK(0), LIGHT(1)
}