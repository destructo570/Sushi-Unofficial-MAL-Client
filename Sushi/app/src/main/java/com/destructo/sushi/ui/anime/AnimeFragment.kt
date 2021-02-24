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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.LIST_SPACE_HEIGHT
import com.destructo.sushi.NSFW_TAG
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
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.ListItemHorizontalDecor
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_anime.view.*
import kotlinx.android.synthetic.main.inc_anime_recoms.view.*
import kotlinx.android.synthetic.main.inc_currently_airing.view.*
import kotlinx.android.synthetic.main.inc_latest_news_home.view.*
import kotlinx.android.synthetic.main.inc_promotional_home.view.*
import kotlinx.android.synthetic.main.inc_upcoming_anime.view.*

@AndroidEntryPoint
class AnimeFragment : BaseFragment() {

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

    private fun navigateToTopAnime() {
        this.findNavController().navigate(R.id.topAnimeFragment,null, getAnimNavOptions())
    }

    private fun navigateToUpcomingAnime() {
        this.findNavController().navigate(R.id.upcomingAnimeFragment,null, getAnimNavOptions())
    }

    private fun navigateToCurrentlyAiring() {
        this.findNavController().navigate(R.id.currentlyAiring,null, getAnimNavOptions())
    }

    private fun navigateToSeasonalAnime() {
        this.findNavController().navigate(R.id.seasonalAnime,null, getAnimNavOptions())
    }

    private fun navigateToAnimeRecom() {
        this.findNavController().navigate(R.id.animeRecomFragment,null, getAnimNavOptions())
    }


    private fun navigateToAnimeDetails(animeMalId: Int) {


        this.findNavController().navigate(
            R.id.animeDetailFragment, bundleOf(Pair(ANIME_ID_ARG, animeMalId)), getAnimNavOptions()
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



