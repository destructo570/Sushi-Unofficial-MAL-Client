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
import com.destructo.sushi.MANGA_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentCharacterMangaographyBinding
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterMangaography : Fragment() {

    private lateinit var mangaoAdapter: PersonAdapter
    private lateinit var mangaoRecyclerView: RecyclerView
    private val characterViewModel: CharacterViewModel
            by viewModels(ownerProducer = {requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            FragmentCharacterMangaographyBinding
                .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        mangaoRecyclerView = binding.mangaographyRecyclerMain
        mangaoRecyclerView.layoutManager = GridLayoutManager(context,3)
        mangaoRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        mangaoRecyclerView.setHasFixedSize(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mangaoAdapter = PersonAdapter(PersonListener {
            it?.let { navigateToMangaDetails(it) }
        })

        characterViewModel.character.observe(viewLifecycleOwner){character->
            mangaoAdapter.submitList(character.mangaography)
            mangaoRecyclerView.adapter = mangaoAdapter
        }

    }

    private fun navigateToMangaDetails(mangaMalId: Int){
        this.findNavController().navigate(
            R.id.mangaDetailsFragment, bundleOf(Pair(MANGA_ID_ARG, mangaMalId))
        )
    }
}