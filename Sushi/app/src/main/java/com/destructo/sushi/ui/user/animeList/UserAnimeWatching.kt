package com.destructo.sushi.ui.user.animeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.adapter.UserAnimeListAdapter
import com.destructo.sushi.databinding.FragmentUserAnimeListBinding
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.listener.AddEpisodeListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import timber.log.Timber


class UserAnimeWatching : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var userAnimeAdapter: UserAnimeListAdapter
    private lateinit var userAnimeRecycler: RecyclerView
    private lateinit var userAnimeProgressbar:ProgressBar
    private lateinit var userAnimePaginationProgressbar: ProgressBar
    private var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            userAnimeViewModel.getUserAnimeList(UserAnimeStatus.WATCHING.value)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAnimeListBinding
            .inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        userAnimeRecycler = binding.userAnimeRecycler
        userAnimeRecycler.setHasFixedSize(true)
        userAnimeRecycler.itemAnimator = null
        userAnimeProgressbar = binding.userAnimeListProgressbar
        userAnimePaginationProgressbar = binding.userAnimeListPaginationProgressbar

        userAnimeRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    position = (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userAnimeAdapter = UserAnimeListAdapter(
            AddEpisodeListener { anime ->
            val episodes = anime?.myAnimeListStatus?.numEpisodesWatched
            val animeId = anime?.id
            if (episodes != null && animeId != null) {
                userAnimeViewModel.addEpisodeAnime(animeId.toString(), episodes + 1)
            }
        },
            MalIdListener {
                it?.let {
                    navigateToAnimeDetails(it)
                }
            }, true
        )
        userAnimeAdapter.setListEndListener(object : ListEndListener {
            override fun onEndReached(position: Int) {
                userAnimeViewModel.getNextPage(UserAnimeStatus.WATCHING.value)
            }

        })
        userAnimeRecycler.adapter = userAnimeAdapter
        userAnimeRecycler.scrollToPosition(position)

        userAnimeViewModel.userAnimeListWatching.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING -> {
                    userAnimeProgressbar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    userAnimeProgressbar.visibility = View.GONE
                    resource.data?.data?.let {
                    }
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        userAnimeViewModel.getUserAnimeByStatus(UserAnimeStatus.WATCHING.value)
            .observe(viewLifecycleOwner){
                userAnimeAdapter.submitList(it)
            }

        userAnimeViewModel.userAnimeStatus.observe(viewLifecycleOwner){ resource->
            when(resource.status){
                Status.LOADING -> {
                    userAnimeProgressbar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    userAnimeProgressbar.visibility = View.GONE
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        userAnimeViewModel.userAnimeListWatchingNext.observe(viewLifecycleOwner){ resource->
            when(resource.status){
                Status.LOADING -> {
                    userAnimePaginationProgressbar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    userAnimePaginationProgressbar.visibility = View.GONE
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        //userAnimeViewModel.getUserAnimeList(UserAnimeStatus.WATCHING.value)

    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            MyAnimeListFragmentDirections.actionMyAnimeListFragmentToAnimeDetailFragment(animeMalId)
        )
    }

}