package com.destructo.sushi.ui.anime.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentCharacterAnimeographyBinding
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterAnimeography : Fragment() {

    private lateinit var animeoAdapter: PersonAdapter
    private lateinit var animeoRecyclerView: RecyclerView
    private val characterViewModel: CharacterViewModel
            by viewModels(ownerProducer = {requireParentFragment()})


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            FragmentCharacterAnimeographyBinding
                .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        animeoRecyclerView = binding.animeographyRecyclerMain
        animeoRecyclerView.layoutManager = GridLayoutManager(context,3)
        animeoRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        animeoRecyclerView.setHasFixedSize(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animeoAdapter = PersonAdapter(PersonListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        characterViewModel.character.observe(viewLifecycleOwner){character->
            animeoAdapter.submitList(character.animeography)
            animeoRecyclerView.adapter = animeoAdapter
        }

    }


    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment, bundleOf(Pair(ANIME_ID_ARG, animeMalId))
        )
    }
}