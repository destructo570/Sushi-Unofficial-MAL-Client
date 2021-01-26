package com.destructo.sushi.ui.anime.animeStaff

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
import com.destructo.sushi.adapter.AllAnimeStaffAdapter
import com.destructo.sushi.databinding.FragmentAllAnimeStaffBinding
import com.destructo.sushi.listener.AnimeStaffListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.animeCharacters.AnimeCharactersFragmentArgs
import com.destructo.sushi.ui.anime.animeCharacters.AnimeCharactersViewModel
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AnimeStaffFragment : Fragment() {

    private lateinit var binding: FragmentAllAnimeStaffBinding
    private val animeCharactersViewModel: AnimeCharactersViewModel by viewModels()
    private var animeIdArg: Int = 0
    private lateinit var toolbar: Toolbar
    private lateinit var staffRecycler: RecyclerView
    private lateinit var staffAdapter: AllAnimeStaffAdapter

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
        binding = FragmentAllAnimeStaffBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        staffRecycler = binding.staffRecycler
        staffRecycler.layoutManager = GridLayoutManager(context,3)
        staffRecycler.addItemDecoration(GridSpacingItemDeco(3,25,true))
        toolbar = binding.toolbar

        setupToolbar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        staffAdapter = AllAnimeStaffAdapter(AnimeStaffListener {
            it?.let { it.malId?.let { it1 -> navigateToPersonDetails(it1) } }
        })
        staffRecycler.adapter = staffAdapter

        animeCharactersViewModel.animeCharacterAndStaff.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resources.data?.let {
                        staffAdapter.submitList(it.staff)
                    }
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resources.message)
                }
            }

        }

    }


    private fun navigateToPersonDetails(malId: Int) {
        findNavController().navigate(R.id.personFragment, bundleOf(Pair("personId", malId)))
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }

}