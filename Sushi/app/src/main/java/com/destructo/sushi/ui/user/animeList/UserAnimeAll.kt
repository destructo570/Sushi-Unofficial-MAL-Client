package com.destructo.sushi.ui.user.animeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.ALLOW
import com.destructo.sushi.databinding.FragmentUserAnimeListBinding
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.ListEndListener
import com.destructo.sushi.ui.anime.AnimeFragmentDirections
import com.destructo.sushi.ui.anime.animeDetails.AnimeDetailViewModel
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserAnimeAll : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private val animeDetailViewModel:AnimeDetailViewModel
            by viewModels()
    private lateinit var userAnimeAdapter: UserAnimeListAdapter
    private lateinit var userAnimeRecycler: RecyclerView
    private lateinit var userAnimeProgressbar: ProgressBar
    private lateinit var userAnimeAllList: MutableList<UserAnimeData?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            userAnimeViewModel.getUserAnimeList(null)
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
        userAnimeAdapter.setListEndListener(object : ListEndListener{
            override fun onEndReached(position: Int) {
                userAnimeViewModel.getNextPage(null)
            }

        })

        userAnimeRecycler.adapter = userAnimeAdapter

        userAnimeViewModel.userAnimeListAll.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING ->{
                    userAnimeProgressbar.visibility = View.VISIBLE
                }
                Status.SUCCESS ->{
                    userAnimeProgressbar.visibility = View.GONE
                    resource.data?.data?.let{
                        //userAnimeAllList = it.toMutableList()
                        //userAnimeViewModel.allNewList.value = it.toMutableList()
                    }
                }
                Status.ERROR ->{
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        userAnimeViewModel.allNewList.observe(viewLifecycleOwner){
            //userAnimeAdapter.submitList(it)
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

        userAnimeViewModel.userAnimeListAllNext.observe(viewLifecycleOwner){resource->
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

        userAnimeViewModel.userAnimeList.observe(viewLifecycleOwner){
            userAnimeAdapter.submitList(it)
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