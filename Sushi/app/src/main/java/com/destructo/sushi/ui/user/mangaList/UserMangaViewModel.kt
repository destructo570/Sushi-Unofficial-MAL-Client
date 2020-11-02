package com.destructo.sushi.ui.user.mangaList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.enum.mal.UserMangaSort
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.enum.mal.UserMangaStatus.*
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

class UserMangaViewModel@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val myMangaListRepo:MyMangaListRepository
) : ViewModel() {

    private var _userMangaListAll: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()
    val userMangaList: LiveData<Resource<UserMangaList>>
        get() = _userMangaListAll

    private var _userMangaListReading: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()
    val userMangaListReading: LiveData<Resource<UserMangaList>>
        get() = _userMangaListReading

    private var _userMangaListCompleted: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()
    val userMangaListCompleted: LiveData<Resource<UserMangaList>>
        get() = _userMangaListCompleted

    private var _userMangaListPlanToRead: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()
    val userMangaListPlanToRead: LiveData<Resource<UserMangaList>>
        get() = _userMangaListPlanToRead

    private var _userMangaListOnHold: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()
    val userMangaListOnHold: LiveData<Resource<UserMangaList>>
        get() = _userMangaListOnHold

    private var _userMangaListDropped: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()
    val userMangaListDropped: LiveData<Resource<UserMangaList>>
        get() = _userMangaListDropped

    private var _userMangaStatus: MutableLiveData<Resource<UpdateUserManga>> = MutableLiveData()
    val userMangaStatus: LiveData<Resource<UpdateUserManga>>
        get() = _userMangaStatus

    fun addChapterManga(mangaId:String,numberOfCh:Int?){
        _userMangaStatus = myMangaListRepo.addChapter(mangaId, numberOfCh)
    }

    fun getUserMangaList(mangaStatus:String?){
        when(mangaStatus){
            READING.value ->{
                _userMangaListReading = myMangaListRepo.getUserMangaList(mangaStatus)
            }
            ON_HOLD.value ->{
                _userMangaListOnHold = myMangaListRepo.getUserMangaList(mangaStatus)
            }
            COMPLETED.value ->{
                _userMangaListCompleted = myMangaListRepo.getUserMangaList(mangaStatus)
            }
            DROPPED.value ->{
                _userMangaListDropped = myMangaListRepo.getUserMangaList(mangaStatus)
            }
            PLAN_TO_READ.value ->{
                _userMangaListPlanToRead = myMangaListRepo.getUserMangaList(mangaStatus)
            }
            else ->{
                _userMangaListAll = myMangaListRepo.getUserMangaList(mangaStatus)
            }

        }
    }

}