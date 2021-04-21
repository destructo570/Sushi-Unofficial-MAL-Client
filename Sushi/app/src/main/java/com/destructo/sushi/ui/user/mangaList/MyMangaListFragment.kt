package com.destructo.sushi.ui.user.mangaList

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.IS_PRO_USER
import com.destructo.sushi.MANGA_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.pagerAdapter.FragmentBasePgerAdapter
import com.destructo.sushi.databinding.FragmentMyMangaListBinding
import com.destructo.sushi.enum.UserAnimeListSort
import com.destructo.sushi.enum.UserMangaListSort
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyMangaListFragment : BaseFragment() {

    private lateinit var binding: FragmentMyMangaListBinding
    private lateinit var toolbar: Toolbar

    private lateinit var myMangaListPagerAdapter: FragmentBasePgerAdapter
    private lateinit var myMangaListViewPager: ViewPager2
    private lateinit var myMangaListTabLayout: TabLayout
    private lateinit var myMangaListTabMediator: TabLayoutMediator
    private val userMangaViewModel: UserMangaViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

    @Inject
    lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            userMangaViewModel.clearList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyMangaListBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        myMangaListViewPager = binding.myMangaListPager
        myMangaListTabLayout = binding.myMangaListTablayout
        toolbar = binding.toolbar
        progressBar = binding.progressbar

        if (sharedPref.getBoolean(IS_PRO_USER, false)){
            binding.randomFab.show()
        }else{
            binding.randomFab.hide()
        }

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


        myMangaListViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.randomFab.setOnClickListener {
                            navigateToRandomAnime(UserMangaStatus.READING.value)
                        }
                    }
                    1 -> {
                        binding.randomFab.setOnClickListener {
                            navigateToRandomAnime(UserMangaStatus.PLAN_TO_READ.value)
                        }
                    }
                    2 -> {
                        binding.randomFab.setOnClickListener {
                            navigateToRandomAnime(UserMangaStatus.ON_HOLD.value)
                        }
                    }
                    3 -> {
                        binding.randomFab.setOnClickListener {
                            navigateToRandomAnime(UserMangaStatus.DROPPED.value)
                        }
                    }
                    4 -> {
                        binding.randomFab.setOnClickListener {
                            navigateToRandomAnime(UserMangaStatus.COMPLETED.value)
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
            UserMangaReading(),
            UserMangaPlanToRead(),
            UserMangaOnHold(),
            UserMangaDropped(),
            UserMangaCompleted()

            )

        myMangaListPagerAdapter =
            FragmentBasePgerAdapter(fragmentList, childFragmentManager, lifecycle)
        myMangaListViewPager.adapter = myMangaListPagerAdapter
        myMangaListViewPager.offscreenPageLimit = 5
        myMangaListTabMediator.attach()


        userMangaViewModel.userMangaListState.observe(viewLifecycleOwner) { resource ->
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

        userMangaViewModel.nextPage.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                userMangaViewModel.getNextPage()
            }
        }

        userMangaViewModel.userSortType.observe(viewLifecycleOwner) {
            when (it.getContentIfNotHandled()) {
                UserMangaListSort.BY_TITLE.value -> userMangaViewModel.getUserMangaList(
                    UserMangaListSort.BY_TITLE.value
                )
                UserMangaListSort.BY_SCORE.value -> userMangaViewModel.getUserMangaList(
                    UserMangaListSort.BY_SCORE.value
                )
                UserMangaListSort.BY_LAST_UPDATED.value -> userMangaViewModel.getUserMangaList(
                    UserMangaListSort.BY_LAST_UPDATED.value
                )
                else -> {
                }
            }
        }

    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }


        toolbar.inflateMenu(R.menu.user_list_sort)
        toolbar.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.sort_by_title -> {
                    userMangaViewModel.clearList()
                    userMangaViewModel.setSortType(UserMangaListSort.BY_TITLE.value)
                }
                R.id.sort_by_score -> {
                    userMangaViewModel.clearList()
                    userMangaViewModel.setSortType(UserAnimeListSort.BY_SCORE.value)
                }
                R.id.sort_by_last_updated -> {
                    userMangaViewModel.clearList()
                    userMangaViewModel.setSortType(UserMangaListSort.BY_LAST_UPDATED.value)
                }
            }
            true

        }

    }

    private fun navigateToRandomAnime(status: String){
        val malId = userMangaViewModel.getRandomManga(status)
        malId?.let{ navigateToMangaDetails(it) }
    }

    private fun navigateToMangaDetails(mangaIdArg: Int){
        this.findNavController().navigate(
            R.id.mangaDetailsFragment,
            bundleOf(Pair(MANGA_ID_ARG, mangaIdArg)),
            getAnimNavOptions()
        )
    }
}