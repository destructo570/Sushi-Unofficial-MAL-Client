package com.destructo.sushi.ui.user.mangaList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeBinding
import com.destructo.sushi.databinding.FragmentMyAnimeListBinding
import com.destructo.sushi.databinding.FragmentMyMangaListBinding
import com.destructo.sushi.ui.user.animeList.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MyMangaListFragment : Fragment() {

    private lateinit var binding: FragmentMyMangaListBinding
    private lateinit var toolbar: Toolbar

    private lateinit var myMangaListPagerAdapter: UserMangaPagerAdapter
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
                        tab.text = getString(R.string.manga_list_tab_reading)
                    }
                    1 -> {
                        tab.text = getString(R.string.manga_list_tab_completed)
                    }
                    2 -> {
                        tab.text = getString(R.string.manga_list_tab_onhold)
                    }
                    3 -> {
                        tab.text = getString(R.string.manga_list_tab_dropped)
                    }
                    4 -> {
                        tab.text = getString(R.string.manga_list_tab_ptr)
                    }
                }
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        val fragmentList = arrayListOf(
            //UserMangaAll(),
            UserMangaReading(),
            UserMangaCompleted(),
            UserMangaOnHold(),
            UserMangaDropped(),
            UserMangaPlanToRead()
        )

        myMangaListPagerAdapter =
            UserMangaPagerAdapter(fragmentList, childFragmentManager, lifecycle)
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