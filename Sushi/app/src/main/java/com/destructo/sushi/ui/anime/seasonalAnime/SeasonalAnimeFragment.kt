package com.destructo.sushi.ui.anime.seasonalAnime

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.R.string.season_sort_type_numListUser
import com.destructo.sushi.R.string.season_sort_type_score
import com.destructo.sushi.databinding.FragmentSeasonalAnimeBinding
import com.destructo.sushi.enum.mal.SeasonalSortType.*
import com.destructo.sushi.model.jikan.anime.core.Anime
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.ui.anime.AnimeFragmentDirections
import com.destructo.sushi.ui.anime.animeDetails.AnimeDetailListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_seasonal_anime.view.*
import kotlinx.android.synthetic.main.seasonal_filter_options.*
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SeasonalAnimeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val seasonAnimeViewModel: SeasonalAnimeViewModel by viewModels()

    private lateinit var binding:FragmentSeasonalAnimeBinding
    private lateinit var seasonAnimeArg: SeasonalAnime
    private lateinit var seasonAdapter: SeasonAnimeAdapter
    private lateinit var seasonAnimeRecycler: RecyclerView
    private lateinit var yearSpinner:Spinner
    private lateinit var seasonSpinner:Spinner
    private lateinit var sortTypeSpinner:Spinner
    private lateinit var filterApplyButton:Button
    private lateinit var seasonCancelButton: Button
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var seasonArchiveMap:MutableMap<String,List<String?>?>
    private var selectedYear:String="2021"
    private var selectedSeason:String="Winter"
    private var selectedSortType:String="anime_score"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seasonAnimeViewModel.getSeasonArchive()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSeasonalAnimeBinding
            .inflate(inflater, container, false).apply {
            lifecycleOwner  = viewLifecycleOwner
        }
        yearSpinner = requireActivity().season_year_spinner
        seasonSpinner = requireActivity().season_spinneer
        sortTypeSpinner = requireActivity().season_sort_spinner
        filterApplyButton = requireActivity().season_apply_filter_button
        seasonCancelButton = requireActivity().season_cancel_filter_button
        drawerLayout = requireActivity().drawer_layout

        yearSpinner.onItemSelectedListener = this
        seasonSpinner.onItemSelectedListener = this

        seasonAnimeArg = SeasonalAnimeFragmentArgs.fromBundle(requireArguments()).seasonalAnime
        seasonAnimeRecycler = binding.root.seasonalAnimeRecyclerMain
        seasonAnimeRecycler.layoutManager = GridLayoutManager(context, 3)

        context?.let { ArrayAdapter.createFromResource(
            it,R.array.season_sort_type,android.R.layout.simple_spinner_item).also {adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sortTypeSpinner.adapter = adapter
            sortTypeSpinner.onItemSelectedListener = this
        } }

        filterApplyButton.setOnClickListener {
            seasonAnimeViewModel.getSeasonalAnime(selectedYear,selectedSeason
                .toLowerCase(Locale.getDefault()),selectedSortType,"100",null)
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        seasonCancelButton.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.filter_menu_id->{ drawerLayout.openDrawer(GravityCompat.END) }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        seasonAnimeViewModel.insertSeasonAnime(seasonAnimeArg)
        seasonAdapter = SeasonAnimeAdapter(AnimeDetailListener {
            it?.let {  navigateToAnimeDetails(it) }
        })

        seasonAnimeViewModel.seasonalAnime.observe(viewLifecycleOwner){
            it?.let { seasonAnime->
                seasonAdapter.submitList(seasonAnime.data)
                seasonAnimeRecycler.apply{
                    setHasFixedSize(true)
                    adapter = seasonAdapter
                }
            }
        }

        seasonAnimeViewModel.seasonArchive.observe(viewLifecycleOwner){
            it.archive.let { listOfArchive->
                seasonArchiveMap = mutableMapOf(
                    listOfArchive?.get(0)?.year.toString() to (listOfArchive?.get(0)?.seasons))

                listOfArchive?.forEach { archive ->
                    seasonArchiveMap[archive?.year.toString()] = archive?.seasons
                }

                val yearList: ArrayList<String> = ArrayList()
                yearList.addAll(seasonArchiveMap.keys)

                val yearArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    requireContext(), android.R.layout.simple_spinner_item,
                    yearList)

                yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                yearSpinner.adapter = yearArrayAdapter
            }

            }
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.seasonal_menu, menu)

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when(parent?.id){
                R.id.season_year_spinner->{
                 selectedYear = parent.getItemAtPosition(position).toString()
                loadSeasons(selectedYear) }

                R.id.season_spinneer->{
                    selectedSeason = parent.getItemAtPosition(position).toString() }

                R.id.season_sort_spinner->{
                    selectedSortType = getSortType(parent.getItemAtPosition(position).toString()) }

        }
    }

    private fun getSortType(selectedItem: String): String {
        var sortType = ""
        when(selectedItem){
            getString(season_sort_type_score) -> { sortType = SCORE.value}
            getString(season_sort_type_numListUser) -> { sortType = NUM_LIST_USER.value}
        }
        return sortType
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadSeasons(year: String) {
        val seasonList:MutableList<String> = seasonArchiveMap[year] as MutableList<String>
        val seasonArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item,
            seasonList
        )

        seasonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        seasonSpinner.adapter = seasonArrayAdapter
    }

    private fun navigateToAnimeDetails(animeMalId: Int){
        this.findNavController().navigate(
            SeasonalAnimeFragmentDirections.actionSeasonalAnimeToAnimeDetailFragment(animeMalId)
        )
    }


}
