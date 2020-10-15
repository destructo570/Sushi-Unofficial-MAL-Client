package com.destructo.sushi.ui.anime.topAnime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentTopAnimeBinding
import com.destructo.sushi.model.jikan.top.TopAnime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopAnimeFragment : Fragment() {

    private val topAnimeViewModel:TopAnimeViewModel by viewModels()
    private lateinit var topAnimeRecycler:RecyclerView
    private lateinit var binding:FragmentTopAnimeBinding
    private lateinit var topAnimeArg: TopAnime
    private lateinit var topAnimeAdapter: TopAnimeAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentTopAnimeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        topAnimeRecycler = binding.topAnimeRecyclerMain
        topAnimeRecycler.layoutManager = GridLayoutManager(context,3)

        topAnimeArg = TopAnimeFragmentArgs.fromBundle(requireArguments()).topAnime

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        topAnimeViewModel.insertTopAnime(topAnimeArg)
        topAnimeAdapter = TopAnimeAdapter()

        topAnimeViewModel.topAnimeList.observe(viewLifecycleOwner){
            it?.let {topAnime->
                topAnimeAdapter.submitList(topAnime.topAnimeEntity)
                topAnimeRecycler.apply{
                    adapter = topAnimeAdapter
                    }
                }
            }
        }


    }
