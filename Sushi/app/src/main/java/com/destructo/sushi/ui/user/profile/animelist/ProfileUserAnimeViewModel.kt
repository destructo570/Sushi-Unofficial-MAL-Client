package com.destructo.sushi.ui.user.profile.animelist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.user.animeList.ProfileUserAnimeList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileAnimeListDao
import kotlinx.coroutines.launch

class ProfileUserAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val profileRepo: ProfileUserAnimeRepository,
    private val profileAnimeListDao: ProfileAnimeListDao
): ViewModel() {

    val userAnimeList: LiveData<Resource<ProfileUserAnimeList>> = profileRepo.userAnimeList

    val getAnimeList = profileAnimeListDao.getAnimeList()

    fun getUserAnimeList(username:String, status: String) {
        viewModelScope.launch {
            profileRepo.getUserAnimeList(username, status)
        }
    }

    fun clearAnimeList(){
        profileAnimeListDao.deleteAllAnime()
        profileRepo.nextAnimePage = 1
    }
}