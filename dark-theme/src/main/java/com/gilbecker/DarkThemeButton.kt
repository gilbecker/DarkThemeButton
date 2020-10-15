package com.gilbecker

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.gilbecker.animator.DarkThemeButtonAnimator
import com.gilbecker.handler.DefaultStateChangedHandler
import com.gilbecker.repository.DarkThemeRepository
import kotlinx.android.synthetic.main.dark_theme_button.view.*

class DarkThemeButton(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs), View.OnClickListener, View.OnTouchListener {

    private var currentButtonState = ButtonState.MOON
    private var animator: DarkThemeButtonAnimator
    private val darkThemeRepo = DarkThemeRepository(context)
    private var stateChangedHandler: StateChangedHandler = DefaultStateChangedHandler()
    private var onClickListener: OnClickListener? = null

    var sunColor
        get() = ContextCompat.getColor(context, R.color.sunColor)
        set(color) {
            sun.setColorFilter(color)
            sunRays.setColorFilter(color)
            animator.updateSunColor(color)
            invalidate()
            requestLayout()
        }

    var moonColor
        get() = ContextCompat.getColor(context, R.color.moonColor)
        set(color) {
            moon.setColorFilter(color)
            animator.updateMoonColor(color)
            invalidate()
            requestLayout()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.dark_theme_button, this)

        animator = DarkThemeButtonAnimator(this, moon, sun, sunRays, moonColor, sunColor)

        context.obtainStyledAttributes(attrs, R.styleable.DarkThemeButton).apply {
            moonColor = getColor(R.styleable.DarkThemeButton_moonColor, moonColor)
            sunColor = getColor(R.styleable.DarkThemeButton_sunColor, sunColor)
//            setSwitchThemeOnClick(
//                getBoolean(R.styleable.DarkThemeButton_switchThemeOnClick, switchThemeOnClick)
//            )
            recycle()
        }

        // confusing
        // use presenter/controller to seperate the logic from the view
        currentButtonState = if (isDarkThemeEnabled()) {
            ButtonState.MOON
        } else {
            ButtonState.SUN
        }

        updateButton()

        stateChangedHandler.onInflate(currentButtonState)

        setOnClickListener(this)
        setOnTouchListener(this)
    }

    override fun onClick(v: View) {
        currentButtonState = currentButtonState.getOpposite()
        playAnimation()
        darkThemeRepo.setDarkThemeMode(currentButtonState)
        onClickListener?.onClick()
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

//    fun setSwitchThemeOnClick(switchThemeOnClick: Boolean): DarkThemeButton {
//        this.switchThemeOnClick = switchThemeOnClick
//        return this
//    }

    fun setButtonChangeHandler(handler: StateChangedHandler): DarkThemeButton {
        stateChangedHandler = handler
        return this
    }

    fun setOnClickListener(listener: OnClickListener): DarkThemeButton {
        onClickListener = listener
        return this
    }

    private fun isDarkThemeEnabled(): Boolean {
        return if (darkThemeRepo.isDarkThemeButtonClicked()) {
            darkThemeRepo.isDarkThemeEnabled()
        } else {
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
    }

    private fun playAnimation() {
        val animationEndListener = DarkThemeButtonAnimator.AnimationEndListener {
            stateChangedHandler.onAnimationEnded(currentButtonState)
        }

        if (currentButtonState == ButtonState.MOON) {
            animator.sunToMoon(animationEndListener)
        } else {
            animator.moonToSun(animationEndListener)
        }

    }

    private fun updateButton() {
        if (currentButtonState == ButtonState.MOON) {
            sun.alpha = 0f
            sunRays.alpha = 0f
        } else {
            moon.alpha = 0f
        }
    }

    interface StateChangedHandler {
        fun onInflate(currentState: ButtonState)
        fun onAnimationEnded(newState: ButtonState)
    }

    interface OnClickListener {
        fun onClick()
    }
}

enum class ButtonState(val id: Int) {
    MOON(0), SUN(1)
}

// change name
private fun ButtonState.getOpposite(): ButtonState {
    return when (this) {
        ButtonState.MOON -> ButtonState.SUN
        ButtonState.SUN -> ButtonState.MOON
    }
}