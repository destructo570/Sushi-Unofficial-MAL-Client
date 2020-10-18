package com.destructo.sushi.ui.animeSchedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeScheduleBinding
import com.destructo.sushi.ui.anime.seasonalAnime.SeasonAnimeAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AnimeScheduleFragment : Fragment(),AdapterView.OnItemSelectedListener{

    private val scheduleViewModel:ScheduleViewModel by viewModels()
    private lateinit var scheduleRecycler:RecyclerView
    private lateinit var binding:FragmentAnimeScheduleBinding
    private lateinit var schedulePagerAdapter: ScheduleAdapter
    private lateinit var weekDaySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleViewModel.getAnimeSchdule("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimeScheduleBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        scheduleRecycler = binding.scheduleRecyclerMain
        weekDaySpinner = binding.scheduleDaySpinner
        context?.let { ArrayAdapter.createFromResource(
            it,R.array.weekDay_array,android.R.layout.simple_spinner_item).also {adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            weekDaySpinner.adapter = adapter
            weekDaySpinner.onItemSelectedListener = this
        } }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        schedulePagerAdapter = ScheduleAdapter()

        scheduleViewModel.animeSchedule.observe(viewLifecycleOwner){
            it?.let {animeSchedule->
                schedulePagerAdapter.submitList(animeSchedule.monday)
                scheduleRecycler.adapter = schedulePagerAdapter
            }
        }


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {

    when(parent?.getItemAtPosition(pos).toString()){
        getString(R.string.string_monday) -> {schedulePagerAdapter.submitList(scheduleViewModel.animeSchedule.value?.monday)}
        getString(R.string.string_tuesday) -> {schedulePagerAdapter.submitList(scheduleViewModel.animeSchedule.value?.tuesday)}
        getString(R.string.string_wednesday) -> {schedulePagerAdapter.submitList(scheduleViewModel.animeSchedule.value?.wednesday) }
        getString(R.string.string_thursday) -> {schedulePagerAdapter.submitList(scheduleViewModel.animeSchedule.value?.thursday)}
        getString(R.string.string_friday) -> {schedulePagerAdapter.submitList(scheduleViewModel.animeSchedule.value?.friday)}
        getString(R.string.string_saturday) -> {schedulePagerAdapter.submitList(scheduleViewModel.animeSchedule.value?.saturday)}
        getString(R.string.string_sunday) -> {schedulePagerAdapter.submitList(scheduleViewModel.animeSchedule.value?.sunday)}
        getString(R.string.string_other) -> {schedulePagerAdapter.submitList(scheduleViewModel.animeSchedule.value?.other)}
        getString(R.string.string_unknown) -> {schedulePagerAdapter.submitList(scheduleViewModel.animeSchedule.value?.unknown)}
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}