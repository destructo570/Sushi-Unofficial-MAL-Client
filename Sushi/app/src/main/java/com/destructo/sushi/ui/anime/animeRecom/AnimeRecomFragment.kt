package com.destructo.sushi.ui.anime.animeRecom

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.NSFW_TAG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.AnimeAdapter
import com.destructo.sushi.databinding.FragmentAllAnimeRecomBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AnimeRecomFragment : BaseFragment() {

    private lateinit var binding: FragmentAllAnimeRecomBinding
    private lateinit var animeRecomRecyclerView: RecyclerView
    private lateinit var animeRecomAdapter: AnimeAdapter
    private lateinit var animeRecomProgressBar: ProgressBar
    private lateinit var animeRecomPaginationProgressBar: LinearLayout

    private lateinit var toolbar: Toolbar

    @Inject
    lateinit var sharedPref: SharedPreferences

    private val animeRecomViewModel:AnimeRecomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            animeRecomViewModel.getAnimeRecomm(null, DEFAULT_PAGE_LIMIT, sharedPref.getBoolean(
                NSFW_TAG, false))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAllAnimeRecomBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        animeRecomRecyclerView = binding.animRecyclerMain
        animeRecomRecyclerView.layoutManager = GridLayoutManager(context,3)
        animeRecomRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        animeRecomProgressBar = binding.animeProgressbar
        animeRecomPaginationProgressBar = binding.animePaginationProgress

        toolbar = binding.toolbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()

        animeRecomAdapter = AnimeAdapter(MalIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })


        animeRecomViewModel.animeRecom.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    animeRecomProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        animeRecomProgressBar.visibility = View.GONE
                        animeRecomAdapter.submitList(it)
                        animeRecomRecyclerView.adapter = animeRecomAdapter
                    }
                }
                Status.ERROR -> {
                }
            }

        }

    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment,
            bundleOf(Pair(ANIME_ID_ARG, animeMalId)),
            getAnimNavOptions()
        )
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}