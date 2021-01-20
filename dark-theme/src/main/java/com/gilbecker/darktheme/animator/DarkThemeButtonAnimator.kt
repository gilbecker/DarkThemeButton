package com.gilbecker.animator

import android.animation.*
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.annotation.ColorInt

private const val SCALE_X = "scaleX"
private const val SCALE_Y = "scaleY"
private const val ALPHA = "alpha"
private const val ROTATION = "rotation"
private const val COLOR_FILTER = "colorFilter"

internal class DarkThemeButtonAnimator(
    private val button: View,
    private val moon: ImageView,
    private val sun: ImageView,
    private val sunRays: ImageView,
    @ColorInt private var moonColor: Int,
    @ColorInt private var sunColor: Int
) {

    private val colorEvaluator = ArgbEvaluator()

    private val sunToMoonAnimator by lazy {
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

        AnimatorSet().apply {
            playTogether(animation, animation2)
        }
    }

    private val moonToSunAnimator by lazy {
        AnimatorSet().apply {
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
    }

    private val sunRaysInAnimator by lazy {
        AnimatorSet().apply {
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

    private val sunRaysOutAnimator by lazy {
        AnimatorSet().apply {
            duration = 400
            interpolator = BounceInterpolator()
            playTogether(
                ObjectAnimator.ofFloat(sunRays, SCALE_X, 0.5f, 1f),
                ObjectAnimator.ofFloat(sunRays, SCALE_Y, 0.5f, 1f),
                ObjectAnimator.ofFloat(sunRays, ALPHA, 1f)
            )
        }
    }

    private val scaleDownAnimator by lazy {
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(button, SCALE_X, 1f, 0.85f),
                ObjectAnimator.ofFloat(button, SCALE_Y, 1f, 0.85f)
            )
        }
    }

    private val scaleUpAnimator by lazy {
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(button, SCALE_X, 0.85f, 1f),
                ObjectAnimator.ofFloat(button, SCALE_Y, 0.85f, 1f)
            )
        }
    }

    fun updateMoonColor(@ColorInt color: Int) {
        moonColor = color
    }

    fun updateSunColor(@ColorInt color: Int) {
        sunColor = color
    }

    fun pushButtonDown() {
        AnimatorSet().apply {
            duration = 200
            playTogether(
                scaleDownAnimator,
                createColorAnimator(moonColor, sunColor, moon),
                createColorAnimator(sunColor, moonColor, sun),
                createColorAnimator(sunColor, moonColor, sunRays),
            )
            start()
        }
    }

    fun pushButtonUp() {
        AnimatorSet().apply {
            duration = 200
            playTogether(
                scaleUpAnimator,
                createColorAnimator(sunColor, moonColor, moon),
                createColorAnimator(moonColor, sunColor, sun),
                createColorAnimator(moonColor, sunColor, sunRays)
            )
            start()
        }
    }

    fun playMoonToSun(onAnimationEnd: () -> Unit) {
        AnimatorSet().apply {
            playSequentially(moonToSunAnimator, sunRaysOutAnimator)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    onAnimationEnd()
                }
            })
            start()
        }
    }

    fun playSunToMoon(onAnimationEnd: () -> Unit) {
        AnimatorSet().apply {
            playSequentially(
                sunRaysInAnimator,
                ObjectAnimator.ofFloat(sunRays, ALPHA, 0f).apply {
                    duration = 1
                },
                sunToMoonAnimator
            )
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    onAnimationEnd()
                }
            })
            start()
        }
    }

    private fun createColorAnimator(fromColor: Int, toColor: Int, view: ImageView): Animator {
        return ObjectAnimator.ofObject(
            view,
            COLOR_FILTER,
            colorEvaluator,
            fromColor,
            toColor
        )
    }
}