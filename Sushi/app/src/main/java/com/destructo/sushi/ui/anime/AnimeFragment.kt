package com.destructo.sushi.ui.anime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.enum.TopSubtype
import com.destructo.sushi.model.jikan.season.Season
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import com.destructo.sushi.ui.anime.seasonalAnime.SeasonAnimeAdapter
import com.destructo.sushi.ui.anime.topAnime.TopAnimeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.inc_currently_airing.view.*
import kotlinx.android.synthetic.main.inc_seasonal_anime.view.*
import kotlinx.android.synthetic.main.inc_top_anime.view.*
import kotlinx.android.synthetic.main.inc_upcoming_anime.*
import kotlinx.android.synthetic.main.inc_upcoming_anime.view.*
import timber.log.Timber

@AndroidEntryPoint
class AnimeFragment : Fragment() {

    private val animeViewModel:AnimeViewModel by viewModels()
    private lateinit var binding:FragmentAnimeBinding

    private lateinit var topAnimeRecycler:RecyclerView
    private lateinit var upcomingAnimeRecycler:RecyclerView
    private lateinit var currentAiringRecycler:RecyclerView
    private lateinit var seasonalAnimeRecycler:RecyclerView

    private lateinit var topAnimeAdapter:TopAnimeAdapter
    private lateinit var upcomingAnimeAdapter:AnimeRankingAdapter
    private lateinit var currentlyAiringAdapter:TopAnimeAdapter
    private lateinit var seasonalAnimeAdapter:SeasonAnimeAdapter

    private lateinit var topAnimeSeeMore:TextView
    private lateinit var upcomingAnimeSeeMore:TextView
    private lateinit var currentlyAiringMore:TextView
    private lateinit var seasonalAnimeMore:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animeViewModel.getTopAnime("1","")
        animeViewModel.getUpcomingAnime(null,"500")
        animeViewModel.getCurrentlyAiringAnime("1")
        animeViewModel.getSeasonalAnime("2020","fall")
        animeViewModel.getMalAnimeRanking()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = FragmentAnimeBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        topAnimeRecycler = binding.root.topAnimeRecycler
        upcomingAnimeRecycler = binding.root.upcomingAnimeRecycler
        currentAiringRecycler = binding.root.currentlyAiringRecycler
        seasonalAnimeRecycler = binding.root.seasonalAnimeRecycler

        topAnimeSeeMore = binding.root.topAnimeMore
        upcomingAnimeSeeMore = binding.root.upcomingAnimeMore
        currentlyAiringMore = binding.root.currentlyAiringMore
        seasonalAnimeMore = binding.root.seasonalAnimeMore

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        topAnimeAdapter = TopAnimeAdapter()
        upcomingAnimeAdapter = AnimeRankingAdapter()
        currentlyAiringAdapter = TopAnimeAdapter()
        seasonalAnimeAdapter = SeasonAnimeAdapter()


        animeViewModel.topAnimeList.observe(viewLifecycleOwner) {
            it?.let { topAnime ->
                topAnimeAdapter.submitList(topAnime.topAnimeEntity)
                topAnimeRecycler.adapter = topAnimeAdapter
                topAnimeSeeMore.setOnClickListener {
                    navigateToTopAnime(topAnime)
                }
            }
        }

        animeViewModel.upcomingAnime.observe(viewLifecycleOwner) {
            it?.let { upcomingAnime ->
                upcomingAnimeAdapter.submitList(upcomingAnime.data)
                upcomingAnimeRecycler.adapter = upcomingAnimeAdapter
                upcomingAnimeSeeMore.setOnClickListener {
                    navigateToUpcomingAnime(upcomingAnime)
                }
            }
        }


        animeViewModel.currentlyAiring.observe(viewLifecycleOwner){
             it?.let {currentlyAiring->
                 currentlyAiringAdapter.submitList(currentlyAiring.topAnimeEntity)
                 currentAiringRecycler.adapter = currentlyAiringAdapter
                 currentlyAiringMore.setOnClickListener {
                     navigateToCurrentlyAiring(currentlyAiring)
                    }
                }
        }

        animeViewModel.seasonalAnime.observe(viewLifecycleOwner){
            it?.let { seasonalAnime->
                    seasonalAnimeAdapter.submitList(seasonalAnime.animeSubEntities)
                    seasonalAnimeRecycler.apply {
                    adapter = seasonalAnimeAdapter}
                    seasonalAnimeMore.setOnClickListener {
                        navigateToSeasonalAnime(seasonalAnime)
                    }
            }
        }

        animeViewModel.animeRanking.observe(viewLifecycleOwner){
            it?.let { animeRanking->

            }
        }



}
    private fun navigateToTopAnime(topAnime: TopAnime){
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToTopAnimeFragment(topAnime)
        )
    }

    private fun navigateToUpcomingAnime(upcomingAnime: AnimeRanking){
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToUpcomingAnimeFragment(upcomingAnime)
        )
    }


    private fun navigateToCurrentlyAiring(currentlyAiring: TopAnime){
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToCurrentlyAiring(currentlyAiring)
        )
    }


    private fun navigateToSeasonalAnime(seasonalAnime: Season){
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToSeasonalAnime(seasonalAnime)
        )
    }}



