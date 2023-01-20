package com.example.cardapp.presentation.fragments.drawer


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.cardapp.R

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
    }
}
