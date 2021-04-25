package com.destructo.sushi.ui.user.mangaList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.enum.UserMangaListSort
import com.destructo.sushi.model.database.UserMangaEntity
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.MangaDetailsDao
import com.destructo.sushi.room.UserMangaDao
import com.destructo.sushi.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserMangaViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val myMangaListRepo:MyMangaListRepository,
    private val userMangaDao: UserMangaDao,
    private val mangaDetailsDao: MangaDetailsDao
) : ViewModel() {

    val userMangaListState: LiveData<Resource<List<UserMangaEntity>>> = myMangaListRepo.userMangaList

    val userMangaStatus: LiveData<Resource<UpdateUserManga>> = myMangaListRepo.userMangaStatus

    var userMangaList = userMangaDao.getUserMangaList()

    var userSortType = MutableLiveData(Event(UserMangaListSort.BY_TITLE.value))

    val nextPage = myMangaListRepo.nextPage

    fun addChapterManga(mangaId:String,numberOfCh:Int?, status:String?){
        viewModelScope.launch(Dispatchers.IO){ myMangaListRepo.addChapter(mangaId, numberOfCh, status) }
    }

    fun getUserMangaList(sortType:String){
        viewModelScope.launch(Dispatchers.IO){ myMangaListRepo.getUserMangaList(sortType) }
    }

    fun getNextPage(){
        viewModelScope.launch(Dispatchers.IO){ myMangaListRepo.getNextPage() }
    }

    fun getMangaListByStatus(status: String): List<UserMangaEntity>?{
        return userMangaList.value?.filter {(it.myMangaListStatus?.status == status)}
    }

//
//    fun getMangaListByStatuss(status: String): List<UserMangaEntity>?{
//        var result: List<UserMangaEntity>? = null
//
//        withTimeoutOrNull(2000) {
//            val list = userMangaList.value?.filter {(it.myMangaListStatus?.status == status)}
//            withContext(Dispatchers.Main){
//                result = list
//            }
//        }
//        return result
//    }


    fun getRandomManga(status: String): Int? {
        val list = getMangaListByStatus(status)
        if (!list.isNullOrEmpty()){
            return list.random().malId
        }
        return null
    }


    fun setSortType(sort_by: String) {
        userSortType.value = Event(sort_by)
    }

    fun clearList(){
        viewModelScope.launch{userMangaDao.clear()}
    }

    fun clearMangaDetail(mangaId: Int){
        viewModelScope.launch{mangaDetailsDao.deleteMangaDetailById(mangaId)}
    }

}