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

    val userMangaList: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListAll

    val userMangaListReading: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListReading

    val userMangaListCompleted: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListCompleted

    val userMangaListPlanToRead: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListPlanToRead

    val userMangaListOnHold: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListOnHold

    val userMangaListDropped: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListDropped

    val userMangaStatus: LiveData<Resource<UpdateUserManga>> = myMangaListRepo.userMangaStatus

    fun addChapterManga(mangaId:String,numberOfCh:Int?){
        myMangaListRepo.addChapter(mangaId, numberOfCh)
    }

    fun getUserMangaList(mangaStatus:String?){
        myMangaListRepo.getUserManga(mangaStatus)
    }

}