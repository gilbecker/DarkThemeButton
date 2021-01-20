package com.gilbecker.darktheme.example.app.ui.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.gilbecker.darkmode.example.app.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}