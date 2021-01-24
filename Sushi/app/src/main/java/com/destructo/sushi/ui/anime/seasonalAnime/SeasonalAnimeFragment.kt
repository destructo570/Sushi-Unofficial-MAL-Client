package com.destructo.sushi.ui.anime.seasonalAnime

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.R
import com.destructo.sushi.R.string.number_of_members
import com.destructo.sushi.R.string.score
import com.destructo.sushi.adapter.SeasonAnimeAdapter
import com.destructo.sushi.databinding.FragmentSeasonalAnimeBinding
import com.destructo.sushi.enum.mal.SeasonalSortType.NUM_LIST_USER
import com.destructo.sushi.enum.mal.SeasonalSortType.SCORE
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.GridSpacingItemDeco
import com.destructo.sushi.util.toTitleCase
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_seasonal_anime.view.*
import kotlinx.android.synthetic.main.seasonal_filter_options.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SeasonalAnimeFragment : Fragment(), AdapterView.OnItemSelectedListener, ListEndListener {

    private val seasonAnimeViewModel: SeasonalAnimeViewModel by viewModels()

    private lateinit var binding: FragmentSeasonalAnimeBinding
    private lateinit var seasonAdapter: SeasonAnimeAdapter
    private lateinit var seasonAnimeRecycler: RecyclerView
    private lateinit var yearSpinner: Spinner
    private lateinit var seasonSpinner: Spinner
    private lateinit var sortTypeSpinner: Spinner
    private lateinit var filterApplyButton: Button
    private lateinit var seasonCancelButton: Button
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView3: NavigationView
    private lateinit var seasonFilterHeader: ConstraintLayout


    private lateinit var toolbar: Toolbar
    private lateinit var seasonalAnimeProgress: ProgressBar
    private lateinit var seasonalAnimePaginationProgress: ProgressBar

    private lateinit var seasonArchiveMap: MutableMap<String, List<String?>?>
    private var selectedYear: String = "2020"
    private var selectedSeason: String = "fall"
    private var selectedSortType: String = "anime_score"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            seasonAnimeViewModel.clearList()
            seasonAnimeViewModel.getSeasonalAnime("2020", "fall", "anime_score", DEFAULT_PAGE_LIMIT, null)
            seasonAnimeViewModel.getSeasonArchive()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSeasonalAnimeBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        drawerLayout = binding.drawerLayout
        navView3 = binding.seasonalNavView
        seasonFilterHeader = navView3.getHeaderView(0) as ConstraintLayout

        yearSpinner = seasonFilterHeader.season_year_spinner
        seasonSpinner = seasonFilterHeader.season_spinneer
        sortTypeSpinner = seasonFilterHeader.season_sort_spinner
        filterApplyButton = seasonFilterHeader.season_apply_filter_button
        seasonCancelButton = seasonFilterHeader.season_cancel_filter_button

        toolbar = binding.toolbar
        seasonalAnimeProgress = binding.seasonalAnimeProgressbar
        seasonalAnimePaginationProgress = binding.seasonalAnimePaginationProgressbar

        yearSpinner.onItemSelectedListener = this
        seasonSpinner.onItemSelectedListener = this

        seasonAnimeRecycler = binding.root.seasonalAnimeRecyclerMain

        seasonAnimeRecycler.apply {
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(GridSpacingItemDeco(3, 25, true))
        }

        context?.let {
            ArrayAdapter.createFromResource(
                it, R.array.season_sort_type, android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sortTypeSpinner.adapter = adapter
                sortTypeSpinner.onItemSelectedListener = this
            }
        }

        filterApplyButton.setOnClickListener {
            toolbar.title = "${selectedSeason.toTitleCase()}, $selectedYear"
            seasonAnimeViewModel.clearList()

            seasonAnimeViewModel.getSeasonalAnime(
                selectedYear,
                selectedSeason.toLowerCase(Locale.getDefault()),
                selectedSortType,
                DEFAULT_PAGE_LIMIT, null
            )

            drawerLayout.closeDrawer(GravityCompat.END)
        }

        seasonCancelButton.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        setOnBackPressed()
        setDrawerListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        seasonAdapter = SeasonAnimeAdapter(MalIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        seasonAdapter.setListEndListener(this)
        seasonAnimeRecycler.adapter = seasonAdapter

        seasonAnimeViewModel.seasonalAnime.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    seasonalAnimeProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    seasonalAnimeProgress.visibility = View.GONE
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        seasonAnimeViewModel.seasonArchive.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resource.data?.archive.let { listOfArchive ->
                        seasonArchiveMap = mutableMapOf()

                        listOfArchive?.forEach { archive ->
                            seasonArchiveMap[archive?.year.toString()] = archive?.seasons
                        }

                        val yearList: ArrayList<String> = ArrayList()
                        yearList.addAll(seasonArchiveMap.keys)

                        val yearArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                            requireContext(), android.R.layout.simple_spinner_item,
                            yearList
                        )

                        yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        yearSpinner.adapter = yearArrayAdapter
                    }

                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        seasonAnimeViewModel.seasonalAnimeList.observe(viewLifecycleOwner){
            seasonAdapter.submitList(it)
        }

        seasonAnimeViewModel.nextPage.observe(viewLifecycleOwner){ resource ->

            when (resource.status) {
                Status.LOADING -> {
                    seasonalAnimePaginationProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    seasonalAnimePaginationProgress.visibility = View.GONE

                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
        }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.seasonal_menu, menu)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("year", selectedYear)
        outState.putString("season", selectedSeason)
        outState.putString("sort_type", selectedSortType)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent?.id) {
            R.id.season_year_spinner -> {
                selectedYear = parent.getItemAtPosition(position).toString()
                loadSeasons(selectedYear)
            }

            R.id.season_spinneer -> {
                selectedSeason = parent.getItemAtPosition(position).toString()
            }

            R.id.season_sort_spinner -> {
                selectedSortType = getSortType(parent.getItemAtPosition(position).toString())
            }

        }
    }



    private fun getSortType(selectedItem: String): String {
        var sortType = ""
        when (selectedItem) {
            getString(score) -> {
                sortType = SCORE.value
            }
            getString(number_of_members) -> {
                sortType = NUM_LIST_USER.value
            }
        }
        return sortType
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadSeasons(year: String) {
        val seasonList: MutableList<String> = seasonArchiveMap[year] as MutableList<String>
        val seasonArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item,
            seasonList
        )

        seasonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        seasonSpinner.adapter = seasonArrayAdapter
    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment, bundleOf(Pair(ANIME_ID_ARG, animeMalId))
        )
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        toolbar.title = "${selectedSeason.toTitleCase()}, $selectedYear"
        toolbar.inflateMenu(R.menu.seasonal_menu)
        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.filter_menu_id -> {
                    drawerLayout.openDrawer(GravityCompat.END)

                }
            }
            false

        }
    }

    private fun setOnBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                drawerLayout.closeDrawer(GravityCompat.END)
            }else{
                findNavController().navigateUp()
            }
        }
    }

    private fun setDrawerListener(){
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {
                requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            override fun onDrawerClosed(drawerView: View) {
                requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })
    }

    override fun onEndReached(position: Int) {
        seasonAnimeViewModel.getAnimeNextPage()
    }


}
