package com.destructo.sushi.ui.anime

import android.content.SharedPreferences
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
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.NSFW_TAG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.AnimeHomeAdapter
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.enum.mal.AnimeRankingType
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_anime.view.*
import kotlinx.android.synthetic.main.inc_currently_airing.view.*
import kotlinx.android.synthetic.main.inc_upcoming_anime.view.*

@AndroidEntryPoint
class AnimeFragment : Fragment() {

    private val animeViewModel: AnimeViewModel by viewModels()
    private lateinit var binding: FragmentAnimeBinding

    private lateinit var upcomingAnimeRecycler: RecyclerView
    private lateinit var currentAiringRecycler: RecyclerView

    private lateinit var upcomingAnimeAdapter: AnimeHomeAdapter
    private lateinit var currentlyAiringAdapter: AnimeHomeAdapter

    private lateinit var upcomingAnimeSeeMore: TextView
    private lateinit var currentlyAiringMore: TextView

    private lateinit var toolbar: Toolbar
    private lateinit var airingAnimeProgressBar:LinearLayout
    private lateinit var upcomingAnimeProgressBar:LinearLayout
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
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        upcomingAnimeRecycler = binding.root.upcomingAnimeRecycler
        upcomingAnimeRecycler.setHasFixedSize(true)
        currentAiringRecycler = binding.root.currentlyAiringRecycler
        currentAiringRecycler.setHasFixedSize(true)


        toolbar = binding.root.anime_frag_toolbar
        airingAnimeProgressBar = binding.root.airing_anime_progress
        upcomingAnimeProgressBar = binding.root.upcoming_anime_progress

        topAnimeCard = binding.topAnimeButton
        seasonalAnimeCard = binding.seasonalAnimeButton
        upcomingAnimeSeeMore = binding.root.upcomingAnimeMore
        currentlyAiringMore = binding.root.currentlyAiringMore

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

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToAnimeDetailFragment(animeMalId)
        )
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.browse_anime)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }

}



