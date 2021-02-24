package com.destructo.sushi.ui.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.*
import com.destructo.sushi.adapter.MalSubEntityPrevAdapter
import com.destructo.sushi.databinding.FragmentProfileFavoriteBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.jikan.user.Favorites
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.inc_user_fav_anime.view.*
import kotlinx.android.synthetic.main.inc_user_fav_character.view.*
import kotlinx.android.synthetic.main.inc_user_fav_manga.view.*
import kotlinx.android.synthetic.main.inc_user_fav_people.view.*
import timber.log.Timber

@AndroidEntryPoint
class ProfileFavoriteFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileFavoriteBinding
    private lateinit var favAnimeRecyclerView: RecyclerView
    private lateinit var favMangaRecyclerView: RecyclerView
    private lateinit var favCharacterRecyclerView: RecyclerView
    private lateinit var favPeopleRecyclerView: RecyclerView

    private lateinit var favAnimeLayout: LinearLayout
    private lateinit var favMangaLayout: LinearLayout
    private lateinit var favCharacterLayout: LinearLayout
    private lateinit var favPeopleLayout: LinearLayout

    private lateinit var favAnimeSeeMore: TextView
    private lateinit var favMangaSeeMore: TextView
    private lateinit var favCharacterSeeMore: TextView
    private lateinit var favPeopleSeeMore: TextView

    private lateinit var favAnimeAdapter: MalSubEntityPrevAdapter
    private lateinit var favMangaAdapter: MalSubEntityPrevAdapter
    private lateinit var favCharacterAdapter: MalSubEntityPrevAdapter
    private lateinit var favPeopleAdapter: MalSubEntityPrevAdapter



    private val profileViewModel:ProfileViewModel by viewModels(
        ownerProducer = {requireParentFragment()})


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileFavoriteBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        favAnimeRecyclerView = binding.root.favAnimeRecycler
        favCharacterRecyclerView = binding.root.favCharacterRecycler
        favMangaRecyclerView = binding.root.favMangaRecycler
        favPeopleRecyclerView = binding.root.favPeopleRecycler

        favAnimeSeeMore = binding.root.userFavAnimeMore
        favMangaSeeMore = binding.root.userFavMangaMore
        favCharacterSeeMore = binding.root.userFavCharacterMore
        favPeopleSeeMore = binding.root.userFavPeopleMore

        favAnimeLayout = binding.favAnimeLayout
        favMangaLayout = binding.favMangaLayout
        favCharacterLayout = binding.favCharacterLayout
        favPeopleLayout = binding.favPeopleLayout

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        favAnimeAdapter = MalSubEntityPrevAdapter(MalIdListener {
            it?.let{ navigateToAnimeDetails(it) }
        })
        favMangaAdapter = MalSubEntityPrevAdapter(MalIdListener {
            it?.let{ navigateToMangaDetails(it) }
        })
        favCharacterAdapter = MalSubEntityPrevAdapter(MalIdListener {
            it?.let{ navigateToCharacterDetails(it) }
        })
        favPeopleAdapter = MalSubEntityPrevAdapter(MalIdListener {
            it?.let{ navigateToPersonDetails(it) }
        })

        favAnimeRecyclerView.adapter = favAnimeAdapter
        favCharacterRecyclerView.adapter = favCharacterAdapter
        favMangaRecyclerView.adapter = favMangaAdapter
        favPeopleRecyclerView.adapter = favPeopleAdapter


        profileViewModel.userInformation.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING->{
                }
                Status.SUCCESS->{
                    resource.data?.favorites?.let {favorites->

                        if (!favorites.anime.isNullOrEmpty()){
                            favAnimeAdapter.submitList(favorites.anime)
                            setAnimeMoreListener(favorites)
                        }else favAnimeLayout.visibility = View.GONE

                        if (!favorites.manga.isNullOrEmpty()){
                            favMangaAdapter.submitList(favorites.manga)
                            setMangaMoreListener(favorites)

                        }else favMangaLayout.visibility = View.GONE

                        if (!favorites.characters.isNullOrEmpty()){
                            favCharacterAdapter.submitList(favorites.characters)
                            setCharacterMoreListener(favorites)

                        }else favCharacterLayout.visibility = View.GONE

                        if (!favorites.people.isNullOrEmpty()){
                            favPeopleAdapter.submitList(favorites.people)
                            setPeopleeMoreListener(favorites)

                        }else favPeopleLayout.visibility = View.GONE

                    }
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(R.id.animeDetailFragment,
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

    private fun navigateToFavorites(catergory: String, fav: Favorites){
        this.findNavController().navigate(
            R.id.favoritesFragment,
            bundleOf(Pair(CATEGORY_ARG, catergory), Pair(FAV_ARG, fav)),
            getAnimNavOptions()
        )
    }

    private fun setAnimeMoreListener(fav: Favorites){
        favAnimeSeeMore.setOnClickListener {
            navigateToFavorites(getString(R.string.anime), fav)
        }
    }
    private fun setCharacterMoreListener(fav: Favorites){
        favCharacterSeeMore.setOnClickListener {
            navigateToFavorites(getString(R.string.character), fav)
        }
    }
    private fun setMangaMoreListener(fav: Favorites){
        favMangaSeeMore.setOnClickListener {
            navigateToFavorites(getString(R.string.manga), fav)
        }
    }
    private fun setPeopleeMoreListener(fav: Favorites){
        favPeopleSeeMore.setOnClickListener {
            navigateToFavorites(getString(R.string.people), fav)
        }
    }

}