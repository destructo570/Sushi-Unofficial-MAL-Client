package com.destructo.sushi.ui.user.mangaList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.adapter.pagerAdapter.FragmentPagerAdapter
import com.destructo.sushi.databinding.FragmentMyMangaListBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MyMangaListFragment : Fragment() {

    private lateinit var binding: FragmentMyMangaListBinding
    private lateinit var toolbar: Toolbar

    private lateinit var myMangaListPagerAdapter: FragmentPagerAdapter
    private lateinit var myMangaListViewPager: ViewPager2
    private lateinit var myMangaListTabLayout: TabLayout
    private lateinit var myMangaListTabMediator: TabLayoutMediator
    private val userMangaViewModel: UserMangaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userMangaViewModel.clearList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyMangaListBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        myMangaListViewPager = binding.myMangaListPager
        myMangaListTabLayout = binding.myMangaListTablayout
        toolbar = binding.toolbar

        myMangaListTabMediator =
            TabLayoutMediator(myMangaListTabLayout, myMangaListViewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = getString(R.string.reading)
                    }
                    1 -> {
                        tab.text = getString(R.string.plan_to_read)
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
            UserMangaReading(),
            UserMangaPlanToRead(),
            UserMangaOnHold(),
            UserMangaDropped(),
            UserMangaCompleted()

            )

        myMangaListPagerAdapter =
            FragmentPagerAdapter(fragmentList, childFragmentManager, lifecycle)
        myMangaListViewPager.adapter = myMangaListPagerAdapter
        myMangaListTabMediator.attach()

    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }
}