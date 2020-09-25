package com.gilbecker.animator

import android.animation.*
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.annotation.ColorInt
import com.gilbecker.DarkThemeButton

private const val SCALE_X = "scaleX"
private const val SCALE_Y = "scaleY"
private const val ALPHA = "alpha"
private const val ROTATION = "rotation"
private const val COLOR_FILTER = "colorFilter"

internal class DarkThemeButtonAnimator(
    private val button: DarkThemeButton,
    private val moon: ImageView,
    private val sun: ImageView,
    private val sunRays: ImageView,
    @ColorInt private var moonColor: Int,
    @ColorInt private var sunColor: Int
) {

    private val colorEvaluator = ArgbEvaluator()

    fun updateMoonColor(@ColorInt color: Int) {
        moonColor = color
    }

    fun updateSunColor(@ColorInt color: Int) {
        sunColor = color
    }

    fun pushDown() {
        AnimatorSet().apply {
            duration = 200
            playTogether(
                getScaleDownAnimator(button),
                getMoonToSunColorAnimator(moon),
                getSunToMoonColorAnimator(sun),
                getSunToMoonColorAnimator(sunRays)
            )
            start()
        }
    }

    fun pushUp() {
        AnimatorSet().apply {
            duration = 200
            playTogether(
                getScaleUpAnimator(button),
                getSunToMoonColorAnimator(moon),
                getMoonToSunColorAnimator(sun),
                getMoonToSunColorAnimator(sunRays)
            )
            start()
        }
    }

    fun moonToSun(listener: AnimationEndListener) {
        AnimatorSet().apply {
            playSequentially(getMoonToSunAnimator(), getSunRaysOutAnimator())
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    listener.onEnd(false)
                }
            })
            start()
        }
    }

    fun sunToMoon(listener: AnimationEndListener) {
        AnimatorSet().apply {
            playSequentially(
                getSunRaysInAnimator(),
                ObjectAnimator.ofFloat(sunRays, ALPHA, 0f).apply {
                    duration = 1
                },
                getSunToMoonAnimator()
            )
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    listener.onEnd(true)
                }
            })
            start()
        }
    }


    private fun getSunToMoonAnimator(): AnimatorSet {
        val animation = AnimatorSet().apply {
            duration = 600
            playTogether(
                ObjectAnimator.ofFloat(moon, ROTATION, 360f),
                ObjectAnimator.ofObject(
                    sun,
                    COLOR_FILTER,
                    colorEvaluator,
                    moonColor,
                    moonColor
                ),
                ObjectAnimator.ofFloat(moon, SCALE_X, 0.6f, 1.05f),
                ObjectAnimator.ofFloat(moon, SCALE_Y, 0.6f, 1.05f),

                )
        }

        val animation2 = AnimatorSet().apply {
            duration = 600
            playTogether(
                ObjectAnimator.ofFloat(moon, ALPHA, 1f),
                ObjectAnimator.ofFloat(sun, ALPHA, 0f)
            )
        }

        return AnimatorSet().apply {
            playTogether(animation, animation2)
        }
    }

    private fun getMoonToSunAnimator(): AnimatorSet {

       return AnimatorSet().apply {
            duration = 600
            playTogether(
                ObjectAnimator.ofFloat(sun, ALPHA, 1f),
                ObjectAnimator.ofFloat(moon, ROTATION, 360f),
                ObjectAnimator.ofFloat(moon, SCALE_X, 1f, 0.8f),
                ObjectAnimator.ofFloat(moon, SCALE_Y, 1f, 0.8f),
                ObjectAnimator.ofObject(
                    moon,
                    COLOR_FILTER,
                    colorEvaluator,
                    sunColor,
                    sunColor
                )
            )
        }
//
//        return AnimatorSet().apply {
//            playTogether(sunEnters, moonDisappears)
//        }
    }

    private fun getSunRaysInAnimator(): AnimatorSet {
        return AnimatorSet().apply {
            duration = 400
            playTogether(
                ObjectAnimator.ofFloat(sunRays, SCALE_X, 1f, 0.6f),
                ObjectAnimator.ofFloat(sunRays, SCALE_Y, 1f, 0.6f),
                ObjectAnimator.ofObject(
                    sun,
                    COLOR_FILTER,
                    colorEvaluator,
                    moonColor,
                    moonColor
                ), ObjectAnimator.ofObject(
                    sunRays,
                    COLOR_FILTER,
                    colorEvaluator,
                    moonColor,
                    moonColor
                )
            )
        }
    }

    private fun getSunRaysOutAnimator(): AnimatorSet {
        return AnimatorSet().apply {
            duration = 400
            interpolator = BounceInterpolator()
            playTogether(
                ObjectAnimator.ofFloat(sunRays, SCALE_X, 0.5f, 1f),
                ObjectAnimator.ofFloat(sunRays, SCALE_Y, 0.5f, 1f),
                ObjectAnimator.ofFloat(sunRays, ALPHA, 1f)
            )
        }
    }

    private fun getScaleUpAnimator(button: View): AnimatorSet {
        return AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(button, SCALE_X, 0.85f, 1f),
                ObjectAnimator.ofFloat(button, SCALE_Y, 0.85f, 1f)
            )
        }
    }

    private fun getScaleDownAnimator(button: View): AnimatorSet {
        return AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(button, SCALE_X, 1f, 0.85f),
                ObjectAnimator.ofFloat(button, SCALE_Y, 1f, 0.85f)
            )
        }
    }

    private fun getMoonToSunColorAnimator(view: ImageView): ObjectAnimator {
        return ObjectAnimator.ofObject(
            view,
            COLOR_FILTER,
            colorEvaluator,
            moonColor,
            sunColor
        )
    }

    private fun getSunToMoonColorAnimator(view: ImageView) =
        ObjectAnimator.ofObject(
            view,
            COLOR_FILTER,
            colorEvaluator,
            sunColor,
            moonColor
        )

    fun interface AnimationEndListener {
        fun onEnd(isDark: Boolean)
    }
}