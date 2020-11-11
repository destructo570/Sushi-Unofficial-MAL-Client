package com.destructo.sushi.ui.manga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.databinding.FragmentMangaBinding
import com.destructo.sushi.enum.TopSubtype
import com.destructo.sushi.enum.mal.MangaRankingType
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.AnimeFragmentDirections
import com.destructo.sushi.ui.manga.mangaDetails.MangaDetailListener
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

@AndroidEntryPoint
class MangaFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val mangaViewModel:MangaViewModel by viewModels()

    private lateinit var binding:FragmentMangaBinding
    private lateinit var mangaRecycler:RecyclerView
    private lateinit var mangaAdapter:MangaAdapter
    private lateinit var mangaTypeSpinner:Spinner
    private lateinit var toolbar: Toolbar
    private lateinit var mangaProgressBar: ProgressBar
    private var currentMangaList:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            mangaViewModel.getTopMangaList(MangaRankingType.ALL.value,"500",null)
        }
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

        mangaViewModel.topManga.observe(viewLifecycleOwner){resource->
            when (resource.status){
                Status.LOADING ->{
                    mangaRecycler.visibility = View.INVISIBLE
                    mangaProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS ->{
                    mangaRecycler.visibility = View.VISIBLE
                    mangaProgressBar.visibility = View.GONE
                    resource?.data?.let {topManga->
                        mangaAdapter.submitList(topManga.data)
                        mangaRecycler.adapter = mangaAdapter
                    }
                }
                Status.ERROR ->{Timber.e("Error: %s", resource.message)}
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        when(parent?.getItemAtPosition(pos).toString()){
            getString(R.string.manga_ranking_all) -> {
                loadSelectedMangaList(MangaRankingType.ALL.value)
            }
            getString(R.string.manga_ranking_manga) -> {
                loadSelectedMangaList(MangaRankingType.MANGA.value)
            }
            getString(R.string.manga_ranking_novels) -> {
                loadSelectedMangaList(MangaRankingType.NOVELS.value)
            }
            getString(R.string.manga_ranking_oneshots) -> {
                loadSelectedMangaList(MangaRankingType.ONESHOTS.value)
            }
            getString(R.string.manga_ranking_doujin) -> {
                loadSelectedMangaList(MangaRankingType.DOUJIN.value)
            }
            getString(R.string.manga_ranking_manhwa) -> {
                loadSelectedMangaList(MangaRankingType.MANHWA.value)
            }
            getString(R.string.manga_ranking_manhua) -> {
                loadSelectedMangaList(MangaRankingType.MANHUA.value)
            }
            getString(R.string.manga_ranking_popularity) -> {
                loadSelectedMangaList(MangaRankingType.BY_POPULARITY.value)
            }
            getString(R.string.manga_ranking_favorites) -> {
               loadSelectedMangaList(MangaRankingType.FAVORITE.value)
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        mangaViewModel.getTopMangaList(MangaRankingType.ALL.value,"500",null)
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
        if(currentMangaList != rankingType){
            mangaViewModel.getTopMangaList(rankingType,"500",null)
            currentMangaList = rankingType
        }
    }


}

