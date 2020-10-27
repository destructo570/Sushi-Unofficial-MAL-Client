package com.destructo.sushi.ui.user.animeList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.enum.mal.UserAnimeSort
import com.destructo.sushi.enum.mal.UserAnimeSort.*
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.enum.mal.UserAnimeStatus.*
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.network.MalApi
import kotlinx.coroutines.launch
import timber.log.Timber

class UserAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    val malApi: MalApi
) : ViewModel() {

    private val _userAnimeList: MutableLiveData<UserAnimeList> = MutableLiveData()
    val userAnimeList: LiveData<UserAnimeList>
        get() = _userAnimeList

    private val _userAnimeListWatching: MutableLiveData<UserAnimeList> = MutableLiveData()
    val userAnimeListWatching: LiveData<UserAnimeList>
        get() = _userAnimeListWatching

    private val _userAnimeListCompleted: MutableLiveData<UserAnimeList> = MutableLiveData()
    val userAnimeListCompleted: LiveData<UserAnimeList>
        get() = _userAnimeListCompleted

    private val _userAnimeListPlanToWatch: MutableLiveData<UserAnimeList> = MutableLiveData()
    val userAnimeListPlanToWatch: LiveData<UserAnimeList>
        get() = _userAnimeListPlanToWatch

    private val _userAnimeListOnHold: MutableLiveData<UserAnimeList> = MutableLiveData()
    val userAnimeListOnHold: LiveData<UserAnimeList>
        get() = _userAnimeListOnHold

    private val _userAnimeListDropped: MutableLiveData<UserAnimeList> = MutableLiveData()
    val userAnimeListDropped: LiveData<UserAnimeList>
        get() = _userAnimeListDropped


    fun getUserAnimeList(status: String?) {
        viewModelScope.launch {
            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                "@me", "100",
                status, ANIME_TITLE.value, ""
                , ALL_ANIME_FIELDS)
            try {
                val userAnimeList = getUserAnimeDeferred.await()
                _userAnimeList.value = userAnimeList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUserAnimeListOnHold() {
        viewModelScope.launch {
            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                "@me", "100",
                ON_HOLD.value, ANIME_TITLE.value, ""
                , ALL_ANIME_FIELDS)
            try {
                val userAnimeList = getUserAnimeDeferred.await()
                _userAnimeListOnHold.value = userAnimeList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUserAnimeListCompleted() {
        viewModelScope.launch {
            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                "@me", "100",
                COMPLETED.value, ANIME_TITLE.value, ""
                , ALL_ANIME_FIELDS)
            try {
                val userAnimeList = getUserAnimeDeferred.await()
                _userAnimeListCompleted.value = userAnimeList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUserAnimeListWatching() {
        viewModelScope.launch {
            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                "@me", "100",
                WATCHING.value, ANIME_TITLE.value, ""
                , ALL_ANIME_FIELDS)
            try {
                val userAnimeList = getUserAnimeDeferred.await()
                _userAnimeListWatching.value = userAnimeList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUserAnimeListDropped() {
        viewModelScope.launch {
            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                "@me", "100",
                DROPPED.value, ANIME_TITLE.value, ""
                , ALL_ANIME_FIELDS)
            try {
                val userAnimeList = getUserAnimeDeferred.await()
                _userAnimeListDropped.value = userAnimeList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUserAnimeListPtw() {
        viewModelScope.launch {
            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                "@me", "100",
                PLAN_TO_WATCH.value, ANIME_TITLE.value, ""
                , ALL_ANIME_FIELDS
            )
            try {
                val userAnimeList = getUserAnimeDeferred.await()
                _userAnimeListPlanToWatch.value = userAnimeList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

}