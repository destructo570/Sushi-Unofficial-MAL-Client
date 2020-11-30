package com.destructo.sushi.ui.anime.seasonalAnime

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.R
import com.destructo.sushi.R.string.season_sort_type_numListUser
import com.destructo.sushi.R.string.season_sort_type_score
import com.destructo.sushi.adapter.SeasonAnimeAdapter
import com.destructo.sushi.databinding.FragmentSeasonalAnimeBinding
import com.destructo.sushi.enum.mal.SeasonalSortType.NUM_LIST_USER
import com.destructo.sushi.enum.mal.SeasonalSortType.SCORE
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.GridSpacingItemDeco
import com.destructo.sushi.util.toTitleCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_seasonal_anime.view.*
import kotlinx.android.synthetic.main.seasonal_filter_options.*
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

    private lateinit var toolbar: Toolbar
    private lateinit var seasonalAnimeProgress: ProgressBar
    private lateinit var seasonalAnimePaginationProgress: ProgressBar

    private lateinit var seasonArchiveMap: MutableMap<String, List<String?>?>
    private var selectedYear: String = "2021"
    private var selectedSeason: String = "winter"
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
    ): View? {

        binding = FragmentSeasonalAnimeBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        yearSpinner = requireActivity().season_year_spinner
        seasonSpinner = requireActivity().season_spinneer
        sortTypeSpinner = requireActivity().season_sort_spinner
        filterApplyButton = requireActivity().season_apply_filter_button
        seasonCancelButton = requireActivity().season_cancel_filter_button
        drawerLayout = requireActivity().drawer_layout

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
                        seasonArchiveMap = mutableMapOf(
                            listOfArchive?.get(0)?.year.toString() to (listOfArchive?.get(0)?.seasons)
                        )

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
            getString(season_sort_type_score) -> {
                sortType = SCORE.value
            }
            getString(season_sort_type_numListUser) -> {
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
            SeasonalAnimeFragmentDirections.actionSeasonalAnimeToAnimeDetailFragment(animeMalId)
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

    override fun onEndReached(position: Int) {
        seasonAnimeViewModel.getAnimeNextPage()
    }


}
