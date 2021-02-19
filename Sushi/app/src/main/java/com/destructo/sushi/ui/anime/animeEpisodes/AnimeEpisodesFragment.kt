package com.destructo.sushi.ui.anime.animeEpisodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.LIST_SPACE_HEIGHT
import com.destructo.sushi.adapter.AnimeEpisodeAdapter
import com.destructo.sushi.databinding.FragmentAllAnimeEpisodesBinding
import com.destructo.sushi.listener.MalUrlListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.ListItemVerticalDecor
import com.destructo.sushi.util.openUrl
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AnimeEpisodesFragment : Fragment() {

    private lateinit var binding: FragmentAllAnimeEpisodesBinding
    private val args: AnimeEpisodesFragmentArgs by navArgs()
    private val animeEpisodeViewModel: AnimeEpisodesViewModel by viewModels()
    private lateinit var animeEpisodeAdapter: AnimeEpisodeAdapter
    private lateinit var episodeRecyclerView: RecyclerView
    private var animeIdArg: Int = 0
    private lateinit var animeEpisodeProgressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
            animeIdArg = savedInstanceState.getInt("animeId")
        }else{
            animeIdArg = AnimeEpisodesFragmentArgs.fromBundle(requireArguments()).animeId
            animeEpisodeViewModel.getAnimeVideos(animeIdArg)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAllAnimeEpisodesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        episodeRecyclerView = binding.episodeRecycler
        episodeRecyclerView.layoutManager = LinearLayoutManager(context)
        episodeRecyclerView.addItemDecoration(ListItemVerticalDecor(LIST_SPACE_HEIGHT))
        animeEpisodeProgressBar = binding.animeEpisodesProgressbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        animeEpisodeAdapter = AnimeEpisodeAdapter(MalUrlListener {
            it?.let {
                context?.openUrl(it)
            }
        })
        episodeRecyclerView.adapter = animeEpisodeAdapter

        animeEpisodeViewModel.animeEpisodeList.observe(viewLifecycleOwner){resource->

            when (resource.status){
                Status.LOADING ->{
                    animeEpisodeProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS ->{
                    animeEpisodeProgressBar.visibility = View.GONE
                    animeEpisodeAdapter.submitList(resource.data?.episodeVideos)
                }
                Status.ERROR ->{
                    Timber.e("${resource.message}")
                }
            }
        }

    }

}