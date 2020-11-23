package com.destructo.sushi.ui.anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.enum.mal.AnimeRankingType
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.Resource
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.adapter.AnimeHomeAdapter
import com.destructo.sushi.ui.anime.adapter.SeasonAnimeHomeAdapter
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import com.destructo.sushi.util.Pkce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_anime.view.*
import kotlinx.android.synthetic.main.inc_currently_airing.view.*
import kotlinx.android.synthetic.main.inc_seasonal_anime.view.*
import kotlinx.android.synthetic.main.inc_top_anime.view.*
import kotlinx.android.synthetic.main.inc_upcoming_anime.view.*
import timber.log.Timber

@AndroidEntryPoint
class AnimeFragment : Fragment() {

    private val animeViewModel: AnimeViewModel by viewModels()
    private lateinit var binding: FragmentAnimeBinding

    private lateinit var topAnimeRecycler: RecyclerView
    private lateinit var upcomingAnimeRecycler: RecyclerView
    private lateinit var currentAiringRecycler: RecyclerView
    private lateinit var seasonalAnimeRecycler: RecyclerView

    private lateinit var topAnimeAdapter: AnimeHomeAdapter
    private lateinit var upcomingAnimeAdapter: AnimeHomeAdapter
    private lateinit var currentlyAiringAdapter: AnimeHomeAdapter
    private lateinit var seasonalAnimeAdapter: SeasonAnimeHomeAdapter

    private lateinit var topAnimeSeeMore: TextView
    private lateinit var upcomingAnimeSeeMore: TextView
    private lateinit var currentlyAiringMore: TextView
    private lateinit var seasonalAnimeMore: TextView

    private lateinit var toolbar: Toolbar
    private lateinit var topAnimeProgressBar:LinearLayout
    private lateinit var airingAnimeProgressBar:LinearLayout
    private lateinit var upcomingAnimeProgressBar:LinearLayout
    private lateinit var seasonAnimeProgressBar:LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            animeViewModel.getTopAnime(AnimeRankingType.ALL.value, null, "25")
            animeViewModel.getUpcomingAnime(AnimeRankingType.UPCOMING.value, null,"25")
            animeViewModel.getCurrentlyAiringAnime(AnimeRankingType.AIRING.value, null,"25")
            animeViewModel.getSeasonalAnime("2020", "fall", null, "25", null)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        topAnimeRecycler = binding.root.topAnimeRecycler
        topAnimeRecycler.setHasFixedSize(true)
        upcomingAnimeRecycler = binding.root.upcomingAnimeRecycler
        upcomingAnimeRecycler.setHasFixedSize(true)
        currentAiringRecycler = binding.root.currentlyAiringRecycler
        currentAiringRecycler.setHasFixedSize(true)
        seasonalAnimeRecycler = binding.root.seasonalAnimeRecycler
        seasonalAnimeRecycler.setHasFixedSize(true)

        toolbar = binding.root.anime_frag_toolbar
        topAnimeProgressBar = binding.root.top_anime_progress
        airingAnimeProgressBar = binding.root.airing_anime_progress
        upcomingAnimeProgressBar = binding.root.upcoming_anime_progress
        seasonAnimeProgressBar = binding.root.seasonal_anime_progress


        topAnimeSeeMore = binding.root.topAnimeMore
        upcomingAnimeSeeMore = binding.root.upcomingAnimeMore
        currentlyAiringMore = binding.root.currentlyAiringMore
        seasonalAnimeMore = binding.root.seasonalAnimeMore

        topAnimeSeeMore.setOnClickListener {
            navigateToTopAnime()
        }
        upcomingAnimeSeeMore.setOnClickListener {
            navigateToUpcomingAnime()
        }
        currentlyAiringMore.setOnClickListener {
            navigateToCurrentlyAiring()
        }
        seasonalAnimeMore.setOnClickListener {
            navigateToSeasonalAnime()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()

        topAnimeAdapter = AnimeHomeAdapter(AnimeIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        upcomingAnimeAdapter = AnimeHomeAdapter(AnimeIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        currentlyAiringAdapter = AnimeHomeAdapter(AnimeIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        seasonalAnimeAdapter = SeasonAnimeHomeAdapter(AnimeIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })

        animeViewModel.topAnime.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    topAnimeProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        topAnimeProgressBar.visibility = View.GONE
                        topAnimeAdapter.submitList(it.data)
                        topAnimeRecycler.adapter = topAnimeAdapter
                    }
                }
                Status.ERROR -> {
                }
            }

        }

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

        animeViewModel.seasonalAnime.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    seasonAnimeProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        seasonAnimeProgressBar.visibility = View.GONE
                        seasonalAnimeAdapter.submitList(it.data)
                        seasonalAnimeRecycler.adapter = seasonalAnimeAdapter
                    }
                }
                Status.ERROR -> {
                }
            }
        }


    }

    private fun navigateToTopAnime() {
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToTopAnimeFragment()
        )
    }

    private fun navigateToUpcomingAnime() {
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToUpcomingAnimeFragment()
        )
    }

    private fun navigateToCurrentlyAiring() {
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToCurrentlyAiring()
        )
    }

    private fun navigateToSeasonalAnime() {
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToSeasonalAnime()
        )
    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToAnimeDetailFragment(animeMalId)
        )
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.title_browse_anime)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }

}



