package com.destructo.sushi.ui.preferences.aboutApp

import android.os.Bundle
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.destructo.sushi.R

class CreditsFragment :  PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_credits, rootKey)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

    }

}