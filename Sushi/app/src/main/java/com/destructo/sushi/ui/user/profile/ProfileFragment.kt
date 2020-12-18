package com.destructo.sushi.ui.user.profile

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentProfileBinding
import com.destructo.sushi.model.mal.userInfo.AnimeStatistics
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.auth.LoginActivity
import com.destructo.sushi.util.SessionManager
import com.destructo.sushi.util.getColorFromAttr
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var binding: FragmentProfileBinding
    private lateinit var progressBar:ProgressBar
    private lateinit var animeStatDonut:DonutProgressView
    private lateinit var animeStatistics:AnimeStatistics

    private lateinit var watchingText:TextView
    private lateinit var completedText:TextView
    private lateinit var onholdText:TextView
    private lateinit var ptwText:TextView
    private lateinit var droppedText:TextView
    private lateinit var totalText:TextView
    private lateinit var logoutButton: Button
    private lateinit var profileHeader:ConstraintLayout
    private lateinit var navView: NavigationView


    private lateinit var animeDaysTxt:TextView
    private lateinit var animeMeanScoreTxt:TextView
    private lateinit var animeEpisodesTxt:TextView
    private lateinit var animeRewatchTxt:TextView
    @Inject
    lateinit var sessionManager: SessionManager

    private val profileViewModel:ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            profileViewModel.clearUserInfo()
            profileViewModel.getUserInfo("anime_statistics")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        toolbar = binding.toolbar
        progressBar = binding.progressBar
        animeStatDonut = binding.userAnimeStatsDonut
        watchingText = binding.animeWatchingTxt
        completedText = binding.animeCompletedTxt
        onholdText = binding.animeOnholdTxt
        droppedText = binding.animeDroppedTxt
        ptwText = binding.animePtwTxt
        totalText = binding.animeTotalTxt
        animeDaysTxt = binding.animeDays
        animeMeanScoreTxt = binding.animeMeanScore
        animeEpisodesTxt =  binding.animeEpisodesWatched
        animeRewatchTxt = binding.animeRewatchValue
        logoutButton = binding.malLogoutButton

        navView = requireActivity().navigationView
        profileHeader = navView.getHeaderView(0) as ConstraintLayout

        logoutButton.setOnClickListener {

            val dialog = AlertDialog.Builder(context, R.style.SushiAlertDialog)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(R.string.yes
                ) { _, _ ->
                    sessionManager.clearSession()
                    logOutOfApp()
                }
                .setNegativeButton(R.string.no
                ) { _, _ ->

                }
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        profileViewModel.userInformation.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING->{
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    progressBar.visibility = View.GONE

                    resource.data?.let {userInfo->
                        binding.userInfo = userInfo
                        userInfo.animeStatistics?.let{
                            animeStatistics = it
                            setAnimeStats(animeStatistics)
                        }
                    }
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }
            }
        }


    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.user_profile)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }

    private fun setAnimeStats(animeStats: AnimeStatistics) {
        val watching = animeStats.numItemsWatching
        val completed = animeStats.numItemsCompleted
        val onHold = animeStats.numItemsOnHold
        val dropped = animeStats.numItemsDropped
        val planToWatch = animeStats.numItemsPlanToWatch
        val total = animeStats.numItems
        val animeDays = animeStats.numDays
        val animeEp = animeStats.numEpisodes
        val animeMeanScore = animeStats.meanScore
        val animeRewatch = animeStats.numTimesRewatched


        setAnimeStatText(watching, completed, onHold, dropped, planToWatch, total,
            animeDays, animeEp, animeMeanScore, animeRewatch)

        val watchingSection = DonutSection(
            name = "Watching",
            color = Color.parseColor("#00e676"),
            amount = watching?.toFloat() ?: 0.0f
        )
        val completedSection = DonutSection(
            name = "Completed",
            color = Color.parseColor("#3d5afe"),
            amount = completed?.toFloat() ?: 0.0f
        )
        val onHoldSection = DonutSection(
            name = "On Hold",
            color = Color.parseColor("#ffea00"),
            amount = onHold?.toFloat() ?: 0.0f
        )
        val droppedSection = DonutSection(
            name = "Dropped",
            color = Color.parseColor("#ff3d00"),
            amount = dropped?.toFloat() ?: 0.0f
        )
        val planToWatchSection = DonutSection(
            name = "Plan to Watch",
            color = Color.parseColor("#607d8b"),
            amount = planToWatch?.toFloat() ?: 0.0f
        )
        animeStatDonut.cap = 0.0f
        animeStatDonut.submitData(listOf(planToWatchSection, droppedSection, onHoldSection, completedSection, watchingSection))


    }

    private fun setAnimeStatText(
        watching: Int?,
        completed: Int?,
        onHold: Int?,
        dropped: Int?,
        planToWatch: Int?,
        total: Int?,
        animeDays: Double?,
        animeEp: Int?,
        animeMeanScore: Double?,
        animeRewatch: Int?
    ) {
        val  watchingStr = "Watching: $watching"
        val  completedStr = "Completed: $completed"
        val  onHoldStr = "On Hold: $onHold"
        val  droppedStr = "Dropped: $dropped"
        val  planToWatchStr = "Plan To Watch: $planToWatch"
        val totalStr = "Total: $total"
        val daysStr = "$animeDays \nDays"
        val episodeStr = "$animeEp \nEpisodes"
        val meanScoreStr = "$animeMeanScore \nMean Score"
        val rewatchStr = "$animeRewatch \nRewatched"


        watchingText.text = watchingStr
        completedText.text = completedStr
        onholdText.text = onHoldStr
        droppedText.text = droppedStr
        ptwText.text = planToWatchStr
        totalText.text = totalStr
        animeRewatchTxt.text = rewatchStr
        animeDaysTxt.text = daysStr
        animeEpisodesTxt.text = episodeStr
        animeMeanScoreTxt.text = meanScoreStr

    }

    private fun logOutOfApp() {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}