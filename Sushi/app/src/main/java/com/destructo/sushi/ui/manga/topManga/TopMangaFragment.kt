package com.destructo.sushi.ui.manga.topManga

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.DEFAULT_PAGE_LIMIT
import com.destructo.sushi.MANGA_ID_ARG
import com.destructo.sushi.NSFW_TAG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.MangaRankingAdapter
import com.destructo.sushi.databinding.FragmentTopMangaBinding
import com.destructo.sushi.enum.mal.MangaRankingType.*
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TopMangaFragment : BaseFragment(), AdapterView.OnItemSelectedListener, ListEndListener {

    private val topMangaViewModel: TopMangaViewModel by viewModels()

    private lateinit var binding:FragmentTopMangaBinding
    private lateinit var mangaRecycler:RecyclerView
    private lateinit var mangaRankingAdapter: MangaRankingAdapter
    private lateinit var mangaTypeSpinner:Spinner
    private lateinit var toolbar: Toolbar
    private lateinit var mangaProgressBar: ProgressBar
    private lateinit var topMangaPaginationProgress: LinearLayout
    private var currentRankingType:String = ""

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState != null){
            val savedString = savedInstanceState.getString("ranking_type")
            savedString?.let {
                currentRankingType = it
            }
        }else{
            topMangaViewModel.getMangaRankingList(
                null,
                DEFAULT_PAGE_LIMIT,
                sharedPref.getBoolean(NSFW_TAG, false))

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("ranking_type", currentRankingType)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopMangaBinding.inflate(inflater,container,false).apply {
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
        mangaRankingAdapter = MangaRankingAdapter(MalIdListener {
            it?.let {  navigateToMangaDetails(it) }
        })
        mangaRankingAdapter.setListEndListener(this)
        mangaRecycler.adapter = mangaRankingAdapter

        topMangaViewModel.mangaRankingList.observe(viewLifecycleOwner) { resource ->
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
                else -> {}
            }
        }

        topMangaViewModel.topMangaNextPage.observe(viewLifecycleOwner) { resource ->
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
                else -> {}
            }
        }

        topMangaViewModel.listOfAllTopManga.observe(viewLifecycleOwner) {
            mangaRankingAdapter.submitList(it)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        when(parent?.getItemAtPosition(pos).toString()){
            getString(R.string.all) -> { loadSelectedMangaList(ALL.value) }
            getString(R.string.manga) -> { loadSelectedMangaList(MANGA.value) }
            getString(R.string.novels) -> { loadSelectedMangaList(NOVELS.value) }
            getString(R.string.oneshots) -> { loadSelectedMangaList(ONESHOTS.value) }
            getString(R.string.doujin) -> { loadSelectedMangaList(DOUJIN.value) }
            getString(R.string.manhwa) -> { loadSelectedMangaList(MANHWA.value) }
            getString(R.string.manhua) -> { loadSelectedMangaList(MANHUA.value) }
            getString(R.string.popularity) -> { loadSelectedMangaList(BY_POPULARITY.value) }
            getString(R.string.favorites) -> { loadSelectedMangaList(FAVORITE.value) }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private fun navigateToMangaDetails(mangaMalId: Int){
        this.findNavController().navigate(
            R.id.mangaDetailsFragment,
            bundleOf(Pair(MANGA_ID_ARG, mangaMalId)),
            getAnimNavOptions()
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
            topMangaViewModel.clearMangaList()
            topMangaViewModel.setRankingType(rankingType)
            topMangaViewModel.getMangaRankingList(
                null,
                DEFAULT_PAGE_LIMIT,
                sharedPref.getBoolean(NSFW_TAG, false))
            currentRankingType = rankingType
        }
    }

    override fun onEndReached(position: Int) {
        topMangaViewModel.getTopMangaNextPage(sharedPref.getBoolean(NSFW_TAG, false))
    }

}

