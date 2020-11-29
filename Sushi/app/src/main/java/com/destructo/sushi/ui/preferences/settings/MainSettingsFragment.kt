package com.destructo.sushi.ui.preferences.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAboutBinding
import com.destructo.sushi.databinding.FragmentMainSettingsBinding
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_main.*

class MainSettingsFragment : Fragment() {

    private lateinit var binding: FragmentMainSettingsBinding
    private lateinit var toolbar: Toolbar
    private lateinit var lookAndFeel:ConstraintLayout
    private lateinit var appPreference:ConstraintLayout
    private lateinit var aboutApp:ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainSettingsBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner }

        toolbar = binding.toolbar
        lookAndFeel = binding.lookAndFeel
        lookAndFeel.setOnClickListener {
            findNavController().navigate(R.id.action_mainSettingsFragment_to_lookAndFeelFragment)
        }
        appPreference = binding.appSettings
        appPreference.setOnClickListener {
            findNavController().navigate(R.id.action_mainSettingsFragment_to_appPreferenceFragment)
        }
        aboutApp = binding.aboutApp
        aboutApp.setOnClickListener {
            findNavController().navigate(R.id.action_mainSettingsFragment_to_aboutFragment2)
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