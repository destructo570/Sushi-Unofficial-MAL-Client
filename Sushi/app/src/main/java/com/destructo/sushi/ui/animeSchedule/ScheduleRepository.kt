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

    fun getAnimeSchedule(weekDay: String): MutableLiveData<Resource<Schedule>>{
        val result = MutableLiveData<Resource<Schedule>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val getAnimeScheduleDeferred = jikanApi.getAnimeScheduleAsync(weekDay)
            try {
                val animeSchedule = getAnimeScheduleDeferred.await()
                withContext(Dispatchers.Main){
                    result.value = Resource.success(animeSchedule)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    result.value = Resource.error(e.message ?: "", null)
                }
            }
        }
        return result
    }
}