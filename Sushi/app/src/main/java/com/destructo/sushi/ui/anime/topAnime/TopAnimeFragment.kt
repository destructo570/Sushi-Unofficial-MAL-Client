package com.destructo.sushi.ui.anime.topAnime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentTopAnimeBinding
import com.destructo.sushi.enum.mal.AnimeRankingType
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopAnimeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val topAnimeViewModel:TopAnimeViewModel by viewModels()
    private lateinit var topAnimeRecycler:RecyclerView
    private lateinit var binding:FragmentTopAnimeBinding
    private lateinit var topAnimeArg: AnimeRanking
    private lateinit var topAnimeAdapter: AnimeRankingAdapter
    private lateinit var animeRankingSpinner: Spinner
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            topAnimeArg = TopAnimeFragmentArgs.fromBundle(requireArguments()).topAnime
            topAnimeViewModel.insertTopAnime(topAnimeArg)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

        toolbar = binding.toolbar
        topAnimeRecycler = binding.topAnimeRecyclerMain
        topAnimeRecycler.layoutManager = GridLayoutManager(context,3)
        topAnimeRecycler.addItemDecoration(GridSpacingItemDeco(3,25,true))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        topAnimeAdapter = AnimeRankingAdapter(AnimeIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })

        topAnimeViewModel.topAnimeList.observe(viewLifecycleOwner){
            it?.let {topAnime->
                topAnimeAdapter.submitList(topAnime.data)
                topAnimeAdapter.stateRestorationPolicy = PREVENT_WHEN_EMPTY
                topAnimeRecycler.apply{
                    adapter = topAnimeAdapter
                    }
                }
            }
        }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        when(parent?.getItemAtPosition(pos).toString()){
            getString(R.string.anime_ranking_all) -> { topAnimeViewModel.getTopAnime(AnimeRankingType.ALL.value,null,"500")}
            getString(R.string.anime_ranking_airing)-> { topAnimeViewModel.getTopAnime(AnimeRankingType.AIRING.value,null,"500")}
            getString(R.string.anime_ranking_upcoming) -> { topAnimeViewModel.getTopAnime(AnimeRankingType.UPCOMING.value,null,"500")}
            getString(R.string.anime_ranking_tv) -> { topAnimeViewModel.getTopAnime(AnimeRankingType.TV.value,null,"500")}
            getString(R.string.anime_ranking_ova) -> { topAnimeViewModel.getTopAnime(AnimeRankingType.OVA.value,null,"500")}
            getString(R.string.anime_ranking_movie) -> { topAnimeViewModel.getTopAnime(AnimeRankingType.MOVIE.value,null,"500")}
            getString(R.string.anime_ranking_special) -> { topAnimeViewModel.getTopAnime(AnimeRankingType.SPECIAL.value,null,"500")}
            getString(R.string.anime_ranking_popularity) -> { topAnimeViewModel.getTopAnime(AnimeRankingType.BY_POPULARITY.value,null,"500")}
            getString(R.string.anime_ranking_favorites) -> { topAnimeViewModel.getTopAnime(AnimeRankingType.FAVORITE.value,null,"500")}

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


    private fun navigateToAnimeDetails(animeMalId: Int){
        this.findNavController().navigate(
            TopAnimeFragmentDirections.actionTopAnimeFragmentToAnimeDetailFragment(animeMalId)
        )
    }

    private fun setupToolbar(){
        toolbar.setNavigationOnClickListener {
                view-> view.findNavController().navigateUp()
        }
    }



}
