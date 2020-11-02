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

    val userAnimeListAll: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListAll

    val userAnimeListWatching: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListWatching

    val userAnimeListCompleted: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListCompleted

    val userAnimeListPlanToWatch: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListPlanToWatch

    val userAnimeListOnHold: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListOnHold

    val userAnimeListDropped: LiveData<Resource<UserAnimeList>> = myAnimeListRepo.userAnimeListDropped

    val userAnimeStatus: LiveData<Resource<UpdateUserAnime>> = myAnimeListRepo.userAnimeStatus

    fun addEpisodeAnime(animeId:String,numberOfEp:Int?){
        myAnimeListRepo.addEpisode(animeId, numberOfEp)
    }

    fun getUserAnimeList(animeStatus:String?){
        myAnimeListRepo.getUserAnime(animeStatus)
    }

}

