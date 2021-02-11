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
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.AnimeStaffRoleAdapter
import com.destructo.sushi.databinding.FragmentPersonAnimeStaffBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonAnimeStaffFragment : Fragment() {

    private val personViewModel: PersonViewModel by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var binding: FragmentPersonAnimeStaffBinding
    private lateinit var animeStaffAdapter: AnimeStaffRoleAdapter
    private lateinit var animeStaffRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonAnimeStaffBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        animeStaffRecyclerView = binding.animeStaffRoleRecycler
        animeStaffRecyclerView.layoutManager = GridLayoutManager(context,3)
        animeStaffRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        animeStaffRecyclerView.setHasFixedSize(true)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animeStaffAdapter = AnimeStaffRoleAdapter(MalIdListener{
            it?.let {navigateToAnimeDetail(it)}
        })
        animeStaffRecyclerView.adapter = animeStaffAdapter

        personViewModel.personData.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING->{

                }
                Status.SUCCESS->{
                    resource.data?.animeStaffPositions?.let{
                        animeStaffAdapter.submitList(it)
                    }

                }
                Status.ERROR->{

                }

            }
        }

    }

    private fun navigateToAnimeDetail(animeMalId:Int){
        this.findNavController().navigate(
            R.id.animeDetailFragment, bundleOf(Pair(ANIME_ID_ARG, animeMalId))
        )

    }

}