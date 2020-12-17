package com.gilbecker

import android.content.Context
import android.util.AttributeSet
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat

class DarkThemePreference(
    context: Context,
    attrs: AttributeSet
) : SwitchPreferenceCompat(context, attrs) {

    private lateinit var darkThemeButton: DarkThemeButton

    init {
        layoutResource = R.layout.preference_layout
    }

    override fun onClick() {
        super.onClick()
        darkThemeButton.performClick()
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        darkThemeButton = holder.findViewById(R.id.darkThemeButton) as DarkThemeButton
    }
}