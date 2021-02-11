package com.destructo.sushi.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.MANGA_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.PublishedMangaAdapter
import com.destructo.sushi.databinding.FragmentPersonAnimeStaffBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.GridSpacingItemDeco

class PublishedMangaFragment: Fragment(){

    private val personViewModel: PersonViewModel by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var binding: FragmentPersonAnimeStaffBinding
    private lateinit var publishedMangaAdapter: PublishedMangaAdapter
    private lateinit var publishedMangaRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonAnimeStaffBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        publishedMangaRecyclerView = binding.animeStaffRoleRecycler
        publishedMangaRecyclerView.layoutManager = GridLayoutManager(context,3)
        publishedMangaRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        publishedMangaRecyclerView.setHasFixedSize(true)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        publishedMangaAdapter = PublishedMangaAdapter(MalIdListener{
            it?.let {navigateToMangaDetail(it)}
        })
        publishedMangaRecyclerView.adapter = publishedMangaAdapter

        personViewModel.personData.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING->{

                }
                Status.SUCCESS->{
                    resource.data?.publishedManga?.let{
                        publishedMangaAdapter.submitList(it)
                    }
                }
                Status.ERROR->{

                }

            }
        }

    }

    private fun navigateToMangaDetail(mangaMalId:Int){
        this.findNavController().navigate(
            R.id.mangaDetailsFragment, bundleOf(Pair(MANGA_ID_ARG, mangaMalId))
        )

    }

}