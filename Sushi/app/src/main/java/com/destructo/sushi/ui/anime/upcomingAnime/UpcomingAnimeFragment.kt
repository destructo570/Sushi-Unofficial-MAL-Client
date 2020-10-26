package com.destructo.sushi.ui.anime.upcomingAnime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.FragmentUpcomingAnimeBinding
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_upcoming_anime.view.*

@AndroidEntryPoint
class UpcomingAnimeFragment : Fragment() {

    private val upcomingAnimeViewModel:UpcomingAnimeViewModel by viewModels()

    private lateinit var binding:FragmentUpcomingAnimeBinding
    private lateinit var upcomingAnimeArg: AnimeRanking
    private lateinit var upcomingAdapter:AnimeRankingAdapter
    private lateinit var upcomingAnimeRecycler:RecyclerView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null){
            upcomingAnimeArg = UpcomingAnimeFragmentArgs
                .fromBundle(requireArguments()).upcomingAnime
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpcomingAnimeBinding
            .inflate(inflater, container, false).apply {
            lifecycleOwner  = viewLifecycleOwner
        }

        upcomingAnimeRecycler = binding.root.upcomingAnimeRecyclerMain
        upcomingAnimeRecycler.layoutManager = GridLayoutManager(context,3)
        upcomingAnimeRecycler.addItemDecoration(GridSpacingItemDeco(3,25,true))
        toolbar = binding.toolbar


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        upcomingAnimeViewModel.insertUpcomingAnime(upcomingAnimeArg)
        upcomingAdapter = AnimeRankingAdapter(AnimeIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })

        upcomingAnimeViewModel.upcomingAnime.observe(viewLifecycleOwner){
            it?.let {upcomingAnime->
                upcomingAdapter.submitList(upcomingAnime.data)
                upcomingAnimeRecycler.apply{
                    setHasFixedSize(true)
                    adapter = upcomingAdapter
                }
            }
        }
        }


    private fun navigateToAnimeDetails(animeMalId: Int){
        this.findNavController().navigate(
            UpcomingAnimeFragmentDirections
                .actionUpcomingAnimeFragmentToAnimeDetailFragment(animeMalId)
        )
    }

    private fun setupToolbar(){
        toolbar.setNavigationOnClickListener {view->
            view.findNavController().navigateUp()
        }
    }

    }
