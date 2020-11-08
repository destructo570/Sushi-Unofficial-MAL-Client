package com.destructo.sushi.ui.user.animeList

import androidx.lifecycle.LiveData
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
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class MyAnimeListRepository
@Inject
constructor(private val malApi: MalApi){

    var userAnimeListAll: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListWatching: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListCompleted: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListPlanToWatch: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListOnHold: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListDropped: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeStatus: MutableLiveData<Resource<UpdateUserAnime>> = MutableLiveData()

    var userAnimeRemove: MutableLiveData<Resource<Unit>> = MutableLiveData()

    fun getUserAnime(animeStatus:String?){
        when(animeStatus){
            UserAnimeStatus.COMPLETED.value ->{
                 getUserAnimeList(animeStatus, userAnimeListCompleted)
            }
            UserAnimeStatus.WATCHING.value ->{
                 getUserAnimeList(animeStatus, userAnimeListWatching)
            }
            UserAnimeStatus.ON_HOLD.value ->{
                 getUserAnimeList(animeStatus, userAnimeListOnHold)
            }
            UserAnimeStatus.PLAN_TO_WATCH.value ->{
                 getUserAnimeList(animeStatus, userAnimeListPlanToWatch)
            }
            UserAnimeStatus.DROPPED.value ->{
                 getUserAnimeList(animeStatus, userAnimeListDropped)
            }
            else ->{
                 getUserAnimeList(animeStatus, userAnimeListAll)
            }

        }
    }

    private fun getUserAnimeList(
        animeStatus:String?,
        animeList:MutableLiveData<Resource<UserAnimeList>>
    ) {
        animeList.value = Resource.loading(null)

        GlobalScope.launch {
            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                "@me", "100",
                animeStatus, UserAnimeSort.ANIME_TITLE.value, "",ALL_ANIME_FIELDS)
            try {
                val userAnime = getUserAnimeDeferred.await()
                withContext(Dispatchers.Main){
                    animeList.value = Resource.success(userAnime)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    animeList.value = Resource.error(e.message ?: "", null)}
            }
        }
    }

    fun addEpisode(animeId:String,numberOfEp:Int?) {
        userAnimeStatus.value = Resource.loading(null)

        GlobalScope.launch {
            val addEpisodeDeferred = malApi.updateUserAnime(animeId,
                null,null,null,numberOfEp,
                null,null,null,null,null)
            try {
                val animeStatus = addEpisodeDeferred.await()
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.success(animeStatus)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.error(e.message ?: "", null)}
            }
        }
    }

    fun updateAnimeUserList(animeId:String, status:String?=null,
                            is_rewatching:Boolean?=null, score:Int?=null,
                            num_watched_episodes:Int?=null, priority:Int?=null,
                            num_times_rewatched:Int?=null, rewatch_value:Int?=null,
                            tags:List<String>?=null, comments:String?=null) {
        userAnimeStatus.value = Resource.loading(null)

        GlobalScope.launch {
            val addEpisodeDeferred = malApi.updateUserAnime(animeId,
                status,is_rewatching,score,num_watched_episodes,
                priority,num_times_rewatched,rewatch_value,tags,comments)
            try {
                val animeStatus = addEpisodeDeferred.await()
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.success(animeStatus)
                }
            }catch (e: java.lang.Exception){
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.error(e.message ?: "", null)
                }
            }
        }
    }


}