package com.destructo.sushi.ui.anime.topAnime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
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
import com.destructo.sushi.enum.mal.AnimeRankingType.*
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TopAnimeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val topAnimeViewModel:TopAnimeViewModel by viewModels()
    private lateinit var topAnimeRecycler:RecyclerView
    private lateinit var binding:FragmentTopAnimeBinding
    private lateinit var topAnimeAdapter: AnimeRankingAdapter
    private lateinit var animeRankingSpinner: Spinner
    private lateinit var toolbar: Toolbar
    private lateinit var topAnimeProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            topAnimeViewModel.getTopAnime(ALL.value,null,"500")
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

        topAnimeProgress = binding.topAnimeProgressbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        topAnimeAdapter = AnimeRankingAdapter(AnimeIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })

        topAnimeViewModel.topAnimeList.observe(viewLifecycleOwner){resource->
            when(resource?.status){
                Status.LOADING ->{
                    topAnimeProgress.visibility = View.VISIBLE
                    topAnimeRecycler.visibility = View.INVISIBLE
                }
                Status.SUCCESS ->{
                    topAnimeProgress.visibility = View.GONE
                    topAnimeRecycler.visibility = View.VISIBLE
                    resource.data?.let {topAnime->
                        topAnimeAdapter.submitList(topAnime.data)
                        topAnimeRecycler.adapter = topAnimeAdapter
                    }
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)}
            }
        }

        }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        when(parent?.getItemAtPosition(pos).toString()){
            getString(R.string.anime_ranking_all) -> { topAnimeViewModel.getTopAnime(ALL.value,null,"500") }
            getString(R.string.anime_ranking_airing)-> { topAnimeViewModel.getTopAnime(AIRING.value,null,"500")}
            getString(R.string.anime_ranking_upcoming) -> { topAnimeViewModel.getTopAnime(UPCOMING.value,null,"500")}
            getString(R.string.anime_ranking_tv) -> { topAnimeViewModel.getTopAnime(TV.value,null,"500")}
            getString(R.string.anime_ranking_ova) -> { topAnimeViewModel.getTopAnime(OVA.value,null,"500")}
            getString(R.string.anime_ranking_movie) -> { topAnimeViewModel.getTopAnime(MOVIE.value,null,"500")}
            getString(R.string.anime_ranking_special) -> { topAnimeViewModel.getTopAnime(SPECIAL.value,null,"500")}
            getString(R.string.anime_ranking_popularity) -> { topAnimeViewModel.getTopAnime(BY_POPULARITY.value,null,"500")}
            getString(R.string.anime_ranking_favorites) -> { topAnimeViewModel.getTopAnime(FAVORITE.value,null,"500")}

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
