package com.destructo.sushi.ui.anime.animeEpisodes

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.adapter.AnimeEpisodeAdapter
import com.destructo.sushi.databinding.FragmentAnimeEpisodesBinding
import com.destructo.sushi.listener.MalUrlListener
import com.destructo.sushi.network.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AnimeEpisodesFragment : Fragment() {

    private lateinit var binding: FragmentAnimeEpisodesBinding
    private val args: AnimeEpisodesFragmentArgs by navArgs()
    private val animeEpisodeViewModel: AnimeEpisodesViewModel by viewModels()
    private lateinit var animeEpisodeAdapter: AnimeEpisodeAdapter
    private lateinit var episodeRecyclerView: RecyclerView
    private var animeIdArg: Int = 0


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

        binding = FragmentAnimeEpisodesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        episodeRecyclerView = binding.episodeRecycler
        episodeRecyclerView.layoutManager = LinearLayoutManager(context)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        animeEpisodeAdapter = AnimeEpisodeAdapter(MalUrlListener {
            it?.let {
                openUrl(it)
            }
        })
        episodeRecyclerView.adapter = animeEpisodeAdapter

        animeEpisodeViewModel.animeEpisodeList.observe(viewLifecycleOwner){resource->

            when (resource.status){
                Status.LOADING ->{}
                Status.SUCCESS ->{
                    animeEpisodeAdapter.submitList(resource.data?.episodeVideos)
                }
                Status.ERROR ->{
                    Timber.e("${resource.message}")
                }
            }
        }

    }

    private fun openUrl(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabIntent = builder.build()
        customTabIntent.launchUrl(requireContext(), Uri.parse(url))
    }
}