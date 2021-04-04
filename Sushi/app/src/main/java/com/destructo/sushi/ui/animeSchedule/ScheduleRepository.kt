package com.destructo.sushi.ui.animeSchedule

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.jikan.schedule.Schedule
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import javax.inject.Inject

class ScheduleRepository
@Inject
constructor(private val jikanApi: JikanApi) {

    var animeSchedule: MutableLiveData<Resource<Schedule>> = MutableLiveData()

    suspend fun getAnimeSchedule(weekDay: String) {
        animeSchedule.value = Resource.loading(null)
        val getAnimeScheduleDeferred = jikanApi.getAnimeScheduleAsync(weekDay)
        try {
            val animeScheduleList = getAnimeScheduleDeferred.await()
            animeSchedule.value = Resource.success(animeScheduleList)

        } catch (e: Exception) {
            animeSchedule.value = Resource.error(e.message ?: "", null)
        }
    }
}