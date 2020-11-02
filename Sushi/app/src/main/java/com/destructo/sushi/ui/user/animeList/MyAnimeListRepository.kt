package com.destructo.sushi.ui.user.animeList

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.enum.mal.UserAnimeSort
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class MyAnimeListRepository
@Inject
constructor(private val malApi: MalApi){

    fun getUserAnimeList(animeStatus:String?)
            :MutableLiveData<Resource<UserAnimeList>>{
        val result = MutableLiveData<Resource<UserAnimeList>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                "@me", "100",
                animeStatus, UserAnimeSort.ANIME_TITLE.value, "",ALL_ANIME_FIELDS)
            try {
                val userAnime = getUserAnimeDeferred.await()
                withContext(Dispatchers.Main){
                    result.value = Resource.success(userAnime)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    result.value = Resource.error(e.message ?: "", null)}
            }
        }
        return  result
    }

    fun addEpisode(animeId:String,numberOfEp:Int?)
    :MutableLiveData<Resource<UpdateUserAnime>>{
        val result = MutableLiveData<Resource<UpdateUserAnime>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val addEpisodeDeferred = malApi.updateUserAnime(animeId,
                null,null,null,numberOfEp,
                null,null,null,null,null)
            try {
                val animeStatus = addEpisodeDeferred.await()
                withContext(Dispatchers.Main){
                    result.value = Resource.success(animeStatus)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    result.value = Resource.error(e.message ?: "", null)}
            }
        }
        return result
    }
}