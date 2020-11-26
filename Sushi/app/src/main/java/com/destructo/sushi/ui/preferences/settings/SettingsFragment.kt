package com.destructo.sushi.ui.preferences.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.destructo.sushi.CURRENT_THEME
import com.destructo.sushi.NSFW_TAG
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentSettingsBinding
import com.destructo.sushi.enum.AppTheme
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        toolbar = binding.toolbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        val prefFragment = PreferenceFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.preferenceFragment, prefFragment).commit()
    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }
}

class PreferenceFragment : PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)

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
                else ->{
                    sharedPref?.edit()?.putString(CURRENT_THEME, AppTheme.LIGHT.value)?.apply()
                    requireActivity().recreate()
                }
            }
            return@setOnPreferenceChangeListener true
        }

        val nsfw = findPreference<SwitchPreferenceCompat>("nsfw")
        nsfw?.setOnPreferenceChangeListener { _, newValue ->
            when (newValue){
                true->{
                    sharedPref?.edit()?.putBoolean(NSFW_TAG, true)?.apply()
                }
                false->{
                    sharedPref?.edit()?.putBoolean(NSFW_TAG, false)?.apply()
                }
            }
            return@setOnPreferenceChangeListener true
        }




    }

}