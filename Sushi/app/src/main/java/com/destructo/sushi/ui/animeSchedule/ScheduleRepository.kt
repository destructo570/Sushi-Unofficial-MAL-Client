package com.destructo.sushi.ui.animeSchedule

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.jikan.schedule.Schedule
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class ScheduleRepository
@Inject
constructor(private val jikanApi: JikanApi){

    var animeSchedule: MutableLiveData<Resource<Schedule>> = MutableLiveData()

    fun getAnimeSchedule(weekDay: String){
        animeSchedule.value = Resource.loading(null)

        GlobalScope.launch {
            val getAnimeScheduleDeferred = jikanApi.getAnimeScheduleAsync(weekDay)
            try {
                val animeScheduleList = getAnimeScheduleDeferred.await()
                withContext(Dispatchers.Main){
                    animeSchedule.value = Resource.success(animeScheduleList)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    animeSchedule.value = Resource.error(e.message ?: "", null)
                }
            }
        }
    }
}