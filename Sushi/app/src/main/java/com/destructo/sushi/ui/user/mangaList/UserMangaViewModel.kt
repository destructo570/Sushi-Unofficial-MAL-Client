package com.destructo.sushi.ui.user.mangaList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.mal.userMangaList.UserMangaData
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserMangaListDao
import kotlinx.coroutines.launch

class UserMangaViewModel@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val myMangaListRepo:MyMangaListRepository,
    private val userMangaListDao: UserMangaListDao
) : ViewModel() {

    val userMangaListAll: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListAll

    val userMangaListReading: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListReading

    val userMangaListCompleted: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListCompleted

    val userMangaListPlanToRead: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListPlanToRead

    val userMangaListOnHold: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListOnHold

    val userMangaListDropped: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListDropped

    val userMangaStatus: LiveData<Resource<UpdateUserManga>> = myMangaListRepo.userMangaStatus

    var userMangaListReadingNext: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListReadingNext

    var userMangaListCompletedNext: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListCompletedNext

    var userMangaListPlanToReadNext: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListPlanToReadNext

    var userMangaListOnHoldNext: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListOnHoldNext

    var userMangaListDroppedNext: LiveData<Resource<UserMangaList>> = myMangaListRepo.userMangaListDroppedNext

    var allNewList: MutableLiveData<MutableList<UserMangaData?>> = MutableLiveData()

    var userMangaList = userMangaListDao.getUserMangaList()

    fun addChapterManga(mangaId:String,numberOfCh:Int?){
        myMangaListRepo.addChapter(mangaId, numberOfCh)
    }

    fun getUserMangaList(mangaStatus:String?){
        myMangaListRepo.getUserManga(mangaStatus)
    }

    fun getNextPage(mangaStatus:String?){
        myMangaListRepo.getUserMangaNext(mangaStatus)
    }

    fun getUserMangaByStatus(mangaStatus:String): LiveData<List<UserMangaData>>{
        return userMangaListDao.getUserMangaListByStatus(mangaStatus)
    }

    fun clearList(){
        viewModelScope.launch{userMangaListDao.clear()}
    }

}