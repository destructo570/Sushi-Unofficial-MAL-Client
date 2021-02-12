package com.destructo.sushi.ui.anime

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.*
import com.destructo.sushi.R
import com.destructo.sushi.adapter.AnimeHomeAdapter
import com.destructo.sushi.adapter.AnimeHomeRecomAdapter
import com.destructo.sushi.adapter.NewsItemAdapter
import com.destructo.sushi.adapter.PromotionItemAdapter
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.enum.mal.AnimeRankingType
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.listener.MalUrlListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.ListItemHorizontalDecor
import com.facebook.ads.*
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_anime.view.*
import kotlinx.android.synthetic.main.inc_anime_recoms.view.*
import kotlinx.android.synthetic.main.inc_currently_airing.view.*
import kotlinx.android.synthetic.main.inc_latest_news_home.view.*
import kotlinx.android.synthetic.main.inc_promotional_home.view.*
import kotlinx.android.synthetic.main.inc_upcoming_anime.view.*
import timber.log.Timber

@AndroidEntryPoint
class AnimeFragment : Fragment() {

    private val animeViewModel: AnimeViewModel by viewModels()
    private lateinit var binding: FragmentAnimeBinding

    private lateinit var upcomingAnimeRecycler: RecyclerView
    private lateinit var currentAiringRecycler: RecyclerView
    private lateinit var animeRecomRecycler: RecyclerView
    private lateinit var newsRecycler: RecyclerView
    private lateinit var promotionRecycler: RecyclerView

    private lateinit var upcomingAnimeAdapter: AnimeHomeAdapter
    private lateinit var currentlyAiringAdapter: AnimeHomeAdapter
    private lateinit var animeRecomAdapter: AnimeHomeRecomAdapter
    private lateinit var latestNewsAdapter: NewsItemAdapter
    private lateinit var promotionAdapter: PromotionItemAdapter

    private lateinit var upcomingAnimeSeeMore: TextView
    private lateinit var currentlyAiringMore: TextView
    private lateinit var animeRecomMore: TextView

    private lateinit var toolbar: Toolbar
    private lateinit var airingAnimeProgressBar:LinearLayout
    private lateinit var upcomingAnimeProgressBar:LinearLayout
    private lateinit var animeRecommProgressBar:LinearLayout
    private lateinit var newsProgressBar:LinearLayout
    private lateinit var promotionProgressBar:LinearLayout
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var topAnimeCard: MaterialCardView
    private lateinit var seasonalAnimeCard: MaterialCardView
    private lateinit var adContainer: LinearLayout
    private lateinit var adView: AdView


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            setHasOptionsMenu(true)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            animeViewModel.getUpcomingAnime(AnimeRankingType.UPCOMING.value, null,"25", sharedPreferences.getBoolean(
                NSFW_TAG, false))
            animeViewModel.getCurrentlyAiringAnime(AnimeRankingType.AIRING.value, null,"25", sharedPreferences.getBoolean(
                NSFW_TAG, false))
            animeViewModel.getAnimeRecomm(null,"25", sharedPreferences.getBoolean(
                NSFW_TAG, false))
            animeViewModel.getNews()
            animeViewModel.getLatestPromotional()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        upcomingAnimeRecycler = binding.root.upcomingAnimeRecycler
        upcomingAnimeRecycler.setHasFixedSize(true)
        upcomingAnimeRecycler.addItemDecoration(ListItemHorizontalDecor(LIST_SPACE_HEIGHT))
        currentAiringRecycler = binding.root.currentlyAiringRecycler
        currentAiringRecycler.setHasFixedSize(true)
        currentAiringRecycler.addItemDecoration(ListItemHorizontalDecor(LIST_SPACE_HEIGHT))
        animeRecomRecycler = binding.root.animeRecomRecycler
        animeRecomRecycler.setHasFixedSize(true)
        animeRecomRecycler.addItemDecoration(ListItemHorizontalDecor(LIST_SPACE_HEIGHT))
        val snapHelper = PagerSnapHelper()
        newsRecycler = binding.root.newsRecycler
        newsRecycler.setHasFixedSize(true)
        snapHelper.attachToRecyclerView(newsRecycler)
        promotionRecycler = binding.root.promotionRecycler
        val snapHelper2 = PagerSnapHelper()
        promotionRecycler.setHasFixedSize(true)
        snapHelper2.attachToRecyclerView(promotionRecycler)

        toolbar = binding.root.anime_frag_toolbar
        airingAnimeProgressBar = binding.root.airing_anime_progress
        upcomingAnimeProgressBar = binding.root.upcoming_anime_progress
        animeRecommProgressBar = binding.root.anime_recom_progress
        newsProgressBar = binding.root.news_progress
        promotionProgressBar = binding.root.promotion_progress

