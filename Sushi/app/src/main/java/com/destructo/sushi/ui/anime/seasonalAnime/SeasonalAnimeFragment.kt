package com.destructo.sushi.ui.anime.seasonalAnime

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentSeasonalAnimeBinding
import com.destructo.sushi.databinding.FragmentUpcomingAnimeBinding
import com.destructo.sushi.model.jikan.season.Season
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.ui.anime.topAnime.TopAnimeAdapter
import com.destructo.sushi.ui.anime.upcomingAnime.UpcomingAnimeFragmentArgs
import com.destructo.sushi.ui.anime.upcomingAnime.UpcomingAnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_seasonal_anime.view.*
import kotlinx.android.synthetic.main.fragment_upcoming_anime.view.*
import timber.log.Timber

@AndroidEntryPoint
class SeasonalAnimeFragment : Fragment() {
    private val seasonAnimeViewModel: SeasonalAnimeViewModel by viewModels()

    private lateinit var binding:FragmentSeasonalAnimeBinding
    private lateinit var seasonAnimeArg: SeasonalAnime
    private lateinit var seasonAdapter: SeasonAnimeAdapter
    private lateinit var seasonAnimeRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seasonAnimeViewModel.getSeasonArchive()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSeasonalAnimeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner  = viewLifecycleOwner
        }

        seasonAnimeArg = SeasonalAnimeFragmentArgs.fromBundle(requireArguments()).seasonalAnime
        seasonAnimeRecycler = binding.root.seasonalAnimeRecyclerMain
        seasonAnimeRecycler.layoutManager = GridLayoutManager(context,3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        seasonAnimeViewModel.insertSeasonAnime(seasonAnimeArg)
        seasonAdapter = SeasonAnimeAdapter()

        seasonAnimeViewModel.seasonalAnime.observe(viewLifecycleOwner){
            it?.let {seasonAnime->
                seasonAdapter.submitList(seasonAnime.data)
                seasonAnimeRecycler.apply{
                    adapter = seasonAdapter
                }
            }
        }

        seasonAnimeViewModel.seasonArchive.observe(viewLifecycleOwner){
            it?.let {seasonArchive->
                Timber.e("Year: ${seasonArchive.archive?.get(45)?.year} Season: ${seasonArchive.archive?.get(45)?.seasons?.get(2)}")

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.seasonal_menu, menu)


    }

    fun openSideSheet(){

    }

    }
