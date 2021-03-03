package com.destructo.sushi.ui.user.animeList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeDetailsDao
import com.destructo.sushi.room.UserAnimeDao
import kotlinx.coroutines.launch

class UserAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val myAnimeListRepo: MyAnimeListRepository,
    private val userAnimeListDao: UserAnimeDao,
    private val animeDetailsDao: AnimeDetailsDao
) : ViewModel() {

    //val userAnimeListState: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeList

    val userAnimeStatus: LiveData<Resource<UpdateUserAnime>> = myAnimeListRepo.userAnimeStatus

    var userAnimeList = userAnimeListDao.getUserAnimeList()

    val animeListSortType = myAnimeListRepo.animeSortType

    val userAnimeListWatching: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListWatching

    val userAnimeListCompleted: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListCompleted

    val userAnimeListPlanToWatch: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListPlanToWatch

    val userAnimeListOnHold: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListOnHold

    val userAnimeListDropped: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListDropped

    var userAnimeListWatchingNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListWatchingNext

    var userAnimeListCompletedNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListCompletedNext

    var userAnimeListPlanToWatchNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListPlanToWatchNext

    var userAnimeListOnHoldNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListOnHoldNext

    var userAnimeListDroppedNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListDroppedNext

    fun addEpisodeAnime(animeId:String,numberOfEp:Int?, status:String?){
        myAnimeListRepo.addEpisode(animeId, numberOfEp, status)
    }

    fun getUserAnimeList(animeStatus:String?){
        myAnimeListRepo.getUserAnime(animeStatus)
    }

    fun setSortType(sort_by:String){
        myAnimeListRepo.animeSortType.value = sort_by
    }

    fun getNextPage(animeStatus:String?){
        myAnimeListRepo.getUserAnimeNext(animeStatus)
    }

    fun clearList(){
        viewModelScope.launch{userAnimeListDao.clear()}
    }

    fun getUserAnimeByStatus(status: String) = userAnimeListDao.getUserAnimeListByStatus(status)

    fun clearAnimeDetails(animeId:Int){
        viewModelScope.launch{animeDetailsDao.deleteAnimeDetailById(animeId)}
    }


}

