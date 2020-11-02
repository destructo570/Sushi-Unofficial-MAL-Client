package com.destructo.sushi.ui.animeSchedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeScheduleBinding
import com.destructo.sushi.model.jikan.season.AnimeSubEntity
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.seasonalAnime.SeasonAnimeAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_anime_schedule.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AnimeScheduleFragment : Fragment(){

    private val scheduleViewModel:ScheduleViewModel by viewModels()
    private lateinit var binding:FragmentAnimeScheduleBinding
    private lateinit var schedulePagerAdapter: SchedulePagerAdapter
    private lateinit var scheduleViewPager:ViewPager2
    private lateinit var scheduleTabLayout:TabLayout
    private lateinit var scheduleTabMediator:TabLayoutMediator
    private lateinit var listOfAnimeSchedule:MutableList<List<AnimeSubEntity?>?>

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var animeScheduleProgress:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            scheduleViewModel.getAnimeSchedule("")
            drawerLayout = requireActivity().drawer_layout
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimeScheduleBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        scheduleViewPager = binding.animeSchedulePager
        scheduleTabLayout = binding.animeScheduleTablayout
        toolbar = binding.toolbar
        animeScheduleProgress = binding.animeScheduleProgress

        scheduleTabMediator = TabLayoutMediator(scheduleTabLayout, scheduleViewPager) { tab, position ->
            when(position){
                0 ->{tab.text = getString(R.string.string_monday)}
                1 ->{tab.text = getString(R.string.string_tuesday)}
                2 ->{tab.text = getString(R.string.string_wednesday)}
                3 ->{tab.text = getString(R.string.string_thursday)}
                4 ->{tab.text = getString(R.string.string_friday)}
                5 ->{tab.text = getString(R.string.string_saturday)}
                6 ->{tab.text = getString(R.string.string_sunday)}
                7 ->{tab.text = getString(R.string.string_other)}
                8 ->{tab.text = getString(R.string.string_unknown)}
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        schedulePagerAdapter = SchedulePagerAdapter()

        scheduleViewModel.animeSchedule.observe(viewLifecycleOwner){resource->

            when(resource.status){

                Status.LOADING->{
                    animeScheduleProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    animeScheduleProgress.visibility = View.GONE
                    resource?.data?.let {animeSchedule->
                        listOfAnimeSchedule =
                            mutableListOf(
                                animeSchedule.monday,
                                animeSchedule.tuesday,
                                animeSchedule.wednesday,
                                animeSchedule.thursday,
                                animeSchedule.friday,
                                animeSchedule.saturday,
                                animeSchedule.sunday,
                                animeSchedule.other,
                                animeSchedule.unknown,)
                        setupViewPager(listOfAnimeSchedule)

                    }
                }
                Status.ERROR->{Timber.e("Error: %s", resource.message)
                }
            }
        }
    }


    private fun setupViewPager(listOfAnime:MutableList<List<AnimeSubEntity?>?>){

        schedulePagerAdapter.submitList(listOfAnime)
        scheduleViewPager.offscreenPageLimit = 9
        scheduleViewPager.adapter = schedulePagerAdapter
        scheduleTabMediator.attach()

    }

    private fun setupToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }

}