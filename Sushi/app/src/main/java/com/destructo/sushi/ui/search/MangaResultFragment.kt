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
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.*
import com.destructo.sushi.adapter.MangaListAdapter
import com.destructo.sushi.databinding.FragmentResultBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MangaResultFragment : BaseFragment() {

    private lateinit var binding: FragmentResultBinding
    private val searchViewModel: SearchViewModel
            by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var mangaListAdapter: MangaListAdapter
    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            searchViewModel.clearMangaList()
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

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context)


        progressBar = binding.resultProgressBar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mangaListAdapter = MangaListAdapter(MalIdListener {
            it?.let { navigateToMangaDetails(it) }
        })
        mangaListAdapter.setListEndListener(object: ListEndListener {
            override fun onEndReached(position: Int) {
                searchViewModel.getNextMangaPage(sharedPref.getBoolean(NSFW_TAG, false))
            }
        })
        resultRecyclerView.adapter = mangaListAdapter


        searchViewModel.searchQuery.observe(viewLifecycleOwner){
            searchViewModel.getMangaResult(
                query = it,
                field = ALL_MANGA_FIELDS,
                limit = DEFAULT_PAGE_LIMIT,
                offset = "",
                nsfw = sharedPref.getBoolean(NSFW_TAG, false))
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

        searchViewModel.mangaSearchResultNext.observe(viewLifecycleOwner){resource->
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
            R.id.mangaDetailsFragment,
            bundleOf(Pair(MANGA_ID_ARG, mangaMalId)),
            getAnimNavOptions()
        )
    }

}