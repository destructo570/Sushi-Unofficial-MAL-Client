package com.destructo.sushi.ui.anime.characterDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.destructo.sushi.databinding.FragmentCharacterAboutBinding
import com.destructo.sushi.databinding.FragmentCharacterBinding

private const val ABOUT: String = "param1"

class CharacterAbout : Fragment() {

    private var characterAbout: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            characterAbout = it.getString(ABOUT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharacterAboutBinding
            .inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        characterAbout?.let {
            binding.characterAboutTxt.text = it
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            CharacterAbout().apply {
                arguments = Bundle().apply {
                    putString(ABOUT, param1)
                }
            }
    }
}