package com.destructo.sushi.ui.manga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.databinding.FragmentMangaBinding
import com.destructo.sushi.enum.TopSubtype
import com.destructo.sushi.enum.mal.MangaRankingType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MangaFragment : Fragment(),AdapterView.OnItemSelectedListener {

    private val mangaViewModel:MangaViewModel by viewModels()

    private lateinit var binding:FragmentMangaBinding
    private lateinit var mangaRecycler:RecyclerView
    private lateinit var mangaAdapter:MangaAdapter
    private lateinit var mangaTypeSpinner:Spinner

    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mangaViewModel.getTopMangaList(MangaRankingType.ALL.value,"500",null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMangaBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        toolbar = binding.toolbar

        mangaTypeSpinner = binding.mangaRankingSpinner
        context?.let { ArrayAdapter.createFromResource(
            it,R.array.manga_ranking_type,android.R.layout.simple_spinner_item).also {adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mangaTypeSpinner.adapter = adapter
            mangaTypeSpinner.onItemSelectedListener = this
        } }

        mangaRecycler = binding.mangaRecyclerMain
        mangaRecycler.layoutManager = GridLayoutManager(context, 3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        mangaAdapter = MangaAdapter()

        mangaViewModel.topManga.observe(viewLifecycleOwner){
            it?.let {topManga->
                mangaAdapter.submitList(topManga.data)
                mangaRecycler.apply {
                    adapter = mangaAdapter
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        when(parent?.getItemAtPosition(pos).toString()){
            getString(R.string.manga_ranking_all) -> {mangaViewModel.getTopMangaList(MangaRankingType.ALL.value,"500",null)}
            getString(R.string.manga_ranking_manga) -> {mangaViewModel.getTopMangaList(MangaRankingType.MANGA.value,"500",null)}
            getString(R.string.manga_ranking_novels) -> {mangaViewModel.getTopMangaList(MangaRankingType.NOVELS.value,"500",null)}
            getString(R.string.manga_ranking_oneshots) -> {mangaViewModel.getTopMangaList(MangaRankingType.ONESHOTS.value,"500",null)}
            getString(R.string.manga_ranking_doujin) -> {mangaViewModel.getTopMangaList(MangaRankingType.DOUJIN.value,"500",null)}
            getString(R.string.manga_ranking_manhwa) -> {mangaViewModel.getTopMangaList(MangaRankingType.MANHWA.value,"500",null)}
            getString(R.string.manga_ranking_manhua) -> {mangaViewModel.getTopMangaList(MangaRankingType.MANHUA.value,"500",null)}
            getString(R.string.manga_ranking_popularity) -> {mangaViewModel.getTopMangaList(MangaRankingType.BY_POPULARITY.value,"500",null)}
            getString(R.string.manga_ranking_favorites) -> {mangaViewModel.getTopMangaList(MangaRankingType.FAVORITE.value,"500",null)}
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        mangaViewModel.getTopMangaList(MangaRankingType.ALL.value,"500",null)
    }


    private fun setupToolbar(){
        toolbar.setNavigationOnClickListener {
                view-> view.findNavController().navigateUp()
        }
    }


}

