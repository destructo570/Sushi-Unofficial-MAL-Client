package com.destructo.sushi.ui.user.animeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.FragmentUserAnimeListBinding
import timber.log.Timber

class UserAnimePlanToWatch : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var ptwAnimeAdapter:UserAnimeListAdapter
    private lateinit var ptwAnimeRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) {
            userAnimeViewModel.getUserAnimeListPtw()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAnimeListBinding
            .inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        ptwAnimeRecycler = binding.userAnimeRecycler
        ptwAnimeRecycler.setHasFixedSize(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ptwAnimeAdapter = UserAnimeListAdapter()
        userAnimeViewModel.userAnimeListPlanToWatch.observe(viewLifecycleOwner){userAnime->
            ptwAnimeAdapter.submitList(userAnime.data)
            ptwAnimeRecycler.adapter = ptwAnimeAdapter
        }



    }

}