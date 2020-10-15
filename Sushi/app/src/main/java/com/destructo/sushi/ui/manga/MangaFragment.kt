package com.destructo.sushi.ui.manga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.databinding.FragmentMangaBinding
import com.destructo.sushi.enum.TopSubtype
import com.destructo.sushi.enum.mal.MangaRankingType
import com.destructo.sushi.ui.anime.topAnime.TopAnimeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MangaFragment : Fragment(),AdapterView.OnItemSelectedListener {

    private val mangaViewModel:MangaViewModel by viewModels()

    private lateinit var binding:FragmentMangaBinding
    private lateinit var mangaRecycler:RecyclerView
    private lateinit var mangaAdapter:MangaAdapter
    private lateinit var mangaTypeSpinner:Spinner

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
            "All" -> {mangaViewModel.getTopMangaList(MangaRankingType.ALL.value,"500",null)}
            "Manga" -> {mangaViewModel.getTopMangaList(MangaRankingType.MANGA.value,"500",null)}
            "Novels" -> {mangaViewModel.getTopMangaList(MangaRankingType.NOVELS.value,"500",null)}
            "Oneshots" -> {mangaViewModel.getTopMangaList(MangaRankingType.ONESHOTS.value,"500",null)}
            "Doujin" -> {mangaViewModel.getTopMangaList(MangaRankingType.DOUJIN.value,"500",null)}
            "Manhwa" -> {mangaViewModel.getTopMangaList(MangaRankingType.MANHWA.value,"500",null)}
            "Manhua" -> {mangaViewModel.getTopMangaList(MangaRankingType.MANHUA.value,"500",null)}
            "By Popularity" -> {mangaViewModel.getTopMangaList(MangaRankingType.BY_POPULARITY.value,"500",null)}
            "Favorite" -> {mangaViewModel.getTopMangaList(MangaRankingType.FAVORITE.value,"500",null)}
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        mangaViewModel.getTopMangaList(MangaRankingType.ALL.value,"500",null)
    }

    }

