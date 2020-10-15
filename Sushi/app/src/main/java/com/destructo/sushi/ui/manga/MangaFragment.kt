package com.destructo.sushi.ui.manga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.databinding.FragmentMangaBinding
import com.destructo.sushi.enum.TopSubtype
import com.destructo.sushi.ui.anime.topAnime.TopAnimeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MangaFragment : Fragment() {

    private val mangaViewModel:MangaViewModel by viewModels()

    private lateinit var binding:FragmentMangaBinding
    private lateinit var mangaRecycler:RecyclerView
    private lateinit var mangaAdapter:MangaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mangaViewModel.getTopMangaList("all","500",null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMangaBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
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
}