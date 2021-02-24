package com.destructo.sushi.ui.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.*
import com.destructo.sushi.adapter.MalSubEntityAdapter
import com.destructo.sushi.databinding.FragmentFavoritesBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.jikan.user.Favorites
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment() {

    private val profileViewModel:ProfileViewModel by viewModels()
    private val args: FavoritesFragmentArgs by navArgs()
    private lateinit var favRecyclerView: RecyclerView
    private lateinit var favAdapter: MalSubEntityAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoritesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        toolbar = binding.toolbar
        favRecyclerView = binding.favRecycler
        favRecyclerView.layoutManager = GridLayoutManager(context, 3)
        favRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        favAdapter = MalSubEntityAdapter(MalIdListener {
            openFragment(args.category, it)
        })
        favRecyclerView.adapter = favAdapter
        submitListToAdapter(args.favorites)


    }

    private fun setupToolbar() {
        toolbar.title = args.category
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun openFragment(category: String, malId: Int?){
        when(category){
            getString(R.string.anime) ->{
                malId?.let { navigateToAnimeDetails(it) }
            }
            getString(R.string.manga) ->{
                malId?.let { navigateToMangaDetails(it) }
            }
            getString(R.string.character) ->{
                malId?.let { navigateToCharacterDetails(it) }
            }
            getString(R.string.people) ->{
                malId?.let { navigateToPersonDetails(it) }
            }

        }
    }

    private fun submitListToAdapter(favorites: Favorites){
        when(args.category){
            getString(R.string.anime) ->{
                favAdapter.submitList(favorites.anime)
            }
            getString(R.string.manga) ->{
                favAdapter.submitList(favorites.manga)
            }
            getString(R.string.character) ->{
                favAdapter.submitList(favorites.characters)
            }
            getString(R.string.people) ->{
                favAdapter.submitList(favorites.people)
            }

        }
    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment,
            bundleOf(Pair(ANIME_ID_ARG, animeMalId)),
            getAnimNavOptions()
        )
    }

    private fun navigateToCharacterDetails(character: Int) {
        this.findNavController().navigate(
            R.id.characterFragment,
            bundleOf(Pair(CHARACTER_ID_ARG, character)),
            getAnimNavOptions()
        )
    }

    private fun navigateToPersonDetails(personId: Int) {
        this.findNavController().navigate(
            R.id.personFragment,
            bundleOf(Pair(PERSON_ID_ARG, personId)),
            getAnimNavOptions()
        )
    }

    private fun navigateToMangaDetails(mangaMalId: Int){
        this.findNavController().navigate(
            R.id.mangaDetailsFragment,
            bundleOf(Pair(MANGA_ID_ARG, mangaMalId)),
            getAnimNavOptions()
        )
    }
}