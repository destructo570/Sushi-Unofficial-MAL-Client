package com.destructo.sushi.ui.user.animeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.ALLOW
import com.destructo.sushi.adapter.UserAnimeListAdapter
import com.destructo.sushi.databinding.FragmentUserAnimeListBinding
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.listener.AddEpisodeListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import timber.log.Timber

class UserAnimeOnHold : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var userAnimeAdapter: UserAnimeListAdapter
    private lateinit var userAnimeRecycler: RecyclerView
    private lateinit var userAnimeProgressbar: ProgressBar
    private lateinit var userAnimePaginationProgressbar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            userAnimeViewModel.getUserAnimeList(UserAnimeStatus.ON_HOLD.value)
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


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        userAnimeAdapter = UserAnimeListAdapter(AddEpisodeListener { anime ->
            val episodes = anime?.myAnimeListStatus?.numEpisodesWatched
            val animeId = anime?.id
            if (episodes != null && animeId != null){
                userAnimeViewModel.addEpisodeAnime(animeId.toString(),episodes+1)
            }
        }, MalIdListener {
            it?.let{
                navigateToAnimeDetails(it)
            }
        }, false)
        userAnimeAdapter.setListEndListener(object : ListEndListener {
            override fun onEndReached(position: Int) {
                userAnimeViewModel.getNextPage(UserAnimeStatus.ON_HOLD.value)
            }

        })

        userAnimeAdapter.stateRestorationPolicy = ALLOW
        userAnimeRecycler.adapter = userAnimeAdapter

        userAnimeViewModel.userAnimeListOnHold.observe(viewLifecycleOwner) { resource ->
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

        userAnimeViewModel.getUserAnimeByStatus(UserAnimeStatus.ON_HOLD.value)
            .observe(viewLifecycleOwner){
            userAnimeAdapter.submitList(it)
        }

            userAnimeViewModel.userAnimeListOnHoldNext.observe(viewLifecycleOwner){resource->
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
        //userAnimeViewModel.getUserAnimeList(UserAnimeStatus.ON_HOLD.value)
    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            MyAnimeListFragmentDirections.actionMyAnimeListFragmentToAnimeDetailFragment(animeMalId)
        )
    }

}