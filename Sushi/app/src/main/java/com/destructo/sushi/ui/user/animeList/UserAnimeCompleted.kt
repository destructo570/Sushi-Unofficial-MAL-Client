package com.destructo.sushi.ui.user.animeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.ALLOW
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.LIST_SPACE_HEIGHT
import com.destructo.sushi.R
import com.destructo.sushi.adapter.UserAnimeListAdapter
import com.destructo.sushi.databinding.FragmentUserAnimeListBinding
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.listener.AddEpisodeListenerUA
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.database.UserAnimeEntity
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.ListItemVerticalDecor
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserAnimeCompleted : BaseFragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var userAnimeRecycler: RecyclerView
    private lateinit var userAnimeAdapter: UserAnimeListAdapter
    private lateinit var userAnimeProgressbar: ProgressBar
    private lateinit var userAnimePaginationProgressbar: ProgressBar
    private var calledOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        if(!calledOnce) {
            calledOnce = true

            binding = FragmentUserAnimeListBinding
                .inflate(inflater, container, false).apply {
                    lifecycleOwner = viewLifecycleOwner
                }

            userAnimeRecycler = binding.userAnimeRecycler
            userAnimeRecycler.addItemDecoration(ListItemVerticalDecor(LIST_SPACE_HEIGHT))
            userAnimeRecycler.setHasFixedSize(true)
            userAnimeRecycler.itemAnimator = null
            userAnimeProgressbar = binding.userAnimeListProgressbar
            userAnimePaginationProgressbar = binding.userAnimeListPaginationProgressbar

            userAnimeAdapter = UserAnimeListAdapter(
                AddEpisodeListenerUA { anime ->
                    val episodes = anime?.myAnimeListStatus?.numEpisodesWatched
                    val animeId = anime?.malId
                    if (episodes != null && animeId != null) {
                        userAnimeViewModel.addEpisodeAnime(animeId.toString(), episodes + 1, null)
                    }
                },
                MalIdListener {
                    it?.let {
                        navigateToAnimeDetails(it)
                    }
                }, false
            )
            userAnimeAdapter.setListEndListener(object : ListEndListener {
                override fun onEndReached(position: Int) {
                    //userAnimeViewModel.getNextPage(UserAnimeStatus.COMPLETED.value)
                }

            })
            userAnimeAdapter.stateRestorationPolicy = ALLOW
            userAnimeRecycler.adapter = userAnimeAdapter


        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            userAnimeViewModel.userAnimeListState.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
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
        userAnimeViewModel.userAnimeList.observe(viewLifecycleOwner){
            val completedList = mutableListOf<UserAnimeEntity>()
            for (anime in it){
                if (anime.myAnimeListStatus?.status == UserAnimeStatus.COMPLETED.value){
                    completedList.add(anime)
                }
            }
            userAnimeAdapter.submitList(completedList)
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

