package com.destructo.sushi.ui.user.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.destructo.sushi.databinding.FragmentProfileStatsBinding
import com.destructo.sushi.model.jikan.user.AnimeStats
import com.destructo.sushi.model.jikan.user.MangaStats
import com.destructo.sushi.network.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.inc_user_anime_stats.view.*
import kotlinx.android.synthetic.main.inc_user_manga_stats.view.*
import timber.log.Timber

@AndroidEntryPoint
class ProfileStatsFragment : Fragment() {

    private lateinit var animeStatDonut: DonutProgressView
    private lateinit var mangaStatDonut: DonutProgressView
    private lateinit var animeStatistics: AnimeStats
    private lateinit var mangaStatistics: MangaStats

    private lateinit var watchingText: TextView
    private lateinit var completedText: TextView
    private lateinit var onholdText: TextView
    private lateinit var ptwText: TextView
    private lateinit var droppedText: TextView
    private lateinit var totalText: TextView

    private lateinit var animeDaysTxt:TextView
    private lateinit var animeMeanScoreTxt:TextView
    private lateinit var animeEpisodesTxt:TextView
    private lateinit var animeRewatchTxt:TextView

    private lateinit var mangaDaysTxt:TextView
    private lateinit var mangaMeanScoreTxt:TextView
    private lateinit var mangaEpisodesTxt:TextView
    private lateinit var mangaRewatchTxt:TextView

