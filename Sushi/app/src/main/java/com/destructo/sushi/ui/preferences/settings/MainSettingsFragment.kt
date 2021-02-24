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
import androidx.preference.PreferenceManager
import com.destructo.sushi.BuildConfig
import com.destructo.sushi.IS_PRO_USER
import com.destructo.sushi.R
import com.destructo.sushi.SushiApplication
import com.destructo.sushi.databinding.FragmentMainSettingsBinding
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.ui.purchaseActivity.PurchaseActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_main.*

class MainSettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentMainSettingsBinding
    private lateinit var toolbar: Toolbar
    private lateinit var lookAndFeel: ConstraintLayout
    private lateinit var appPreference: ConstraintLayout
    private lateinit var aboutApp: ConstraintLayout
    private lateinit var sharedPref: SharedPreferences

    private lateinit var proPromo: MaterialCardView
    private lateinit var buyNowButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    }

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

            if (BuildConfig.DEBUG) {
                findNavController().navigate(
                    R.id.action_mainSettingsFragment_to_lookAndFeelFragment,
                    null,
                    getAnimNavOptions()
                )
            } else {
                if (sharedPref.getBoolean(IS_PRO_USER, false)) {
                    findNavController().navigate(
                        R.id.action_mainSettingsFragment_to_lookAndFeelFragment,
                        null,
                        getAnimNavOptions()
                    )
                } else {
                    startPurchaseActivity()
                }
            }

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
        proPromo = binding.proPromo
        buyNowButton = binding.promoCta

        if (!SushiApplication.getContext().queryPurchases()) {
            proPromo.visibility = View.VISIBLE
        }

        buyNowButton.setOnClickListener {
            startPurchaseActivity()
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


    private fun startPurchaseActivity() {
        val intent = Intent(context, PurchaseActivity::class.java)
        startActivity(intent)
    }


}