package com.destructo.sushi.ui.anime.topAnime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentTopAnimeBinding
import com.destructo.sushi.enum.mal.AnimeRankingType
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopAnimeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val topAnimeViewModel:TopAnimeViewModel by viewModels()
    private lateinit var topAnimeRecycler:RecyclerView
    private lateinit var binding:FragmentTopAnimeBinding
    private lateinit var topAnimeArg: AnimeRanking
    private lateinit var topAnimeAdapter: AnimeRankingAdapter
    private lateinit var animeRankingSpinner: Spinner



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentTopAnimeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        animeRankingSpinner = binding.animeRankingSpinner
        context?.let { ArrayAdapter.createFromResource(
            it,R.array.anime_ranking_type,android.R.layout.simple_spinner_item).also {adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            animeRankingSpinner.adapter = adapter
            animeRankingSpinner.onItemSelectedListener = this
        } }

        topAnimeRecycler = binding.topAnimeRecyclerMain
        topAnimeRecycler.layoutManager = GridLayoutManager(context,3)

        topAnimeArg = TopAnimeFragmentArgs.fromBundle(requireArguments()).topAnime

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        topAnimeViewModel.insertTopAnime(topAnimeArg)
        topAnimeAdapter = AnimeRankingAdapter()

        topAnimeViewModel.topAnimeList.observe(viewLifecycleOwner){
            it?.let {topAnime->
                topAnimeAdapter.submitList(topAnime.data)
                topAnimeRecycler.apply{
                    adapter = topAnimeAdapter
                    }
                }
            }
        }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        when(parent?.getItemAtPosition(pos).toString()){
            "All" -> { topAnimeViewModel.getTopAnime(AnimeRankingType.ALL.value,null,"500")}
            "Airing" -> { topAnimeViewModel.getTopAnime(AnimeRankingType.AIRING.value,null,"500")}
            "Upcoming" -> { topAnimeViewModel.getTopAnime(AnimeRankingType.UPCOMING.value,null,"500")}
            "Tv" -> { topAnimeViewModel.getTopAnime(AnimeRankingType.TV.value,null,"500")}
            "Ova" -> { topAnimeViewModel.getTopAnime(AnimeRankingType.OVA.value,null,"500")}
            "Movie" -> { topAnimeViewModel.getTopAnime(AnimeRankingType.MOVIE.value,null,"500")}
            "Special" -> { topAnimeViewModel.getTopAnime(AnimeRankingType.SPECIAL.value,null,"500")}
            "By Popularity" -> { topAnimeViewModel.getTopAnime(AnimeRankingType.BY_POPULARITY.value,null,"500")}
            "Favorite" -> { topAnimeViewModel.getTopAnime(AnimeRankingType.FAVORITE.value,null,"500")}

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}
