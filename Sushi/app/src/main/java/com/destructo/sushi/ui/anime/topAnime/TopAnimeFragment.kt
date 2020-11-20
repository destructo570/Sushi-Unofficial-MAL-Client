package com.destructo.sushi.ui.anime.topAnime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.*
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentTopAnimeBinding
import com.destructo.sushi.enum.mal.AnimeRankingType.*
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.ListEndListener
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TopAnimeFragment : Fragment(), AdapterView.OnItemSelectedListener, ListEndListener {

    private val topAnimeViewModel:TopAnimeViewModel by viewModels()
    private lateinit var topAnimeRecycler:RecyclerView
    private lateinit var binding:FragmentTopAnimeBinding
    private lateinit var topAnimeAdapter: AnimeRankingAdapter
    private lateinit var animeRankingSpinner: Spinner
    private lateinit var toolbar: Toolbar
    private lateinit var topAnimeProgress: ProgressBar
    private var currentRankingType:String = ALL.value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            val savedString = savedInstanceState.getString("ranking_type")
            savedString?.let {
                currentRankingType = it
            }

        }else{
            topAnimeViewModel.clearAnimeList()
            topAnimeViewModel.getAnimeRankingList(null, DEFAULT_PAGE_LIMIT)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("ranking_type", currentRankingType)
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
        topAnimeAdapter.setListEndListener(this)
        topAnimeAdapter.stateRestorationPolicy = ALLOW
        topAnimeRecycler.adapter = topAnimeAdapter

        topAnimeViewModel.animeRankingList.observe(viewLifecycleOwner) { resource ->
            when (resource?.status) {
                Status.LOADING -> {
                    topAnimeProgress.visibility = View.VISIBLE
                    topAnimeRecycler.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    topAnimeProgress.visibility = View.GONE
                    topAnimeRecycler.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        topAnimeViewModel.topAnimeNextPage.observe(viewLifecycleOwner) { resource ->
            when (resource?.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        topAnimeViewModel.listOfAllTopAnime.observe(viewLifecycleOwner) {
            topAnimeAdapter.submitList(it)
            Timber.e("Changes...")
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        when(parent?.getItemAtPosition(pos).toString()){
            getString(R.string.anime_ranking_all) -> { loadSelectedAnimeList(ALL.value) }
            getString(R.string.anime_ranking_airing)-> { loadSelectedAnimeList(AIRING.value)}
            getString(R.string.anime_ranking_upcoming) -> { loadSelectedAnimeList(UPCOMING.value)}
            getString(R.string.anime_ranking_tv) -> { loadSelectedAnimeList(TV.value)}
            getString(R.string.anime_ranking_ova) -> { loadSelectedAnimeList(OVA.value)}
            getString(R.string.anime_ranking_movie) -> { loadSelectedAnimeList(MOVIE.value)}
            getString(R.string.anime_ranking_special) -> { loadSelectedAnimeList(SPECIAL.value)}
            getString(R.string.anime_ranking_popularity) -> { loadSelectedAnimeList(BY_POPULARITY.value)}
            getString(R.string.anime_ranking_favorites) -> { loadSelectedAnimeList(FAVORITE.value)}

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

    private fun loadSelectedAnimeList(rankingType:String){
        if(currentRankingType != rankingType){
            topAnimeViewModel.clearAnimeList()
            topAnimeViewModel.setRankingType(rankingType)
            topAnimeViewModel.getAnimeRankingList(null,DEFAULT_PAGE_LIMIT)
            currentRankingType = rankingType
        }
    }


    override fun onEndReached(position: Int) {
        topAnimeViewModel.getTopAnimeNextPage()
    }

}
