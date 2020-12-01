package com.destructo.sushi.ui.anime.currentlyAiring

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.NSFW_TAG
import com.destructo.sushi.databinding.FragmentCurrentlyAiringBinding
import com.destructo.sushi.network.Status
import com.destructo.sushi.adapter.AnimeRankingAdapter
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_currently_airing.view.*
import timber.log.Timber

@AndroidEntryPoint
class CurrentlyAiringFragment : Fragment(), ListEndListener {

    private val currentlyAiringViewModel: CurrentlyAiringViewModel by viewModels()

    private lateinit var binding: FragmentCurrentlyAiringBinding
    private lateinit var currentlyAiringAdapter: AnimeRankingAdapter
    private lateinit var currentlyAiringRecycler: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var currentlyAiringProgress:ProgressBar
    private lateinit var currentlyAiringPagingProgress:ProgressBar
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        if(savedInstanceState == null){
            currentlyAiringViewModel.clearAnimeList()
            currentlyAiringViewModel.getAnimeRankingList(null,
                DEFAULT_PAGE_LIMIT,
                sharedPreferences.getBoolean(NSFW_TAG, false))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCurrentlyAiringBinding
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
            CurrentlyAiringFragmentDirections.actionCurrentlyAiringToAnimeDetailFragment(animeMalId)
        )
    }

    private fun setupToolbar(){
        toolbar.setNavigationOnClickListener {view->
            view.findNavController().navigateUp()
        }
    }

    override fun onEndReached(position: Int) {
        currentlyAiringViewModel.getTopAnimeNextPage(sharedPreferences.getBoolean(NSFW_TAG, false))
    }

}
