package com.destructo.sushi.ui.user.animeList

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.adapter.pagerAdapter.FragmentPagerAdapter
import com.destructo.sushi.databinding.FragmentMyAnimeListBinding
import com.destructo.sushi.enum.UserAnimeListSort
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MyAnimeListFragment : Fragment(){

    private lateinit var binding: FragmentMyAnimeListBinding
    private lateinit var myAnimeListPagerAdapter: FragmentPagerAdapter
    private lateinit var myAnimeListViewPager: ViewPager2
    private lateinit var myAnimeListTabLayout: TabLayout
    private lateinit var myAnimeListTabMediator: TabLayoutMediator
    private lateinit var toolbar: Toolbar
    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            userAnimeViewModel.clearList()
            userAnimeViewModel.setSortType(UserAnimeListSort.BY_TITLE.value)
        }
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
                        tab.text = getString(R.string.watching)
                    }
                    1 -> {
                        tab.text = getString(R.string.plan_to_watch)
                    }
                    2 -> {
                        tab.text = getString(R.string.on_hold)
                    }
                    3 -> {
                        tab.text = getString(R.string.dropped)
                    }
                    4 -> {
                        tab.text = getString(R.string.completed)
                    }
                }
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()

        val fragmentList = arrayListOf(
            UserAnimeWatching(),
            UserAnimePlanToWatch(),
            UserAnimeOnHold(),
            UserAnimeDropped(),
            UserAnimeCompleted()
            )

        myAnimeListPagerAdapter =
            FragmentPagerAdapter(fragmentList, childFragmentManager, lifecycle)
        myAnimeListViewPager.adapter = myAnimeListPagerAdapter
        myAnimeListTabMediator.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.user_list_sort, menu)

    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
        //toolbar.inflateMenu(R.menu.user_list_sort)
//        toolbar.setOnMenuItemClickListener { item ->
//
//            when(item.itemId){
//                R.id.sort_by_title -> {
//                    userAnimeViewModel.clearList()
//                    userAnimeViewModel.setSortType(UserAnimeListSort.BY_TITLE.value)                }
//                R.id.sort_by_score -> {
//                    userAnimeViewModel.clearList()
//                    userAnimeViewModel.setSortType(UserAnimeListSort.BY_SCORE.value)                }
//                R.id.sort_by_last_updated -> {
//                    userAnimeViewModel.clearList()
//                    userAnimeViewModel.setSortType(UserAnimeListSort.BY_LAST_UPDATED.value)                }
//
//            }
//            true
//
//        }
    }
}
