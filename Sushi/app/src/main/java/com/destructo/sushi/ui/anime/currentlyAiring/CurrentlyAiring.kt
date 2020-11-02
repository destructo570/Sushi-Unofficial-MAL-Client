package com.destructo.sushi.ui.anime.currentlyAiring

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.FragmentCurrentlyAiringBinding
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_currently_airing.view.*
import timber.log.Timber

@AndroidEntryPoint
class CurrentlyAiring : Fragment() {

    private val currentlyAiringViewModel: CurrentlyAiringViewModel by viewModels()

    private lateinit var binding: FragmentCurrentlyAiringBinding
    private lateinit var currentlyAiringAdapter: AnimeRankingAdapter
    private lateinit var currentlyAiringRecycler: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var currentlyAiringProgress:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            currentlyAiringViewModel.getCurrentlyAiringAnime(null,"500")
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
            setHasFixedSize(true)
            addItemDecoration(GridSpacingItemDeco(3,25,true))
            layoutManager = GridLayoutManager(context,3)
        }
        toolbar = binding.toolbar
        currentlyAiringProgress = binding.currentlyAiringProgressbar

            return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        currentlyAiringAdapter = AnimeRankingAdapter(AnimeIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })

        currentlyAiringViewModel.currentlyAiring.observe(viewLifecycleOwner){resource->

            when(resource.status){

                Status.LOADING->{
                    currentlyAiringProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    currentlyAiringProgress.visibility = View.GONE
                    resource.data?.let {currentlyAiring->
                        currentlyAiringAdapter.submitList(currentlyAiring.data)
                        currentlyAiringRecycler.apply{
                            adapter = currentlyAiringAdapter
                        }
                    }
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }

            }


        }
    }


    private fun navigateToAnimeDetails(animeMalId: Int){
        this.findNavController().navigate(
            CurrentlyAiringDirections.actionCurrentlyAiringToAnimeDetailFragment(animeMalId)
        )
    }

    private fun setupToolbar(){
        toolbar.setNavigationOnClickListener {view->
            view.findNavController().navigateUp()
        }
    }

    }

