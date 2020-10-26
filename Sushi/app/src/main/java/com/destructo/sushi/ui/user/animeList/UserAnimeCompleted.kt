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

class UserAnimeCompleted : Fragment() {

    private lateinit var binding: FragmentUserAnimeListBinding
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var completedAnimeRecycler: RecyclerView
    private lateinit var completedAnimeAdapter: UserAnimeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            userAnimeViewModel.getUserAnimeListCompleted()
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        completedAnimeAdapter = UserAnimeListAdapter()
        userAnimeViewModel.userAnimeListCompleted.observe(viewLifecycleOwner) { userAnime ->
            completedAnimeAdapter.submitList(userAnime.data)
            completedAnimeRecycler.adapter = completedAnimeAdapter
        }
    }

}

