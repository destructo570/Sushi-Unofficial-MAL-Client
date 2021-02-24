package com.destructo.sushi.ui.anime.upcomingAnime

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.NSFW_TAG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.AnimeRankingAdapter
import com.destructo.sushi.databinding.FragmentUpcomingAnimeBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_upcoming_anime.view.*
import timber.log.Timber

@AndroidEntryPoint
class UpcomingAnimeFragment : BaseFragment(), ListEndListener {

    private val upcomingAnimeViewModel:UpcomingAnimeViewModel by viewModels()

    private lateinit var binding:FragmentUpcomingAnimeBinding
    private lateinit var upcomingAdapter:AnimeRankingAdapter
    private lateinit var upcomingAnimeRecycler:RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var upcomingAnimeProgress:ProgressBar
    private lateinit var upcomingAnimePaginationProgress:ProgressBar
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        if(savedInstanceState == null){
            upcomingAnimeViewModel.clearList()
            upcomingAnimeViewModel.getUpcomingList(null,
                DEFAULT_PAGE_LIMIT,
                sharedPreferences.getBoolean(NSFW_TAG, false))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUpcomingAnimeBinding
            .inflate(inflater, container, false).apply {
            lifecycleOwner  = viewLifecycleOwner
        }

        upcomingAnimeRecycler = binding.root.upcomingAnimeRecyclerMain
        upcomingAnimeRecycler.apply {
            layoutManager = GridLayoutManager(context,3)
            addItemDecoration(GridSpacingItemDeco(3,25,true))
        }

        toolbar = binding.toolbar
        upcomingAnimeProgress = binding.upcomingAnimeProgressbar
        upcomingAnimePaginationProgress = binding.upcomingPaginationProgress


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        upcomingAdapter = AnimeRankingAdapter(MalIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        upcomingAdapter.setListEndListener(this)
        upcomingAnimeRecycler.adapter = upcomingAdapter

        upcomingAnimeViewModel.upcomingList.observe(viewLifecycleOwner){ resource->

            when(resource.status){

                Status.LOADING->{
                    upcomingAnimeProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    upcomingAnimeProgress.visibility = View.GONE
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }

            }
        }

        upcomingAnimeViewModel.nextPage.observe(viewLifecycleOwner){ resource->

            when(resource.status){

                Status.LOADING->{
                    upcomingAnimePaginationProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    upcomingAnimePaginationProgress.visibility = View.GONE
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }

            }
        }

        upcomingAnimeViewModel.upcomingAnimeList.observe(viewLifecycleOwner){
            upcomingAdapter.submitList(it)
        }
    }

    private fun navigateToAnimeDetails(animeMalId: Int){
        this.findNavController().navigate(
            R.id.animeDetailFragment,
            bundleOf(Pair(ANIME_ID_ARG, animeMalId)),
            getAnimNavOptions()
        )
    }

    private fun setupToolbar(){
        toolbar.setNavigationOnClickListener {view->
            view.findNavController().navigateUp()
        }
    }

    override fun onEndReached(position: Int) {
        upcomingAnimeViewModel.getNextPage(sharedPreferences.getBoolean(NSFW_TAG, false))
    }

}
