package com.destructo.sushi.ui.anime.upcomingAnime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentUpcomingAnimeBinding
import com.destructo.sushi.model.top.TopAnime
import com.destructo.sushi.ui.anime.topAnime.TopAnimeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_upcoming_anime.view.*

@AndroidEntryPoint
class UpcomingAnimeFragment : Fragment() {

    private val upcomingAnimeViewModel:UpcomingAnimeViewModel by viewModels()

    private lateinit var binding:FragmentUpcomingAnimeBinding
    private lateinit var upcomingAnimeArg:TopAnime
    private lateinit var upcomingAdapter:TopAnimeAdapter
    private lateinit var upcomingAnimeRecycler:RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpcomingAnimeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner  = viewLifecycleOwner
        }

        upcomingAnimeArg = UpcomingAnimeFragmentArgs.fromBundle(requireArguments()).upcomingAnime
        upcomingAnimeRecycler = binding.root.upcomingAnimeRecyclerMain
        upcomingAnimeRecycler.layoutManager = GridLayoutManager(context,3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        upcomingAnimeViewModel.insertUpcomingAnime(upcomingAnimeArg)
        upcomingAdapter = TopAnimeAdapter()

        upcomingAnimeViewModel.upcomingAnime.observe(viewLifecycleOwner){
            it?.let {upcomingAnime->
                upcomingAdapter.submitList(upcomingAnime.topAnimeEntity)
                upcomingAnimeRecycler.apply{
                    adapter = upcomingAdapter
                }
            }
        }
        }

    }
