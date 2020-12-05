package com.destructo.sushi.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.FragmentPersonVoiceActingBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonVoiceActingFragment : Fragment() {

    private val personViewModel: PersonViewModel by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var binding: FragmentPersonVoiceActingBinding
    private lateinit var voiceActingAdapter: VoiceActingAdapter
    private lateinit var voiceActingRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonVoiceActingBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        voiceActingRecyclerView = binding.voiceActingRecycler
        voiceActingRecyclerView.layoutManager = LinearLayoutManager(context)
        voiceActingRecyclerView.setHasFixedSize(true)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        voiceActingAdapter = VoiceActingAdapter(MalIdListener{animeId->
            animeId?.let {navigateToAnimeDetail(it)}
        }, MalIdListener {characterId->
            characterId?.let {navigateToCharacterDetail(it)}
        })

        voiceActingRecyclerView.adapter = voiceActingAdapter

        personViewModel.personData.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING->{

                }
                Status.SUCCESS->{
                    resource.data?.voiceActingRoles?.let{
                        voiceActingAdapter.submitList(it)
                    }

                }
                Status.ERROR->{

                }

            }
        }

    }

    private fun navigateToAnimeDetail(malId:Int){
        this.findNavController().navigate(
            PersonFragmentDirections.actionPersonFragmentToAnimeDetailFragment(malId)
        )
    }

    private fun navigateToCharacterDetail(malId:Int){
        this.findNavController().navigate(
            PersonFragmentDirections.actionPersonFragmentToCharacterFragment(malId)

        )
    }

}