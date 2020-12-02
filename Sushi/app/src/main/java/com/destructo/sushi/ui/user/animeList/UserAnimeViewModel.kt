package com.destructo.sushi.ui.user.animeList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserAnimeListDao
import kotlinx.coroutines.launch

class UserAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val myAnimeListRepo: MyAnimeListRepository,
    private val userAnimeListDao: UserAnimeListDao
) : ViewModel() {

    val userAnimeListAll: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListAll

    val userAnimeListWatching: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListWatching

    val userAnimeListCompleted: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListCompleted

    val userAnimeListPlanToWatch: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListPlanToWatch

    val userAnimeListOnHold: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListOnHold

    val userAnimeListDropped: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListDropped

    val userAnimeStatus: LiveData<Resource<UpdateUserAnime>> = myAnimeListRepo.userAnimeStatus

    var userAnimeListAllNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListAllNext

    var userAnimeListWatchingNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListWatchingNext

    var userAnimeListCompletedNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListCompletedNext

    var userAnimeListPlanToWatchNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListPlanToWatchNext

    var userAnimeListOnHoldNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListOnHoldNext

    var userAnimeListDroppedNext: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListDroppedNext

    var allNewList: MutableLiveData<MutableList<UserAnimeData?>> = MutableLiveData()

    var userAnimeList = userAnimeListDao.getUserAnimeList()


    fun addEpisodeAnime(animeId:String,numberOfEp:Int?){
        myAnimeListRepo.addEpisode(animeId, numberOfEp)
    }

    fun getUserAnimeList(animeStatus:String?){
        myAnimeListRepo.getUserAnime(animeStatus)
    }

    fun getNextPage(animeStatus:String?){
        myAnimeListRepo.getUserAnimeNext(animeStatus)
    }

    fun setSortType(sort_by:String){
        myAnimeListRepo.anime_sort_type = sort_by
    }

    fun getUserAnimeByStatus(animeStatus:String): LiveData<List<UserAnimeData>>{
        return userAnimeListDao.getUserAnimeListByStatus(animeStatus)
    }

    fun clearList(){
        viewModelScope.launch{userAnimeListDao.clear()}
    }

}