    private lateinit var binding: FragmentProfileStatsBinding
    private val profileViewModel:ProfileViewModel by viewModels(
        ownerProducer = {requireParentFragment()})


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileStatsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        animeStatDonut = binding.root.user_anime_stats_donut
        mangaStatDonut = binding.root.user_manga_stats_donut
        watchingText = binding.root.anime_watching_txt
        completedText = binding.root.anime_completed_txt
        onholdText = binding.root.anime_onhold_txt
        droppedText = binding.root.anime_dropped_txt
        ptwText = binding.root.anime_ptw_txt
        totalText = binding.root.anime_total_txt
        animeDaysTxt = binding.root.anime_days
        animeMeanScoreTxt = binding.root.anime_mean_score
        animeEpisodesTxt =  binding.root.anime_episodes_watched
        animeRewatchTxt = binding.root.anime_rewatch_value
        mangaDaysTxt = binding.root.manga_days
        mangaMeanScoreTxt = binding.root.manga_mean_score
        mangaEpisodesTxt = binding.root.manga_episodes_watched
        mangaRewatchTxt = binding.root.manga_rewatch_value


        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profileViewModel.userInformation.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING->{
                }
                Status.SUCCESS->{
                    resource.data?.let {userInfo->
                        binding.userInfo = userInfo
                        userInfo.animeStats?.let{
                            animeStatistics = it
                            setAnimeStats(animeStatistics)
                        }
                        userInfo.mangaStats?.let{
                            mangaStatistics = it
                            setMangaStats(mangaStatistics)
                        }
                    }
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }
            }
        }
    }



    private fun setAnimeStats(animeStats: AnimeStats) {
        val watching = animeStats.watching
        val completed = animeStats.completed
        val onHold = animeStats.onHold
        val dropped = animeStats.dropped
        val planToWatch = animeStats.planToWatch
        val total = animeStats.totalEntries
        val animeDays = animeStats.daysWatched
        val animeEp = animeStats.episodesWatched
        val animeMeanScore = animeStats.meanScore
        val animeRewatch = animeStats.rewatched


        setAnimeStatText(animeDays, animeEp, animeMeanScore, animeRewatch)

        val watchingSection = DonutSection(
            name = "Watching",
            color = Color.parseColor("#78e08f"),
            amount = watching?.toFloat() ?: 0.0f
        )
        val readingSection = DonutSection(
            name = "Reading",
            color = Color.parseColor("#78e08f"),
            amount = watching?.toFloat() ?: 0.0f
        )
        val planToReadSection = DonutSection(
            name = "Plan to Watch",
            color = Color.parseColor("#9cafb5"),
            amount = planToWatch?.toFloat() ?: 0.0f
        )
        val completedSection = DonutSection(
            name = "Completed",
            color = Color.parseColor("#6a89cc"),
            amount = completed?.toFloat() ?: 0.0f
        )
        val onHoldSection = DonutSection(
            name = "On Hold",
            color = Color.parseColor("#fad390"),
            amount = onHold?.toFloat() ?: 0.0f
        )
        val droppedSection = DonutSection(
            name = "Dropped",
            color = Color.parseColor("#e55039"),
            amount = dropped?.toFloat() ?: 0.0f
        )
        val planToWatchSection = DonutSection(
            name = "Plan to Watch",
            color = Color.parseColor("#9cafb5"),
            amount = planToWatch?.toFloat() ?: 0.0f
        )
        animeStatDonut.cap = 0.0f
        animeStatDonut.submitData(listOf(planToWatchSection, droppedSection, onHoldSection, completedSection, watchingSection))
        mangaStatDonut.cap = 0.0f
        mangaStatDonut.submitData(listOf(planToReadSection, droppedSection, onHoldSection, completedSection, readingSection))


    }

    private fun setMangaStats(mangaStats: MangaStats) {
        val reading = mangaStats.reading
        val completed = mangaStats.completed
        val onHold = mangaStats.onHold
        val dropped = mangaStats.dropped
        val planToRead = mangaStats.planToRead
        val total = mangaStats.totalEntries
        val mangaDays = mangaStats.daysRead
        val mangaCh = mangaStats.chaptersRead
        val mangaMeanScore = mangaStats.meanScore
        val mangaRewatch = mangaStats.reread


        setMangaStatText(mangaDays, mangaCh, mangaMeanScore, mangaRewatch)

        val readingSection = DonutSection(
            name = "Reading",
            color = Color.parseColor("#78e08f"),
            amount = reading?.toFloat() ?: 0.0f
        )
        val planToReadSection = DonutSection(
            name = "Plan to Watch",
            color = Color.parseColor("#9cafb5"),
            amount = planToRead?.toFloat() ?: 0.0f
        )
        val completedSection = DonutSection(
            name = "Completed",
            color = Color.parseColor("#6a89cc"),
            amount = completed?.toFloat() ?: 0.0f
        )
        val onHoldSection = DonutSection(
            name = "On Hold",
            color = Color.parseColor("#fad390"),
            amount = onHold?.toFloat() ?: 0.0f
        )
        val droppedSection = DonutSection(
            name = "Dropped",
            color = Color.parseColor("#e55039"),
            amount = dropped?.toFloat() ?: 0.0f
        )
        mangaStatDonut.cap = 0.0f
        mangaStatDonut.submitData(listOf(planToReadSection, droppedSection, onHoldSection, completedSection, readingSection))


    }

    private fun setAnimeStatText(
        animeDays: Double?,
        animeEp: Int?,
        animeMeanScore: Double?,
        animeRewatch: Int?
    ) {

        val daysStr = "$animeDays \nDays"
        val episodeStr = "$animeEp \nEpisodes"
        val meanScoreStr = "$animeMeanScore \nMean Score"
        val rewatchStr = "$animeRewatch \nRewatched"

        animeRewatchTxt.text = rewatchStr
        animeDaysTxt.text = daysStr
        animeEpisodesTxt.text = episodeStr
        animeMeanScoreTxt.text = meanScoreStr

    }

    private fun setMangaStatText(

        animeDays: Double?,
        animeEp: Int?,
        animeMeanScore: Double?,
        animeRewatch: Int?
    ) {

        val daysStr = "$animeDays \nDays"
        val episodeStr = "$animeEp \nChapters"
        val meanScoreStr = "$animeMeanScore \nMean Score"
        val rewatchStr = "$animeRewatch \nReread"

        mangaRewatchTxt.text = rewatchStr
        mangaDaysTxt.text = daysStr
        mangaEpisodesTxt.text = episodeStr
        mangaMeanScoreTxt.text = meanScoreStr

    }



}