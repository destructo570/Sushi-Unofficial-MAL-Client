package com.destructo.sushi.ui.anime.upcomingAnime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.databinding.FragmentUpcomingAnimeBinding
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.ListEndListener
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_upcoming_anime.view.*
import timber.log.Timber

@AndroidEntryPoint
class UpcomingAnimeFragment : Fragment(), ListEndListener {

    private val upcomingAnimeViewModel:UpcomingAnimeViewModel by viewModels()

    private lateinit var binding:FragmentUpcomingAnimeBinding
    private lateinit var upcomingAdapter:AnimeRankingAdapter
    private lateinit var upcomingAnimeRecycler:RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var upcomingAnimeProgress:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null){
            upcomingAnimeViewModel.clearList()
            upcomingAnimeViewModel.getUpcomingList(null, DEFAULT_PAGE_LIMIT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        upcomingAdapter = AnimeRankingAdapter(AnimeIdListener {
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

        upcomingAnimeViewModel.upcomingAnimeList.observe(viewLifecycleOwner){
            upcomingAdapter.submitList(it)
        }
    }

    private fun navigateToAnimeDetails(animeMalId: Int){
        this.findNavController().navigate(
            UpcomingAnimeFragmentDirections
                .actionUpcomingAnimeFragmentToAnimeDetailFragment(animeMalId)
        )
    }

    private fun setupToolbar(){
        toolbar.setNavigationOnClickListener {view->
            view.findNavController().navigateUp()
        }
    }

    override fun onEndReached(position: Int) {
        upcomingAnimeViewModel.getNextPage()
    }

}
