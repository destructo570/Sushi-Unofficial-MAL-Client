package com.destructo.sushi.ui.anime.characterDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.databinding.FragmentCharacterBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFragment() : Fragment() {

    private lateinit var binding: FragmentCharacterBinding
    private lateinit var characterPager: ViewPager2
    private lateinit var characterPagerAdapter: FragmentStateAdapter
    private lateinit var characterTabLayout: TabLayout
    private var characterArg: Int = 0
    private lateinit var characterTabMediator:TabLayoutMediator


    val characterViewModel: CharacterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            characterArg = CharacterFragmentArgs.fromBundle(requireArguments()).characterId
            characterViewModel.getCharacterDetails(characterArg)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterBinding
            .inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        characterPager = binding.characterViewPager
        characterTabLayout = binding.characterTabLayout


        characterTabMediator = TabLayoutMediator(characterTabLayout, characterPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.character_about)
                }
                1 -> {
                    tab.text = getString(R.string.character_voice_actor)
                }
                2 -> {
                    tab.text = getString(R.string.character_animeography)
                }
                3 -> {
                    tab.text = getString(R.string.character_mangaography)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        characterViewModel.character.observe(viewLifecycleOwner) { character ->
            binding.characterEntity = character
            val about = character.about as String

            val fragmentList = arrayListOf<Fragment>(
                CharacterAbout.newInstance(about),
                CharacterVoiceActors(),
                CharacterAnimeography(),
                CharacterMangaography()
            )


            setupViewPager(fragmentList)
        }


    }



    private fun setupViewPager(fragmentList:ArrayList<Fragment>){
        characterPagerAdapter = CharacterPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )
        characterPager.adapter = characterPagerAdapter
        characterTabMediator.attach()

    }


}

