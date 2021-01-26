package com.destructo.sushi.ui.common.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.destructo.sushi.databinding.FragmentCharacterAboutBinding

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
    ): View {
        val binding = FragmentCharacterAboutBinding
            .inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        characterAbout?.let {
            binding.characterAboutTxt.text = it.replace("\\n", "\n")
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