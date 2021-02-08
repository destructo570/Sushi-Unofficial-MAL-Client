package com.destructo.sushi.ui.anime.animeCharacters

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
import com.destructo.sushi.adapter.AllCharactersAdapter
import com.destructo.sushi.databinding.FragmentAllAnimeCharactersBinding
import com.destructo.sushi.listener.AnimeCharacterListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AnimeCharactersFragment : Fragment() {

    private lateinit var binding: FragmentAllAnimeCharactersBinding
    private val animeCharactersViewModel: AnimeCharactersViewModel by viewModels()
    private var animeIdArg: Int = 0
    private lateinit var characterRecyclerView: RecyclerView
    private lateinit var characterAdapter: AllCharactersAdapter
    private lateinit var toolbar:Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
            animeIdArg = savedInstanceState.getInt("animeId")
        }else{
            animeIdArg = AnimeCharactersFragmentArgs.fromBundle(requireArguments()).malId
            animeCharactersViewModel.getAnimeCharacters(animeIdArg)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("animeId", animeIdArg)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllAnimeCharactersBinding.inflate(inflater, container, false).apply {
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

        characterAdapter = AllCharactersAdapter(AnimeCharacterListener {
            it?.let { navigateToCharacterDetails(it)
            }
        })

        binding.characterRecycler.adapter = characterAdapter

        animeCharactersViewModel.animeCharacterAndStaff.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resources.data?.let {
                        characterAdapter.submitList(it.characters)
                    }
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resources.message)
                }
            }

        }

    }

    private fun navigateToCharacterDetails(characterId: Int) {
        findNavController().navigate(R.id.characterFragment, bundleOf(Pair("characterId", characterId)))
    }


    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }
}