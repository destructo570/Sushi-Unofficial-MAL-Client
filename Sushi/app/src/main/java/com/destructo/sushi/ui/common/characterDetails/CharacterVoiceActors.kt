package com.destructo.sushi.ui.common.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.PERSON_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentCharacterVoiceActorsBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterVoiceActors : BaseFragment() {

    private lateinit var voiceAdapter: VoiceActorAdapter
    private lateinit var voiceRecyclerView: RecyclerView
    private val characterViewModel: CharacterViewModel
            by viewModels(ownerProducer = {requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            FragmentCharacterVoiceActorsBinding
                .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        voiceRecyclerView = binding.characterRecyclerMain
        voiceRecyclerView.layoutManager = GridLayoutManager(context,3)
        voiceRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        voiceRecyclerView.setHasFixedSize(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        voiceAdapter = VoiceActorAdapter(MalIdListener{
            it?.let {navigateToPersonFragment(it)}
        })

        characterViewModel.character.observe(viewLifecycleOwner){character->
            voiceAdapter.submitList(character.voiceActors)
            voiceRecyclerView.adapter = voiceAdapter
        }
    }

    private fun navigateToPersonFragment(personId:Int){

        this.findNavController().navigate(
            R.id.personFragment,
            bundleOf(Pair(PERSON_ID_ARG, personId)),
            getAnimNavOptions()
        )

    }
}