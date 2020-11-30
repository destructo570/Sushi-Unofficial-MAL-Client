package com.destructo.sushi.ui.preferences.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(){

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSettingsBinding


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
        navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_settings) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, _, _ ->

        }

        return binding.root
    }



}