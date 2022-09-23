package com.destructo.sushi.ui.preferences.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentMainSettingsBinding
import com.destructo.sushi.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainSettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentMainSettingsBinding
    private lateinit var toolbar: Toolbar
    private lateinit var lookAndFeel: ConstraintLayout
    private lateinit var appPreference: ConstraintLayout
    private lateinit var aboutApp: ConstraintLayout
    private lateinit var faqSection: ConstraintLayout

    @Inject
    lateinit var sharedPref: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainSettingsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        toolbar = binding.toolbar
        lookAndFeel = binding.lookAndFeel
        lookAndFeel.setOnClickListener(fun(_: View) {

            findNavController().navigate(
                R.id.action_mainSettingsFragment_to_lookAndFeelFragment,
                null,
                getAnimNavOptions()
            )
        })
        appPreference = binding.appSettings
        appPreference.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainSettingsFragment_to_appPreferenceFragment,
                null,
                getAnimNavOptions()
            )
        }
        aboutApp = binding.aboutApp
        aboutApp.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainSettingsFragment_to_aboutFragment2,
                null,
                getAnimNavOptions()
            )
        }
        faqSection = binding.faq
        faqSection.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainSettingsFragment_to_faqFragment,
                null,
                getAnimNavOptions()
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
    }


    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }


}