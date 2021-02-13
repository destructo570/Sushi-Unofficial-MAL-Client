package com.destructo.sushi.ui.preferences.aboutApp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.destructo.sushi.BuildConfig
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAboutBinding
import com.destructo.sushi.ui.preferences.donation.DonationActivity
import com.destructo.sushi.util.getColorFromAttr
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding
    private lateinit var toolbar: Toolbar
    private lateinit var twitterButton:Button
    private lateinit var dribbbleButton:Button
    private lateinit var giveFeedback:ConstraintLayout
    private lateinit var discordButton:ConstraintLayout
    private lateinit var rateAppButton:ConstraintLayout
    private lateinit var openSourceLicense:ConstraintLayout
    private lateinit var changelogButton:ConstraintLayout
    private lateinit var donateButton:ConstraintLayout
    private lateinit var appVersionTxt:TextView
    private lateinit var translateSushiView:ConstraintLayout
    private lateinit var creditSection:ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

        toolbar = binding.toolbar
        twitterButton = binding.devTwitterButton
        dribbbleButton = binding.devDribbbleButton
        giveFeedback = binding.giveFeedback
        rateAppButton = binding.rateReviewApp
        discordButton = binding.joinDiscord
        openSourceLicense = binding.openSourceLicense
        appVersionTxt = binding.appVersionDescText
        changelogButton = binding.changelog
        donateButton = binding.donations
        translateSushiView = binding.translateSushi
        creditSection = binding.appCredits

        appVersionTxt.text = BuildConfig.VERSION_NAME

        twitterButton.setOnClickListener {
            openUrl(getString(R.string.social_twitter_link))
        }
        dribbbleButton.setOnClickListener {
            openUrl(getString(R.string.social_dribbble_link))
        }
        giveFeedback.setOnClickListener {
            sendFeedback()
        }
        rateAppButton.setOnClickListener {
            openUrl(getString(R.string.sushi_play_link))
        }
        discordButton.setOnClickListener {
            openUrl(getString(R.string.social_discord_link))
        }
        donateButton.setOnClickListener {
            openDonationActivity()
        }
        openSourceLicense.setOnClickListener {
            startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        }
        changelogButton.setOnClickListener {
            showChangelog()
        }
        translateSushiView.setOnClickListener {
            openUrl(getString(R.string.sushi_translate_link))
        }
        creditSection.setOnClickListener {
            findNavController().navigate(R.id.action_aboutFragment2_to_creditsFragment)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_line)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun openDonationActivity() {
        val intent = Intent(context, DonationActivity::class.java)
        startActivity(intent)
    }

    private fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode(getString(R.string.dev_email_id)) +
                "?subject=" + Uri.encode("Sushi - Unofficial MAL client")
        intent.data = Uri.parse(uriText)
        startActivity(Intent.createChooser(intent, "Send Feedback"))
    }

    private fun showChangelog(){
        val dialog = AlertDialog.Builder(context, R.style.SushiAlertDialog)
            .setTitle("Changelog")
            .setMessage(getString(R.string.latest_changelog))
            .setNegativeButton(R.string.close
            ) { _, _ -> }
            .create()

        dialog.setOnShowListener {

            val view = dialog.window
            view?.setBackgroundDrawable(context?.let { it1 -> ContextCompat.getDrawable(it1,R.drawable.drawable_alert_dialog_bg) })
            context?.let {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(it.getColorFromAttr(R.attr.textColorPrimary))
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(it.getColorFromAttr(R.attr.textColorSecondary))
            }
        }

        dialog.show()
    }

}