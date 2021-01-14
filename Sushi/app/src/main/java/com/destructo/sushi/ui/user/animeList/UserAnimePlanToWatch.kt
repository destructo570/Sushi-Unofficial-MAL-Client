package com.destructo.sushi.ui.user.animeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.ALLOW
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.UserAnimeListAdapter
import com.destructo.sushi.databinding.FragmentUserAnimeListBinding
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.listener.AddEpisodeListener
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserAnimePlanToWatch : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var userAnimeAdapter: UserAnimeListAdapter
    private lateinit var userAnimeRecycler: RecyclerView
    private lateinit var userAnimeProgressbar: ProgressBar
    private lateinit var userAnimePaginationProgressbar: ProgressBar
    private var calledOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) {
            userAnimeViewModel.getUserAnimeList(UserAnimeStatus.PLAN_TO_WATCH.value)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(!calledOnce) {
            calledOnce = true

            binding = FragmentUserAnimeListBinding
                .inflate(inflater, container, false).apply {
                    lifecycleOwner = viewLifecycleOwner
                }

            userAnimeRecycler = binding.userAnimeRecycler
            userAnimeRecycler.setHasFixedSize(true)
            userAnimeRecycler.itemAnimator = null
            userAnimeProgressbar = binding.userAnimeListProgressbar
            userAnimePaginationProgressbar = binding.userAnimeListPaginationProgressbar

            userAnimeAdapter = UserAnimeListAdapter(
                AddEpisodeListener { anime ->
                    val episodes = anime?.myAnimeListStatus?.numEpisodesWatched
                    val animeId = anime?.id
                    if (episodes != null && animeId != null){
                        userAnimeViewModel.addEpisodeAnime(animeId.toString(),episodes+1, null)
                    }
                },
                MalIdListener {
                    it?.let{
                        navigateToAnimeDetails(it)
                    }
                }, false)
            userAnimeAdapter.setListEndListener(object : ListEndListener {
                override fun onEndReached(position: Int) {
                    userAnimeViewModel.getNextPage(UserAnimeStatus.PLAN_TO_WATCH.value)
                }

            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        userAnimeAdapter.stateRestorationPolicy = ALLOW
        userAnimeRecycler.adapter = userAnimeAdapter

        userAnimeViewModel.userAnimeListPlanToWatch.observe(viewLifecycleOwner){resource ->
            when(resource.status){
                Status.LOADING ->{userAnimeProgressbar.visibility = View.VISIBLE}
                Status.SUCCESS ->{
                    userAnimeProgressbar.visibility = View.GONE
                    resource.data?.data?.let{

                    }
                }
                Status.ERROR ->{Timber.e("Error: %s", resource.message)}
            }
        }

        userAnimeViewModel.getUserAnimeByStatus(UserAnimeStatus.PLAN_TO_WATCH.value)
            .observe(viewLifecycleOwner){
                userAnimeAdapter.submitList(it)
            }

        userAnimeViewModel.userAnimeStatus.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING ->{
                    userAnimeProgressbar.visibility = View.VISIBLE
                }
                Status.SUCCESS ->{
                    userAnimeProgressbar.visibility = View.GONE
                }
                Status.ERROR ->{
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        userAnimeViewModel.userAnimeListPlanToWatchNext.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING ->{
                    userAnimePaginationProgressbar.visibility = View.VISIBLE
                }
                Status.SUCCESS ->{
                    userAnimePaginationProgressbar.visibility = View.GONE
                }
                Status.ERROR ->{
                    Timber.e("Error: %s", resource.message)
                }
            }

    }

    }

    override fun onResume() {
        super.onResume()
        //userAnimeViewModel.getUserAnimeList(UserAnimeStatus.PLAN_TO_WATCH.value)
    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment, bundleOf(Pair(ANIME_ID_ARG, animeMalId))
        )
    }

}