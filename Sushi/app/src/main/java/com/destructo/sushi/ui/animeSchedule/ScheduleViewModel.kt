package com.destructo.sushi.ui.animeSchedule

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.jikan.schedule.Schedule
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber

class ScheduleViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val scheduleRepo: ScheduleRepository
)
    :ViewModel() {

    private var _animeSchedule: MutableLiveData<Resource<Schedule>> = MutableLiveData()
    val animeSchedule:LiveData<Resource<Schedule>>
        get() = _animeSchedule


    fun getAnimeSchedule(weekDay:String){
        _animeSchedule = scheduleRepo.getAnimeSchedule(weekDay)
    }


}