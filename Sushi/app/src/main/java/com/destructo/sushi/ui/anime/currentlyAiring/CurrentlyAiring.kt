package com.destructo.sushi.ui.anime.currentlyAiring

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentCurrentlyAiringBinding
import com.destructo.sushi.databinding.FragmentUpcomingAnimeBinding
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.ui.anime.AnimeFragmentDirections
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import com.destructo.sushi.ui.anime.animeDetails.AnimeDetailListener
import com.destructo.sushi.ui.anime.upcomingAnime.UpcomingAnimeFragmentArgs
import com.destructo.sushi.ui.anime.upcomingAnime.UpcomingAnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_currently_airing.view.*
import kotlinx.android.synthetic.main.fragment_upcoming_anime.view.*

@AndroidEntryPoint
class CurrentlyAiring : Fragment() {

    private val currentlyAiringViewModel: CurrentlyAiringViewModel by viewModels()

    private lateinit var binding: FragmentCurrentlyAiringBinding
    private lateinit var currentlyAiringArg: AnimeRanking
    private lateinit var currentlyAiringAdapter: AnimeRankingAdapter
    private lateinit var currentlyAiringRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCurrentlyAiringBinding.inflate(inflater, container, false).apply {
            lifecycleOwner  = viewLifecycleOwner
        }

        currentlyAiringArg = CurrentlyAiringArgs.fromBundle(requireArguments()).currentlyAiring
        currentlyAiringRecycler = binding.root.currentlyRecyclerMain
        currentlyAiringRecycler.layoutManager = GridLayoutManager(context,3)

            return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        currentlyAiringViewModel.insertUpcomingAnime(currentlyAiringArg)
        currentlyAiringAdapter = AnimeRankingAdapter(AnimeDetailListener {
            it?.let { navigateToAnimeDetails(it) }
        })

        currentlyAiringViewModel.currentlyAiring.observe(viewLifecycleOwner){
            it?.let {currentlyAiring->
                currentlyAiringAdapter.submitList(currentlyAiring.data)
                currentlyAiringRecycler.apply{
                    adapter = currentlyAiringAdapter
                }
            }
        }
    }


    private fun navigateToAnimeDetails(animeMalId: Int){
        this.findNavController().navigate(
            CurrentlyAiringDirections.actionCurrentlyAiringToAnimeDetailFragment(animeMalId)
        )
    }

    }

