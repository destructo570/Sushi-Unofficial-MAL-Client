package com.destructo.sushi.ui.user.animeList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.enum.mal.UserAnimeSort
import com.destructo.sushi.enum.mal.UserAnimeSort.*
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.enum.mal.UserAnimeStatus.*
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

class UserAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    val myAnimeListRepo: MyAnimeListRepository
) : ViewModel() {

    private var _userAnimeListAll: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()
    val userAnimeListAll: LiveData<Resource<UserAnimeList>>
        get() = _userAnimeListAll

    private var _userAnimeListWatching: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()
    val userAnimeListWatching: LiveData<Resource<UserAnimeList>>
        get() = _userAnimeListWatching

    private var _userAnimeListCompleted: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()
    val userAnimeListCompleted: LiveData<Resource<UserAnimeList>>
        get() = _userAnimeListCompleted

    private var _userAnimeListPlanToWatch: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()
    val userAnimeListPlanToWatch: LiveData<Resource<UserAnimeList>>
        get() = _userAnimeListPlanToWatch

    private var _userAnimeListOnHold: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()
    val userAnimeListOnHold: LiveData<Resource<UserAnimeList>>
        get() = _userAnimeListOnHold

    private var _userAnimeListDropped: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()
    val userAnimeListDropped: LiveData<Resource<UserAnimeList>>
        get() = _userAnimeListDropped

    private var _userAnimeStatus: MutableLiveData<Resource<UpdateUserAnime>> = MutableLiveData()
    val userAnimeStatus: LiveData<Resource<UpdateUserAnime>>
        get() = _userAnimeStatus


//    fun getUserAnimeList(status: String?) {
//        viewModelScope.launch {
//            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
//                "@me", "100",
//                status, ANIME_TITLE.value, ""
//                , ALL_ANIME_FIELDS)
//            try {
//                val userAnimeList = getUserAnimeDeferred.await()
//                _userAnimeList.value = userAnimeList
//            } catch (e: Exception) {
//                Timber.e("Error: %s", e.message)
//            }
//        }
//    }
//
//    fun getUserAnimeListOnHold() {
//        viewModelScope.launch {
//            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
//                "@me", "100",
//                ON_HOLD.value, ANIME_TITLE.value, ""
//                , ALL_ANIME_FIELDS)
//            try {
//                val userAnimeList = getUserAnimeDeferred.await()
//                _userAnimeListOnHold.value = userAnimeList
//            } catch (e: Exception) {
//                Timber.e("Error: %s", e.message)
//            }
//        }
//    }
//
//    fun getUserAnimeListCompleted() {
//        viewModelScope.launch {
//            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
//                "@me", "100",
//                COMPLETED.value, ANIME_TITLE.value, ""
//                , ALL_ANIME_FIELDS)
//            try {
//                val userAnimeList = getUserAnimeDeferred.await()
//                _userAnimeListCompleted.value = userAnimeList
//            } catch (e: Exception) {
//                Timber.e("Error: %s", e.message)
//            }
//        }
//    }
//
//    fun getUserAnimeListWatching() {
//        viewModelScope.launch {
//            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
//                "@me", "100",
//                WATCHING.value, ANIME_TITLE.value, ""
//                , ALL_ANIME_FIELDS)
//            try {
//                val userAnimeList = getUserAnimeDeferred.await()
//                _userAnimeListWatching.value = userAnimeList
//            } catch (e: Exception) {
//                Timber.e("Error: %s", e.message)
//            }
//        }
//    }
//
//    fun getUserAnimeListDropped() {
//        viewModelScope.launch {
//            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
//                "@me", "100",
//                DROPPED.value, ANIME_TITLE.value, ""
//                , ALL_ANIME_FIELDS)
//            try {
//                val userAnimeList = getUserAnimeDeferred.await()
//                _userAnimeListDropped.value = userAnimeList
//            } catch (e: Exception) {
//                Timber.e("Error: %s", e.message)
//            }
//        }
//    }
//
//    fun getUserAnimeListPtw() {
//        viewModelScope.launch {
//            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
//                "@me", "100",
//                PLAN_TO_WATCH.value, ANIME_TITLE.value, ""
//                , ALL_ANIME_FIELDS
//            )
//            try {
//                val userAnimeList = getUserAnimeDeferred.await()
//                _userAnimeListPlanToWatch.value = userAnimeList
//            } catch (e: Exception) {
//                Timber.e("Error: %s", e.message)
//            }
//        }
//    }

    fun addEpisodeAnime(animeId:String,numberOfEp:Int?){
        _userAnimeStatus = myAnimeListRepo.addEpisode(animeId, numberOfEp)
    }

    fun getUserAnimeList(animeStatus:String?){
        when(animeStatus){
            COMPLETED.value ->{
                _userAnimeListCompleted = myAnimeListRepo.getUserAnimeList(animeStatus)
            }
            WATCHING.value ->{
                _userAnimeListWatching = myAnimeListRepo.getUserAnimeList(animeStatus)
            }
            ON_HOLD.value ->{
                _userAnimeListOnHold = myAnimeListRepo.getUserAnimeList(animeStatus)
            }
            PLAN_TO_WATCH.value ->{
                _userAnimeListPlanToWatch = myAnimeListRepo.getUserAnimeList(animeStatus)
            }
            DROPPED.value ->{
                _userAnimeListDropped = myAnimeListRepo.getUserAnimeList(animeStatus)
            }
            else ->{
                _userAnimeListAll = myAnimeListRepo.getUserAnimeList(animeStatus)
            }

        }
    }

}