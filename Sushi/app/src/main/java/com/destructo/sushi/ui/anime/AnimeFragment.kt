package com.destructo.sushi.ui.anime

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.enum.TopSubtype
import com.destructo.sushi.enum.mal.AnimeRankingType
import com.destructo.sushi.model.jikan.season.Season
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import com.destructo.sushi.ui.anime.seasonalAnime.SeasonAnimeAdapter
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

    private lateinit var topAnimeAdapter:AnimeRankingAdapter
    private lateinit var upcomingAnimeAdapter:AnimeRankingAdapter
    private lateinit var currentlyAiringAdapter:AnimeRankingAdapter
    private lateinit var seasonalAnimeAdapter:SeasonAnimeAdapter

    private lateinit var topAnimeSeeMore:TextView
    private lateinit var upcomingAnimeSeeMore:TextView
    private lateinit var currentlyAiringMore:TextView
    private lateinit var seasonalAnimeMore:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        animeViewModel.getTopAnime(AnimeRankingType.ALL.value,null,"500")
        animeViewModel.getUpcomingAnime(null,"500")
        animeViewModel.getCurrentlyAiringAnime(null,"500")
        animeViewModel.getSeasonalAnime("2020","fall",null,"100",null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
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

        topAnimeAdapter = AnimeRankingAdapter()
        upcomingAnimeAdapter = AnimeRankingAdapter()
        currentlyAiringAdapter = AnimeRankingAdapter()
        seasonalAnimeAdapter = SeasonAnimeAdapter()


        animeViewModel.topAnimeList.observe(viewLifecycleOwner) {
            it?.let { topAnime ->
                topAnimeAdapter.submitList(topAnime.data)
                topAnimeRecycler.apply{
                    setHasFixedSize(true)
                    adapter = topAnimeAdapter }

                topAnimeSeeMore.setOnClickListener {
                    navigateToTopAnime(topAnime)
                }
            }
        }

        animeViewModel.upcomingAnime.observe(viewLifecycleOwner) {
            it?.let { upcomingAnime ->
                upcomingAnimeAdapter.submitList(upcomingAnime.data)
                upcomingAnimeRecycler.apply{
                    setHasFixedSize(true)
                    adapter = upcomingAnimeAdapter}

                upcomingAnimeSeeMore.setOnClickListener {
                    navigateToUpcomingAnime(upcomingAnime)
                }
            }
        }


        animeViewModel.currentlyAiring.observe(viewLifecycleOwner){
             it?.let {currentlyAiring->
                 currentlyAiringAdapter.submitList(currentlyAiring.data)
                 currentAiringRecycler.apply{
                     setHasFixedSize(true)
                     adapter = currentlyAiringAdapter}
                 currentlyAiringMore.setOnClickListener {
                     navigateToCurrentlyAiring(currentlyAiring)
                    }
                }
        }

        animeViewModel.seasonalAnime.observe(viewLifecycleOwner){
            it?.let { seasonalAnime->
                    seasonalAnimeAdapter.submitList(seasonalAnime.data)
                    seasonalAnimeRecycler.apply {
                        setHasFixedSize(true)
                    adapter = seasonalAnimeAdapter}
                    seasonalAnimeMore.setOnClickListener {
                        navigateToSeasonalAnime(seasonalAnime)
                    }
            }
        }


}
    private fun navigateToTopAnime(topAnime: AnimeRanking){
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToTopAnimeFragment(topAnime)
        )
    }

    private fun navigateToUpcomingAnime(upcomingAnime: AnimeRanking){
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToUpcomingAnimeFragment(upcomingAnime)
        )
    }


    private fun navigateToCurrentlyAiring(currentlyAiring: AnimeRanking){
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToCurrentlyAiring(currentlyAiring)
        )
    }


    private fun navigateToSeasonalAnime(seasonalAnime: SeasonalAnime){
        this.findNavController().navigate(
            AnimeFragmentDirections.actionAnimeFragmentToSeasonalAnime(seasonalAnime)
        )
    }}



