package com.destructo.sushi.ui.anime.seasonalAnime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentSeasonalAnimeBinding
import com.destructo.sushi.databinding.FragmentUpcomingAnimeBinding
import com.destructo.sushi.model.season.Season
import com.destructo.sushi.model.top.TopAnime
import com.destructo.sushi.ui.anime.topAnime.TopAnimeAdapter
import com.destructo.sushi.ui.anime.upcomingAnime.UpcomingAnimeFragmentArgs
import com.destructo.sushi.ui.anime.upcomingAnime.UpcomingAnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_seasonal_anime.view.*
import kotlinx.android.synthetic.main.fragment_upcoming_anime.view.*

@AndroidEntryPoint
class SeasonalAnime : Fragment() {
    private val seasonAnimeViewModel: SeasonalAnimeViewModel by viewModels()

    private lateinit var binding:FragmentSeasonalAnimeBinding
    private lateinit var seasonAnimeArg: Season
    private lateinit var seasonAdapter: SeasonAnimeAdapter
    private lateinit var seasonAnimeRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSeasonalAnimeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner  = viewLifecycleOwner
        }

        seasonAnimeArg = SeasonalAnimeArgs.fromBundle(requireArguments()).seasonalAnime
        seasonAnimeRecycler = binding.root.seasonalAnimeRecyclerMain
        seasonAnimeRecycler.layoutManager = GridLayoutManager(context,3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        seasonAnimeViewModel.insertSeasonAnime(seasonAnimeArg)
        seasonAdapter = SeasonAnimeAdapter()

        seasonAnimeViewModel.seasonalAnime.observe(viewLifecycleOwner){
            it?.let {seasonAnime->
                seasonAdapter.submitList(seasonAnime.animeSubEntities)
                seasonAnimeRecycler.apply{
                    adapter = seasonAdapter
                }
            }
        }
    }
    }
