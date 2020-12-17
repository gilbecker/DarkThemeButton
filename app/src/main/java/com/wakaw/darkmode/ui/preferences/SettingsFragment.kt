package com.wakaw.darkmode.ui.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.wakaw.darkmode.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}