package com.destructo.sushi.ui.preferences.aboutApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAboutBinding
import com.destructo.sushi.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding
    private lateinit var toolbar: Toolbar
    private lateinit var twitterButton:Button
    private lateinit var dribbbleButton:Button
    private lateinit var discordButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

        toolbar = binding.toolbar
        twitterButton = binding.devTwitterButton
        dribbbleButton = binding.devDribbbleButton
        discordButton = binding.devDiscordButton

        twitterButton.setOnClickListener {
            openUrl(getString(R.string.social_twitter_link))
        }
        dribbbleButton.setOnClickListener {
            openUrl(getString(R.string.social_dribbble_link))
        }
        discordButton.setOnClickListener {
            openUrl(getString(R.string.social_discord_link))
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

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}