package com.destructo.sushi.ui.anime.currentlyAiring

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.NSFW_TAG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.AnimeRankingAdapter
import com.destructo.sushi.databinding.FragmentAllAnimeCurrentlyAiringBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_all_anime_currently_airing.view.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CurrentlyAiringFragment : BaseFragment(), ListEndListener {

    private val currentlyAiringViewModel: CurrentlyAiringViewModel by viewModels()

    private lateinit var binding: FragmentAllAnimeCurrentlyAiringBinding
    private lateinit var currentlyAiringAdapter: AnimeRankingAdapter
    private lateinit var currentlyAiringRecycler: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var currentlyAiringProgress:ProgressBar
    private lateinit var currentlyAiringPagingProgress:ProgressBar

    @Inject
    lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null){
            currentlyAiringViewModel.clearAnimeList()
            currentlyAiringViewModel.getAnimeRankingList(null,
                DEFAULT_PAGE_LIMIT,
                sharedPref.getBoolean(NSFW_TAG, false))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAllAnimeCurrentlyAiringBinding
            .inflate(inflater, container, false).apply {
            lifecycleOwner  = viewLifecycleOwner
        }

        currentlyAiringRecycler = binding.root.currentlyRecyclerMain
        currentlyAiringRecycler.apply {
            addItemDecoration(GridSpacingItemDeco(3,25,true))
            layoutManager = GridLayoutManager(context,3)
        }
        toolbar = binding.toolbar
        currentlyAiringProgress = binding.currentlyAiringProgressbar
        currentlyAiringPagingProgress = binding.airingPaginationProgress

            return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        currentlyAiringAdapter = AnimeRankingAdapter(MalIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        currentlyAiringAdapter.setListEndListener(this)
        currentlyAiringRecycler.adapter = currentlyAiringAdapter

        currentlyAiringViewModel.currentlyAiringList.observe(viewLifecycleOwner){ resource->

            when(resource.status){

                Status.LOADING->{
                    currentlyAiringProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    currentlyAiringProgress.visibility = View.GONE
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }

            }
        }

        currentlyAiringViewModel.nextPage.observe(viewLifecycleOwner){ resource->

            when(resource.status){

                Status.LOADING->{
                    currentlyAiringPagingProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    currentlyAiringPagingProgress.visibility = View.GONE
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }

            }
        }

        currentlyAiringViewModel.currentlyAiring.observe(viewLifecycleOwner){
            currentlyAiringAdapter.submitList(it)
        }

    }

    private fun navigateToAnimeDetails(animeMalId: Int){
        this.findNavController().navigate(
            R.id.animeDetailFragment, bundleOf(Pair(ANIME_ID_ARG, animeMalId)),
            getAnimNavOptions()
        )
    }

    private fun setupToolbar(){
        toolbar.setNavigationOnClickListener {view->
            view.findNavController().navigateUp()
        }
    }

    override fun onEndReached(position: Int) {
        currentlyAiringViewModel.getTopAnimeNextPage(sharedPref.getBoolean(NSFW_TAG, false))
    }

}

