package com.gilbecker.darktheme.mvc

interface Contract {

    interface View {
        fun hideMoon()
        fun hideSun()
        fun startSunToMoonAnimation()
        fun startMoonToSunAnimation()
    }

    interface Controller {
        fun onCreate()
        fun onAnimationEnded()
        fun onButtonClicked()
    }
}
