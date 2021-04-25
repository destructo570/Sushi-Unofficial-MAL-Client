package com.destructo.sushi.ui.user.animeList

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.IS_PRO_USER
import com.destructo.sushi.R
import com.destructo.sushi.adapter.pagerAdapter.FragmentBasePgerAdapter
import com.destructo.sushi.databinding.FragmentMyAnimeListBinding
import com.destructo.sushi.enum.UserAnimeListSort
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyAnimeListFragment : BaseFragment() {

    private lateinit var binding: FragmentMyAnimeListBinding
    private lateinit var myAnimeListPagerAdapter: FragmentBasePgerAdapter
    private lateinit var myAnimeListViewPager: ViewPager2
    private lateinit var myAnimeListTabLayout: TabLayout
    private lateinit var myAnimeListTabMediator: TabLayoutMediator
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar

    @Inject
    lateinit var sharedPref: SharedPreferences

    private val userAnimeViewModel: UserAnimeViewModel
            by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMyAnimeListBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        myAnimeListViewPager = binding.myAnimeListPager
        myAnimeListTabLayout = binding.myAnimeListTablayout
        toolbar = binding.toolbar
        progressBar = binding.progressbar

        if (sharedPref.getBoolean(IS_PRO_USER, false)){
            binding.randomFab.show()
        }else{
            binding.randomFab.hide()
        }

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

        myAnimeListViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.randomFab.setOnClickListener {
                            navigateToRandomAnime(UserAnimeStatus.WATCHING.value)
                        }
                    }
                    1 -> {
                        binding.randomFab.setOnClickListener {
                            navigateToRandomAnime(UserAnimeStatus.PLAN_TO_WATCH.value)
                        }
                    }
                    2 -> {
                        binding.randomFab.setOnClickListener {
                            navigateToRandomAnime(UserAnimeStatus.ON_HOLD.value)
                        }
                    }
                    3 -> {
                        binding.randomFab.setOnClickListener {
                            navigateToRandomAnime(UserAnimeStatus.DROPPED.value)
                        }
                    }
                    4 -> {
                        binding.randomFab.setOnClickListener {
                            navigateToRandomAnime(UserAnimeStatus.COMPLETED.value)
                        }
                    }
                }
            }
        })

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
            FragmentBasePgerAdapter(fragmentList, childFragmentManager, lifecycle)
        myAnimeListViewPager.adapter = myAnimeListPagerAdapter
        myAnimeListViewPager.offscreenPageLimit = 5
        myAnimeListTabMediator.attach()

        userAnimeViewModel.userAnimeListState.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        userAnimeViewModel.nextPage.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                userAnimeViewModel.getNextPage()
            }
        }

        userAnimeViewModel.userSortType.observe(viewLifecycleOwner) {
            when (it.getContentIfNotHandled()) {
                UserAnimeListSort.BY_TITLE.value -> userAnimeViewModel.getUserAnimeList(
                    UserAnimeListSort.BY_TITLE.value
                )
                UserAnimeListSort.BY_SCORE.value -> userAnimeViewModel.getUserAnimeList(
                    UserAnimeListSort.BY_SCORE.value
                )
                UserAnimeListSort.BY_LAST_UPDATED.value -> userAnimeViewModel.getUserAnimeList(
                    UserAnimeListSort.BY_LAST_UPDATED.value
                )
                else -> {
                }
            }
        }


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

        toolbar.inflateMenu(R.menu.user_list_sort)
        toolbar.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.sort_by_title -> {
                    userAnimeViewModel.clearList()
                    userAnimeViewModel.setSortType(UserAnimeListSort.BY_TITLE.value)
                    true
                }
                R.id.sort_by_score -> {
                    userAnimeViewModel.clearList()
                    userAnimeViewModel.setSortType(UserAnimeListSort.BY_SCORE.value)
                    true
                }
                R.id.sort_by_last_updated -> {
                    userAnimeViewModel.clearList()
                    userAnimeViewModel.setSortType(UserAnimeListSort.BY_LAST_UPDATED.value)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

        }
    }


    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment,
            bundleOf(Pair(ANIME_ID_ARG, animeMalId)),
            getAnimNavOptions()
        )
    }

    private fun navigateToRandomAnime(status: String){
       val malId = userAnimeViewModel.getRandomAnime(status)
        malId?.let{ navigateToAnimeDetails(it) }
    }

}
