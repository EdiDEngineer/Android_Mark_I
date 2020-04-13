package com.example.android.androidmarki.ui.home.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

import com.example.android.androidmarki.R

class SettingsFragment : PreferenceFragmentCompat() {


    private lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

}
