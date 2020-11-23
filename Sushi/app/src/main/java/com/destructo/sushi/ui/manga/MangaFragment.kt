package com.destructo.sushi.ui.manga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentMangaBinding
import com.destructo.sushi.enum.mal.MangaRankingType.*
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.listener.ListEndListener
import com.destructo.sushi.ui.manga.mangaDetails.MangaDetailListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

@AndroidEntryPoint
class MangaFragment : Fragment(), AdapterView.OnItemSelectedListener, ListEndListener {

    private val mangaViewModel:MangaViewModel by viewModels()

    private lateinit var binding:FragmentMangaBinding
    private lateinit var mangaRecycler:RecyclerView
    private lateinit var mangaAdapter:MangaAdapter
    private lateinit var mangaTypeSpinner:Spinner
    private lateinit var toolbar: Toolbar
    private lateinit var mangaProgressBar: ProgressBar
    private lateinit var topMangaPaginationProgress: LinearLayout
    private var currentRankingType:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            val savedString = savedInstanceState.getString("ranking_type")
            savedString?.let {
                currentRankingType = it
            }
        }else{
            mangaViewModel.getMangaRankingList(null, DEFAULT_PAGE_LIMIT)

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("ranking_type", currentRankingType)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMangaBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        toolbar = binding.toolbar
        mangaProgressBar = binding.mangaProgressbar
        topMangaPaginationProgress = binding.topMangaPaginationProgress

        mangaTypeSpinner = binding.mangaRankingSpinner
        context?.let { ArrayAdapter.createFromResource(
            it,R.array.manga_ranking_type,android.R.layout.simple_spinner_item).also {adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mangaTypeSpinner.adapter = adapter
            mangaTypeSpinner.onItemSelectedListener = this
        } }

        mangaRecycler = binding.mangaRecyclerMain
        mangaRecycler.layoutManager = GridLayoutManager(context, 3)
        mangaRecycler.addItemDecoration(GridSpacingItemDeco(3,25,true))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        mangaAdapter = MangaAdapter(MangaDetailListener {
            it?.let {  navigateToMangaDetails(it) }
        })
        mangaAdapter.setListEndListener(this)
        mangaRecycler.adapter = mangaAdapter

        mangaViewModel.mangaRankingList.observe(viewLifecycleOwner) { resource ->
            when (resource?.status) {
                Status.LOADING -> {
                    mangaProgressBar.visibility = View.VISIBLE
                    mangaRecycler.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    mangaProgressBar.visibility = View.GONE
                    mangaRecycler.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        mangaViewModel.topMangaNextPage.observe(viewLifecycleOwner) { resource ->
            when (resource?.status) {
                Status.LOADING -> {
                    topMangaPaginationProgress.visibility = View.VISIBLE

                }
                Status.SUCCESS -> {
                    topMangaPaginationProgress.visibility = View.GONE

                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        mangaViewModel.listOfAllTopManga.observe(viewLifecycleOwner) {
            mangaAdapter.submitList(it)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        when(parent?.getItemAtPosition(pos).toString()){
            getString(R.string.manga_ranking_all) -> { loadSelectedMangaList(ALL.value) }
            getString(R.string.manga_ranking_manga) -> { loadSelectedMangaList(MANGA.value) }
            getString(R.string.manga_ranking_novels) -> { loadSelectedMangaList(NOVELS.value) }
            getString(R.string.manga_ranking_oneshots) -> { loadSelectedMangaList(ONESHOTS.value) }
            getString(R.string.manga_ranking_doujin) -> { loadSelectedMangaList(DOUJIN.value) }
            getString(R.string.manga_ranking_manhwa) -> { loadSelectedMangaList(MANHWA.value) }
            getString(R.string.manga_ranking_manhua) -> { loadSelectedMangaList(MANHUA.value) }
            getString(R.string.manga_ranking_popularity) -> { loadSelectedMangaList(BY_POPULARITY.value) }
            getString(R.string.manga_ranking_favorites) -> { loadSelectedMangaList(FAVORITE.value) }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private fun navigateToMangaDetails(mangaMalId: Int){
        this.findNavController().navigate(
            MangaFragmentDirections.actionMangaFragmentToMangaDetailsFragment(mangaMalId)
        )
    }

    private fun setupToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }

    private fun loadSelectedMangaList(rankingType:String){
        if(currentRankingType != rankingType){
            mangaViewModel.clearMangaList()
            mangaViewModel.setRankingType(rankingType)
            mangaViewModel.getMangaRankingList(null, DEFAULT_PAGE_LIMIT)
            currentRankingType = rankingType
        }
    }

    override fun onEndReached(position: Int) {
        mangaViewModel.getTopMangaNextPage()
    }

}

