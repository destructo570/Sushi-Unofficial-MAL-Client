package com.destructo.sushi.ui.user.animeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.*
import com.destructo.sushi.databinding.FragmentUserAnimeListBinding
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import timber.log.Timber

class UserAnimePlanToWatch : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var userAnimeAdapter:UserAnimeListAdapter
    private lateinit var userAnimeRecycler: RecyclerView
    private lateinit var userAnimeProgressbar: ProgressBar

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
        binding = FragmentUserAnimeListBinding
            .inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        userAnimeRecycler = binding.userAnimeRecycler
        userAnimeRecycler.setHasFixedSize(true)
        userAnimeRecycler.itemAnimator = null
        userAnimeProgressbar = binding.userAnimeListProgressbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userAnimeAdapter = UserAnimeListAdapter(AddEpisodeListener { anime ->
            val episodes = anime?.myAnimeListStatus?.numEpisodesWatched
            val animeId = anime?.id
            if (episodes != null && animeId != null){
                userAnimeViewModel.addEpisodeAnime(animeId.toString(),episodes+1)
            }
        },
            AnimeIdListener {
                it?.let{
                    navigateToAnimeDetails(it)
                }
            })

        userAnimeAdapter.stateRestorationPolicy = ALLOW
        userAnimeRecycler.adapter = userAnimeAdapter

        userAnimeViewModel.userAnimeListPlanToWatch.observe(viewLifecycleOwner){resource ->
            when(resource.status){
                Status.LOADING ->{userAnimeProgressbar.visibility = View.VISIBLE}
                Status.SUCCESS ->{
                    userAnimeProgressbar.visibility = View.GONE
                    resource.data?.let{
                        userAnimeAdapter.submitList(it.data)
                    }
                }
                Status.ERROR ->{Timber.e("Error: %s", resource.message)}
            }
        }

        userAnimeViewModel.userAnimeStatus.observe(viewLifecycleOwner){animeStatus->
            userAnimeViewModel.getUserAnimeList(UserAnimeStatus.PLAN_TO_WATCH.value)
        }

    }

    override fun onResume() {
        super.onResume()
        userAnimeViewModel.getUserAnimeList(null)
    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            MyAnimeListFragmentDirections.actionMyAnimeListFragmentToAnimeDetailFragment(animeMalId)
        )
    }

}