package com.destructo.sushi.ui.preferences.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.destructo.sushi.CURRENT_THEME
import com.destructo.sushi.R
import com.destructo.sushi.enum.AppTheme

class LookAndFeelFragment : PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.look_and_feel_preference, rootKey)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

        val sharedPref: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)

        val theme = findPreference<ListPreference>("theme")
        theme?.setOnPreferenceChangeListener { _, newValue ->
            when (newValue){
                getString(R.string.theme_night)->{
                    sharedPref?.edit()?.putString(CURRENT_THEME, AppTheme.NIGHT.value)?.apply()
                    requireActivity().recreate()
                }
                getString(R.string.theme_light)->{
                    sharedPref?.edit()?.putString(CURRENT_THEME, AppTheme.LIGHT.value)?.apply()
                    requireActivity().recreate()
                }
                getString(R.string.theme_amoled)->{
                    sharedPref?.edit()?.putString(CURRENT_THEME, AppTheme.AMOLED.value)?.apply()
                    requireActivity().recreate()
                }
                getString(R.string.theme_amoled_lime)->{
                    sharedPref?.edit()?.putString(CURRENT_THEME, AppTheme.AMOLED_LIME.value)?.apply()
                    requireActivity().recreate()
                }
                getString(R.string.theme_amoled_sakura)->{
                    sharedPref?.edit()?.putString(CURRENT_THEME, AppTheme.AMOLED_SAKURA.value)?.apply()
                    requireActivity().recreate()
                }
                getString(R.string.theme_amoled_cyan)->{
                        sharedPref?.edit()?.putString(CURRENT_THEME, AppTheme.AMOLED_CYAN.value)?.apply()
                    requireActivity().recreate()
                }
                else ->{
                    sharedPref?.edit()?.putString(CURRENT_THEME, AppTheme.LIGHT.value)?.apply()
                    requireActivity().recreate()
                }
            }
            return@setOnPreferenceChangeListener true
        }

    }

}