package com.destructo.sushi.ui.anime.characterDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.FragmentCharacterVoiceActorsBinding
import com.destructo.sushi.util.GridSpacingItemDeco
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterVoiceActors(
) : Fragment() {

    private lateinit var voiceAdapter: VoiceActorAdapter
    private lateinit var voiceRecyclerView: RecyclerView
    private val characterViewModel: CharacterViewModel
            by viewModels(ownerProducer = {requireParentFragment()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentCharacterVoiceActorsBinding.inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        voiceRecyclerView = binding.characterRecyclerMain
        voiceRecyclerView.layoutManager = GridLayoutManager(context,3)
        voiceRecyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
        voiceRecyclerView.setHasFixedSize(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        voiceAdapter = VoiceActorAdapter()

        characterViewModel.character.observe(viewLifecycleOwner){character->
            voiceAdapter.submitList(character.voiceActors)
            voiceRecyclerView.adapter = voiceAdapter
        }
    }
}