package com.destructo.sushi.ui.user.animeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentMyAnimeListBinding
import com.destructo.sushi.ui.anime.AnimeFragmentDirections
import com.destructo.sushi.ui.anime.AnimeUpdateListener
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

@AndroidEntryPoint
class MyAnimeListFragment : Fragment(){

    private lateinit var binding: FragmentMyAnimeListBinding
    private lateinit var myAnimeListPagerAdapter: UserAnimePagerAdapter
    private lateinit var myAnimeListViewPager: ViewPager2
    private lateinit var myAnimeListTabLayout: TabLayout
    private lateinit var myAnimeListTabMediator: TabLayoutMediator
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyAnimeListBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        myAnimeListViewPager = binding.myAnimeListPager
        myAnimeListTabLayout = binding.myAnimeListTablayout
        toolbar = binding.toolbar

        myAnimeListTabMediator =
            TabLayoutMediator(myAnimeListTabLayout, myAnimeListViewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = getString(R.string.anime_list_tab_all)
                    }
                    1 -> {
                        tab.text = getString(R.string.anime_list_tab_watching)
                    }
                    2 -> {
                        tab.text = getString(R.string.anime_list_tab_completed)
                    }
                    3 -> {
                        tab.text = getString(R.string.anime_list_tab_onhold)
                    }
                    4 -> {
                        tab.text = getString(R.string.anime_list_tab_dropped)
                    }
                    5 -> {
                        tab.text = getString(R.string.anime_list_tab_ptw)
                    }
                }
            }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()

        val fragmentList = arrayListOf(
            UserAnimeAll(),
            UserAnimeWatching(),
            UserAnimeCompleted(),
            UserAnimeOnHold(),
            UserAnimeDropped(),
            UserAnimePlanToWatch()
        )

        myAnimeListPagerAdapter =
            UserAnimePagerAdapter(fragmentList, childFragmentManager, lifecycle)
        myAnimeListViewPager.adapter = myAnimeListPagerAdapter
        myAnimeListTabMediator.attach()

    }



    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }




}
