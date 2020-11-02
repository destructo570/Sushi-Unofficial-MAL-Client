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

class UserAnimeCompleted : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var completedAnimeRecycler: RecyclerView
    private lateinit var completedAnimeAdapter: UserAnimeListAdapter
    private lateinit var userAnimeProgressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            userAnimeViewModel.getUserAnimeList(UserAnimeStatus.COMPLETED.value)
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

        completedAnimeRecycler = binding.userAnimeRecycler
        completedAnimeRecycler.setHasFixedSize(true)
        userAnimeProgressbar = binding.userAnimeListProgressbar


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        completedAnimeAdapter = UserAnimeListAdapter(AddEpisodeListener { anime ->
            val episodes = anime?.myListStatus?.numEpisodesWatched
            val animeId = anime?.id
            if (episodes != null && animeId != null){
                userAnimeViewModel.addEpisodeAnime(animeId.toString(),episodes+1)
            }
        })
        userAnimeViewModel.userAnimeListCompleted.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING ->{userAnimeProgressbar.visibility = View.VISIBLE}
                Status.SUCCESS ->{
                    userAnimeProgressbar.visibility = View.GONE
                    resource.data?.let{completedAnimeAdapter.submitList(it.data)
                        completedAnimeRecycler.adapter = completedAnimeAdapter}
                }
                Status.ERROR ->{Timber.e("Error: %s", resource.message)}
            }
        }
    }

}

