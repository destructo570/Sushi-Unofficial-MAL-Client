package com.destructo.sushi.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentCharacterBinding
import com.destructo.sushi.databinding.FragmentResultBinding
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.AnimeFragmentDirections
import com.destructo.sushi.ui.anime.characterDetails.CharacterViewModel
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AnimeResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val searchViewModel: SearchViewModel
            by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var resultRecyclerView:RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var animeListAdapter:AnimeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        resultRecyclerView = binding.resultRecycler
        resultRecyclerView.setHasFixedSize(true)
        resultRecyclerView.layoutManager = GridLayoutManager(context,3)
        resultRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))

        progressBar = binding.resultProgressBar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animeListAdapter = AnimeListAdapter(AnimeIdListener {
            it?.let {navigateToAnimeDetails(it)}
        })

        searchViewModel.searchQuery.observe(viewLifecycleOwner){
            searchViewModel.getAnimeResult(
                query = it,
                field = ALL_ANIME_FIELDS,
                limit = "100",
                offset = "")
        }

        searchViewModel.animeSearchResult.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING ->{
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    progressBar.visibility = View.GONE
                    resource.data?.data?.let{animeList->
                        animeListAdapter.submitList(animeList)
                        resultRecyclerView.adapter = animeListAdapter
                    }

                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }

            }
        }

    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToAnimeDetailFragment(animeMalId)
        )
    }

}