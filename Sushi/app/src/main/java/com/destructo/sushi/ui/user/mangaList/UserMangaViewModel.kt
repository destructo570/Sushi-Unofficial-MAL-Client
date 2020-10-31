package com.destructo.sushi.ui.user.mangaList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.enum.mal.UserMangaSort
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.MalApi
import kotlinx.coroutines.launch
import timber.log.Timber

class UserMangaViewModel@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    val malApi: MalApi
) : ViewModel() {

    private val _userMangaList: MutableLiveData<UserMangaList> = MutableLiveData()
    val userMangaList: LiveData<UserMangaList>
        get() = _userMangaList

    private val _userMangaListReading: MutableLiveData<UserMangaList> = MutableLiveData()
    val userMangaListReading: LiveData<UserMangaList>
        get() = _userMangaListReading

    private val _userMangaListCompleted: MutableLiveData<UserMangaList> = MutableLiveData()
    val userMangaListCompleted: LiveData<UserMangaList>
        get() = _userMangaListCompleted

    private val _userMangaListPlanToRead: MutableLiveData<UserMangaList> = MutableLiveData()
    val userMangaListPlanToRead: LiveData<UserMangaList>
        get() = _userMangaListPlanToRead

    private val _userMangaListOnHold: MutableLiveData<UserMangaList> = MutableLiveData()
    val userMangaListOnHold: LiveData<UserMangaList>
        get() = _userMangaListOnHold

    private val _userMangaListDropped: MutableLiveData<UserMangaList> = MutableLiveData()
    val userMangaListDropped: LiveData<UserMangaList>
        get() = _userMangaListDropped

    private val _userMangaStatus: MutableLiveData<UpdateUserManga> = MutableLiveData()
    val userMangaStatus: LiveData<UpdateUserManga>
        get() = _userMangaStatus


    fun getUserMangaList(status: String?) {
        viewModelScope.launch {
            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", "100",
                status, UserMangaSort.MANGA_TITLE.value, ""
                , ALL_MANGA_FIELDS)
            try {
                val userMangaList = getUserMangaDeferred.await()
                _userMangaList.value = userMangaList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUserMangaListOnHold() {
        viewModelScope.launch {
            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", "100",
                UserMangaStatus.ON_HOLD.value, UserMangaSort.MANGA_TITLE.value, ""
                , ALL_MANGA_FIELDS)
            try {
                val userMangaList = getUserMangaDeferred.await()
                _userMangaListOnHold.value = userMangaList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUserMangaListCompleted() {
        viewModelScope.launch {
            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", "100",
                UserMangaStatus.COMPLETED.value, UserMangaSort.MANGA_TITLE.value, ""
                , ALL_MANGA_FIELDS)
            try {
                val userMangaList = getUserMangaDeferred.await()
                _userMangaListCompleted.value = userMangaList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUserMangaListReading() {
        viewModelScope.launch {
            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", "100",
                UserMangaStatus.READING.value, UserMangaSort.MANGA_TITLE.value, ""
                , ALL_MANGA_FIELDS)
            try {
                val userMangaList = getUserMangaDeferred.await()
                _userMangaListReading.value = userMangaList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUserMangaListDropped() {
        viewModelScope.launch {
            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", "100",
                UserMangaStatus.DROPPED.value, UserMangaSort.MANGA_TITLE.value, "", ALL_MANGA_FIELDS
            )
            try {
                val userMangaList = getUserMangaDeferred.await()
                _userMangaListDropped.value = userMangaList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUserMangaListPtr() {
        viewModelScope.launch {
            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", "100",
                UserMangaStatus.PLAN_TO_READ.value, UserMangaSort.MANGA_TITLE.value, ""
                , ALL_MANGA_FIELDS)
            try {
                val userMangaList = getUserMangaDeferred.await()
                _userMangaListPlanToRead.value = userMangaList
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }
        }
    }


    fun addChapterManga(mangaId:String,numberOfCh:Int?){
        viewModelScope.launch {
            val addChapterDeferred = malApi.updateUserManga(mangaId,
                null,null,null,null,
                numberOfCh,null,null,
                null,null,null)

            try {
                val mangaStatus = addChapterDeferred.await()
                _userMangaStatus.value = mangaStatus
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }

}