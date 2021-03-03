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

    val userAnimeListState: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeList

    val userAnimeStatus: LiveData<Resource<UpdateUserAnime>> = myAnimeListRepo.userAnimeStatus

    var userAnimeList = userAnimeListDao.getUserAnimeList()

    fun addEpisodeAnime(animeId:String,numberOfEp:Int?, status:String?){
        myAnimeListRepo.addEpisode(animeId, numberOfEp, status)
    }

    fun getUserAnimeList(){
        myAnimeListRepo.getUserAnimeList()
    }

    fun setSortType(sort_by:String){
        myAnimeListRepo.animeSortType = sort_by
    }

    fun clearList(){
        viewModelScope.launch{userAnimeListDao.clear()}
    }

    fun getUserListByStatus(status: String) = userAnimeListDao.getUserAnimeList()

    fun clearAnimeDetails(animeId:Int){
        viewModelScope.launch{animeDetailsDao.deleteAnimeDetailById(animeId)}
    }

}

