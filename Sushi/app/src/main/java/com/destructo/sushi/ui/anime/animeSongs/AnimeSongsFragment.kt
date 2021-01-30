package com.destructo.sushi.ui.anime.animeSongs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.adapter.pagerAdapter.FragmentPagerAdapter
import com.destructo.sushi.databinding.FragmentAnimeSongsBinding
import com.destructo.sushi.network.Status
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnimeSongsFragment : Fragment() {

    private lateinit var binding: FragmentAnimeSongsBinding
    private val animeSongsViewModel: AnimeSongsViewModel by viewModels()
    private var animeIdArg: Int = 0
    private lateinit var toolbar: Toolbar
    private lateinit var pager: ViewPager2
    private lateinit var pagerAdapter: FragmentStateAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var animeSongsProgressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            animeIdArg = AnimeSongsFragmentArgs.fromBundle(requireArguments()).animeId
            animeSongsViewModel.getAnimeById(animeIdArg)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAnimeSongsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        toolbar = binding.toolbar
        pager = binding.animeSongsViewPager
        tabLayout = binding.animeSongsTabLayout
        animeSongsProgressBar = binding.animeSongsProgressbar

        tabMediator = TabLayoutMediator(tabLayout, pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.opening)
                }
                1 -> {
                    tab.text = getString(R.string.ending)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        animeSongsViewModel.animeDetails.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING->{
                    animeSongsProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS->{

                    animeSongsProgressBar.visibility = View.GONE

                    val fragmentList = arrayListOf(
                        OpeningSongsFragment(),
                        EndingSongsFragment(),
                    )
                    setupViewPager(fragmentList)
                }
                Status.ERROR -> {

                }

            }
        }
    }

    private fun setupViewPager(fragmentList:ArrayList<Fragment>){
        pagerAdapter = FragmentPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )
        pager.adapter = pagerAdapter
        tabMediator.attach()
    }


    fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    }