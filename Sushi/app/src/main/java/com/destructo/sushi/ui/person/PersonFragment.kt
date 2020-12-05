package com.destructo.sushi.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.adapter.pagerAdapter.FragmentPagerAdapter
import com.destructo.sushi.databinding.FragmentPersonBinding
import com.destructo.sushi.network.Status
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PersonFragment : Fragment() {

    private val personViewModel: PersonViewModel by viewModels()
    private lateinit var binding:FragmentPersonBinding
    private lateinit var personPager: ViewPager2
    private lateinit var personPagerAdapter: FragmentStateAdapter
    private lateinit var personTabLayout: TabLayout
    private var personArg: Int = 0
    private lateinit var personTabMediator: TabLayoutMediator
    private lateinit var toolbar: Toolbar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null){
            Timber.e("Here")
            personArg = PersonFragmentArgs.fromBundle(requireArguments()).personId
            personViewModel.getPersonData(personArg)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        toolbar = binding.toolbar
        personPager = binding.personViewPager
        personTabLayout = binding.personTabLayout

        personTabMediator = TabLayoutMediator(personTabLayout, personPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.about)
                }
                1 -> {
                    tab.text = getString(R.string.voice_acting)
                }
                2 -> {
                    tab.text = getString(R.string.anime_staff_role)
                }
            }
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()

        personViewModel.personData.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING->{

                }
                Status.SUCCESS->{
                    binding.personEntity = resource.data
                    val fragmentList = arrayListOf(
                        PersonAboutFragment(),
                        PersonVoiceActingFragment(),
                        PersonAnimeStaffFragment()
                    )
                    setupViewPager(fragmentList)
                }
                Status.ERROR->{

                }

            }
        }
    }

    private fun setupViewPager(fragmentList:ArrayList<Fragment>){
        personPagerAdapter = FragmentPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )
        personPager.adapter = personPagerAdapter
        personTabMediator.attach()

    }

    fun setupToolbar(){
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}