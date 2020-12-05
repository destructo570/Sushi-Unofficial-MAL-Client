package com.destructo.sushi.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.destructo.sushi.databinding.FragmentPersonAboutBinding
import com.destructo.sushi.network.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonAboutFragment : Fragment() {

    private val personViewModel: PersonViewModel by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var binding: FragmentPersonAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonAboutBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        personViewModel.personData.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING->{

                }
                Status.SUCCESS->{
                    binding.personEntity = resource.data
                }
                Status.ERROR->{

                }

            }
        }

    }
}