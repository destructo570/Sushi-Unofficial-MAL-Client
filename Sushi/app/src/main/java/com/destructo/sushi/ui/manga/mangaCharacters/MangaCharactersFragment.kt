package com.destructo.sushi.ui.manga.mangaCharacters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.adapter.AllMangaCharacterAdapter
import com.destructo.sushi.databinding.FragmentAllMangaCharactersBinding
import com.destructo.sushi.listener.MangaCharacterListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MangaCharactersFragment : Fragment() {

    private lateinit var binding: FragmentAllMangaCharactersBinding
    private val charactersViewModel: MangaCharactersViewModel by viewModels()
    private var mangaIdArg: Int = 0
    private lateinit var characterRecyclerView: RecyclerView
    private lateinit var characterAdapter: AllMangaCharacterAdapter
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
            mangaIdArg = savedInstanceState.getInt("mangaId")
        }else{
            mangaIdArg = MangaCharactersFragmentArgs.fromBundle(requireArguments()).mangaId
            charactersViewModel.getMangaCharacters(mangaIdArg)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("mangaId", mangaIdArg)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllMangaCharactersBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        characterRecyclerView = binding.characterRecycler
        characterRecyclerView.layoutManager = GridLayoutManager(context,3)
        characterRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        toolbar = binding.toolbar

        setupToolbar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        characterAdapter = AllMangaCharacterAdapter(MangaCharacterListener {
            it?.let { it.malId?.let { it1 -> navigateToCharacterDetails(it1) } }
        })
        characterRecyclerView.adapter = characterAdapter
        charactersViewModel.mangaCharacter.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resource.data?.let { characters ->
                        characterAdapter.submitList(characters.characters)

                    }
                }
                Status.ERROR -> {
                }
            }
        }
    }

    private fun navigateToCharacterDetails(character: Int) {
        findNavController().navigate(R.id.characterFragment, bundleOf(Pair("characterId", character)))
    }


    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }
}