        topAnimeCard = binding.topAnimeButton
        seasonalAnimeCard = binding.seasonalAnimeButton
        upcomingAnimeSeeMore = binding.root.upcomingAnimeMore
        currentlyAiringMore = binding.root.currentlyAiringMore
        animeRecomMore = binding.root.animeRecomMore

        adContainer = binding.adContainer


        loadAds()
        topAnimeCard.setOnClickListener {
            navigateToTopAnime()
        }
        seasonalAnimeCard.setOnClickListener {
            navigateToSeasonalAnime()
        }
        upcomingAnimeSeeMore.setOnClickListener {
            navigateToUpcomingAnime()
        }
        currentlyAiringMore.setOnClickListener {
            navigateToCurrentlyAiring()
        }
        animeRecomMore.setOnClickListener {
            navigateToAnimeRecom()
        }

        return binding.root
    }

    private fun loadAds() {
        if (!SushiApplication.getContext().queryPurchases()){
            adView = AdView(context, AdPlacementId.getId(), AdSize.BANNER_HEIGHT_50)
            adContainer.addView(adView)
            val adListener = object : AdListener {
                override fun onError(p0: Ad?, p1: AdError?) {
                    Timber.e("Error: ${p1?.errorMessage}")
                }

                override fun onAdLoaded(p0: Ad?) {
                    Timber.e("onAdLoaded")
                    adContainer.visibility = View.VISIBLE
                }

                override fun onAdClicked(p0: Ad?) {
                    Timber.e("onAdClicked")
                }

                override fun onLoggingImpression(p0: Ad?) {
                    Timber.e("onLoggingImpression")
                } }

            adView.loadAd(
                adView.buildLoadAdConfig()
                    .withAdListener(adListener)
                    .build()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()

        upcomingAnimeAdapter = AnimeHomeAdapter(MalIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        currentlyAiringAdapter = AnimeHomeAdapter(MalIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        animeRecomAdapter = AnimeHomeRecomAdapter(MalIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        latestNewsAdapter = NewsItemAdapter(MalUrlListener {
            it?.let { openUrl(it) }
        })
        promotionAdapter = PromotionItemAdapter(MalUrlListener {
            it?.let { openUrl(it) }
        })

        animeViewModel.upcomingAnime.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    upcomingAnimeProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        upcomingAnimeProgressBar.visibility = View.GONE
                        upcomingAnimeAdapter.submitList(it.data)
                        upcomingAnimeRecycler.adapter = upcomingAnimeAdapter
                    }
                }
                Status.ERROR -> {
                }
            }

        }


        animeViewModel.currentlyAiring.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    airingAnimeProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        airingAnimeProgressBar.visibility = View.GONE
                        currentlyAiringAdapter.submitList(it.data)
                        currentAiringRecycler.adapter = currentlyAiringAdapter
                    }
                }
                Status.ERROR -> {
                }
            }

        }

        animeViewModel.animeRecom.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    animeRecommProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        animeRecommProgressBar.visibility = View.GONE
                        animeRecomAdapter.submitList(it)
                        animeRecomRecycler.adapter = animeRecomAdapter
                    }
                }
                Status.ERROR -> {
                }
            }

        }

        animeViewModel.newsList.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    newsProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        newsProgressBar.visibility = View.GONE
                        latestNewsAdapter.submitList(it)
                        newsRecycler.adapter = latestNewsAdapter
                    }
                }
                Status.ERROR -> {
                }
            }

        }


        animeViewModel.promotionList.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    promotionProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        promotionProgressBar.visibility = View.GONE
                        promotionAdapter.submitList(it)
                        promotionRecycler.adapter = promotionAdapter
                    }
                }
                Status.ERROR -> {
                }
            }

        }
    }

    override fun onDestroy() {
        if(::adView.isInitialized && adView != null){
            adView.destroy()
        }
        super.onDestroy()
    }

    private fun navigateToTopAnime() {
        this.findNavController().navigate(R.id.topAnimeFragment)
    }

    private fun navigateToUpcomingAnime() {
        this.findNavController().navigate(R.id.upcomingAnimeFragment)
    }

    private fun navigateToCurrentlyAiring() {
        this.findNavController().navigate(R.id.currentlyAiring)
    }

    private fun navigateToSeasonalAnime() {
        this.findNavController().navigate(R.id.seasonalAnime)
    }

    private fun navigateToAnimeRecom() {
        this.findNavController().navigate(R.id.animeRecomFragment)
    }


    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment, bundleOf(Pair(ANIME_ID_ARG, animeMalId))
        )
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.browse_anime)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }

    private fun openUrl(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabIntent = builder.build()
        customTabIntent.launchUrl(requireContext(), Uri.parse(url))
    }

}



