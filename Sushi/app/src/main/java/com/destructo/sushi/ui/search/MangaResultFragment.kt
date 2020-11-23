package com.destructo.sushi.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.databinding.FragmentResultBinding
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.listener.ListEndListener
import com.destructo.sushi.ui.manga.mangaDetails.MangaDetailListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MangaResultFragment : Fragment(), ListEndListener {

    private lateinit var binding: FragmentResultBinding
    private val searchViewModel: SearchViewModel
            by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var mangaListAdapter:MangaListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            searchViewModel.clearMangaList()
        }
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
        resultRecyclerView.layoutManager = GridLayoutManager(context,3)
        resultRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))

        progressBar = binding.resultProgressBar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mangaListAdapter = MangaListAdapter(MangaDetailListener {
            it?.let {
                navigateToMangaDetails(it)
            }
        })
        mangaListAdapter.setListEndListener(object: ListEndListener {
            override fun onEndReached(position: Int) {
                searchViewModel.getNextMangaPage()
            }
        })
        resultRecyclerView.adapter = mangaListAdapter


        searchViewModel.searchQuery.observe(viewLifecycleOwner){
            searchViewModel.getMangaResult(
                query = it,
                field = ALL_MANGA_FIELDS,
                limit = DEFAULT_PAGE_LIMIT,
                offset = "")
        }

        searchViewModel.mangaSearchResult.observe(viewLifecycleOwner){resource->
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

        searchViewModel.searchMangaResult.observe(viewLifecycleOwner){
            mangaListAdapter.submitList(it)
        }
    }

    private fun navigateToMangaDetails(mangaMalId: Int){
        this.findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToMangaDetailsFragment(mangaMalId)
        )
    }

    override fun onEndReached(position: Int) {
        searchViewModel.getNextMangaPage()
    }

}