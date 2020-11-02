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
import com.destructo.sushi.network.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserAnimeAll : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var userAnimeAdapter: UserAnimeListAdapter
    private lateinit var userAnimeRecycler: RecyclerView
    private lateinit var userAnimeProgressbar: ProgressBar


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
        userAnimeProgressbar = binding.userAnimeListProgressbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userAnimeAdapter = UserAnimeListAdapter(AddEpisodeListener { anime ->
            val episodes = anime?.myListStatus?.numEpisodesWatched
            val animeId = anime?.id
            if (episodes != null && animeId != null){
                userAnimeViewModel.addEpisodeAnime(animeId.toString(),episodes+1)
            }
        })
        userAnimeViewModel.userAnimeListAll.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING ->{userAnimeProgressbar.visibility = View.VISIBLE}
                Status.SUCCESS ->{
                    userAnimeProgressbar.visibility = View.GONE
                    resource.data?.let{userAnimeAdapter.submitList(it.data)
                        userAnimeRecycler.adapter = userAnimeAdapter}
                }
                Status.ERROR ->{Timber.e("Error: %s", resource.message)}
            }
        }

        userAnimeViewModel.userAnimeStatus.observe(viewLifecycleOwner){animeStatus->
            userAnimeViewModel.getUserAnimeList(null)
        }
    }
}