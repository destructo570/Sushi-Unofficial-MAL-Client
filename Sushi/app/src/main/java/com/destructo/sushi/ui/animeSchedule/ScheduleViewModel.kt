package com.destructo.sushi.ui.animeSchedule

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.jikan.schedule.Schedule
import com.destructo.sushi.network.JikanApi
import kotlinx.coroutines.launch
import timber.log.Timber

class ScheduleViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val jikanApi: JikanApi
)
    :ViewModel() {

    private val _animeSchedule: MutableLiveData<Schedule> = MutableLiveData()
    val animeSchedule:LiveData<Schedule>
    get() = _animeSchedule

    fun getAnimeSchdule(weekday:String){
        viewModelScope.launch {
            val getAnimeScheduleDeferred = jikanApi.getAnimeScheduleAsync(weekday)
            try {
                val animeSchedule = getAnimeScheduleDeferred.await()
                _animeSchedule.value = animeSchedule
            }catch (e:Exception){
                Timber.e("Error: ${e.message}")
            }
        }
    }
}