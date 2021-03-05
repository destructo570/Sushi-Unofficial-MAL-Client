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
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.LIST_SPACE_HEIGHT
import com.destructo.sushi.R
import com.destructo.sushi.adapter.UserAnimeListAdapter
import com.destructo.sushi.databinding.FragmentUserAnimeListBinding
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.listener.AddEpisodeListenerUA
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.database.UserAnimeEntity
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.ListItemVerticalDecor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAnimeOnHold : BaseFragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var userAnimeAdapter: UserAnimeListAdapter
    private lateinit var userAnimeRecycler: RecyclerView
    private lateinit var userAnimeProgressbar: ProgressBar
    private lateinit var userAnimePaginationProgressbar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserAnimeListBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        userAnimeRecycler = binding.userAnimeRecycler
        userAnimeRecycler.addItemDecoration(ListItemVerticalDecor(LIST_SPACE_HEIGHT))
        userAnimeRecycler.setHasFixedSize(true)
        userAnimeProgressbar = binding.userAnimeListProgressbar
        userAnimePaginationProgressbar = binding.userAnimeListPaginationProgressbar

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        userAnimeAdapter = UserAnimeListAdapter(AddEpisodeListenerUA { anime ->
            val episodes = anime?.myAnimeListStatus?.numEpisodesWatched
            val animeId = anime?.malId
            if (episodes != null && animeId != null) {
                userAnimeViewModel.addEpisodeAnime(animeId.toString(), episodes + 1, null)
            }
        }, MalIdListener {
            it?.let {
                navigateToAnimeDetails(it)
            }
        }, false)

        userAnimeRecycler.adapter = userAnimeAdapter

        userAnimeViewModel.userAnimeList.observe(viewLifecycleOwner) {
            val onHoldList = mutableListOf<UserAnimeEntity>()
            for (anime in it) {
                if (anime.myAnimeListStatus?.status == UserAnimeStatus.ON_HOLD.value) {
                    onHoldList.add(anime)
                }
            }
            userAnimeAdapter.submitList(onHoldList)
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