package com.destructo.sushi.ui.preferences.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.destructo.sushi.NSFW_TAG
import com.destructo.sushi.R
import com.destructo.sushi.util.makeLongToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppPreferenceFragment : PreferenceFragmentCompat(){

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_preference, rootKey)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

        val nsfw = findPreference<SwitchPreferenceCompat>("nsfw")
        nsfw?.setOnPreferenceChangeListener { _, newValue ->
            when (newValue){
                true->{
                    sharedPref.edit()?.putBoolean(NSFW_TAG, true)?.apply()
                    context?.makeLongToast(getString(R.string.ascii_smirk))
                }
                false->{
                    sharedPref.edit()?.putBoolean(NSFW_TAG, false)?.apply()
                }
            }
            return@setOnPreferenceChangeListener true
        }

    }
}
