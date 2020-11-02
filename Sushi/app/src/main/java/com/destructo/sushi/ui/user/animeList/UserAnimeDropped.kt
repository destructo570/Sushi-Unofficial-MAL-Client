package com.destructo.sushi.ui.user.animeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.FragmentUserAnimeListBinding
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.network.Status
import timber.log.Timber

class UserAnimeDropped : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var droppedAnimeAdapter: UserAnimeListAdapter
    private lateinit var droppedAnimeRecycler: RecyclerView
    private lateinit var userAnimeProgressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            userAnimeViewModel.getUserAnimeList(UserAnimeStatus.DROPPED.value)
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

        droppedAnimeRecycler = binding.userAnimeRecycler
        droppedAnimeRecycler.setHasFixedSize(true)
        userAnimeProgressbar = binding.userAnimeListProgressbar


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        droppedAnimeAdapter = UserAnimeListAdapter(AddEpisodeListener { anime ->
            val episodes = anime?.myListStatus?.numEpisodesWatched
            val animeId = anime?.id
            if (episodes != null && animeId != null){
                userAnimeViewModel.addEpisodeAnime(animeId.toString(),episodes+1)
            }
        })
        userAnimeViewModel.userAnimeListDropped.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING ->{userAnimeProgressbar.visibility = View.VISIBLE}
                Status.SUCCESS ->{
                    userAnimeProgressbar.visibility = View.GONE
                    resource.data?.let{droppedAnimeAdapter.submitList(it.data)
                        droppedAnimeRecycler.adapter = droppedAnimeAdapter}
                }
                Status.ERROR ->{Timber.e("Error: %s", resource.message)}
            }
        }
    }

}