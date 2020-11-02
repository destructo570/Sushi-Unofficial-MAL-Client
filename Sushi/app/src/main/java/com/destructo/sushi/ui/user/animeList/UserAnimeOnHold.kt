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

class UserAnimeOnHold : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var onHoldAnimeAdapter: UserAnimeListAdapter
    private lateinit var onHoldAnimeRecycler: RecyclerView
    private lateinit var userAnimeProgressbar: ProgressBar

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

        onHoldAnimeRecycler = binding.userAnimeRecycler
        onHoldAnimeRecycler.setHasFixedSize(true)
        userAnimeProgressbar = binding.userAnimeListProgressbar


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onHoldAnimeAdapter = UserAnimeListAdapter(AddEpisodeListener { anime ->
            val episodes = anime?.myListStatus?.numEpisodesWatched
            val animeId = anime?.id
            if (episodes != null && animeId != null){
                userAnimeViewModel.addEpisodeAnime(animeId.toString(),episodes+1)
            }
        })
        userAnimeViewModel.userAnimeListOnHold.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING ->{userAnimeProgressbar.visibility = View.VISIBLE}
                Status.SUCCESS ->{
                    userAnimeProgressbar.visibility = View.GONE
                    resource.data?.let{onHoldAnimeAdapter.submitList(it.data)
                        onHoldAnimeRecycler.adapter = onHoldAnimeAdapter}
                }
                Status.ERROR ->{Timber.e("Error: %s", resource.message)}
            }
        }
    }

}