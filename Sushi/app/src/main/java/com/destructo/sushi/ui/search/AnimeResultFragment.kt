package com.destructo.sushi.ui.search

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.NSFW_TAG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.AnimeListAdapter
import com.destructo.sushi.databinding.FragmentResultBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AnimeResultFragment : BaseFragment() {

    private lateinit var binding: FragmentResultBinding
    private val searchViewModel: SearchViewModel
            by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var resultRecyclerView:RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var animeListAdapter: AnimeListAdapter

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
        searchViewModel.clearAnimeList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        resultRecyclerView = binding.resultRecycler
        resultRecyclerView.layoutManager = GridLayoutManager(context,3)
        resultRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))

        progressBar = binding.resultProgressBar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animeListAdapter = AnimeListAdapter(MalIdListener {
            it?.let {navigateToAnimeDetails(it)}
        })
        animeListAdapter.setListEndListener(object: ListEndListener {
            override fun onEndReached(position: Int) {
                searchViewModel.getNextAnimePage(sharedPref.getBoolean(NSFW_TAG, false))
            }

        })
        resultRecyclerView.adapter = animeListAdapter

        searchViewModel.animeSearchResult.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING ->{
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    progressBar.visibility = View.GONE
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        searchViewModel.animeSearchResultNext.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING ->{
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    progressBar.visibility = View.GONE
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        searchViewModel.searchAnimeResult.observe(viewLifecycleOwner){
            animeListAdapter.submitList(it)
        }

    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment,
            bundleOf(Pair(ANIME_ID_ARG, animeMalId)),
            getAnimNavOptions()
        )
    }


}