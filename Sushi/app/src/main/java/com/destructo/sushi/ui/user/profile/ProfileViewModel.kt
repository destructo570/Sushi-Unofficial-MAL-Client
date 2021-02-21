package com.destructo.sushi.ui.user.profile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.user.UserInfo
import com.destructo.sushi.model.jikan.user.animeList.ProfileUserAnimeList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileAnimeListDao
import kotlinx.coroutines.launch

class ProfileViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val profileRepo:ProfileRepository,
    private val profileAnimeListDao: ProfileAnimeListDao
): ViewModel(){

    val userInformation: LiveData<Resource<UserInfo>> = profileRepo.userInfo

    val userAnimeList: LiveData<Resource<ProfileUserAnimeList>> = profileRepo.userAnimeList

    val userInformationMalApi: LiveData<Resource<com.destructo.sushi.model.mal.userInfo.UserInfo>> = profileRepo.userInfoMalApi

    val getAnimeList = profileAnimeListDao.getAnimeList()

    fun getUserInfo(fields:String) {
        viewModelScope.launch {
            profileRepo.getUserInfo(fields)
        }
    }

    fun getUserAnimeList(username:String, status: String) {
        viewModelScope.launch {
            profileRepo.getUserAnimeList(username, status)
        }
    }

    fun getUserInfoFromMalApi(fields:String) {
        viewModelScope.launch {
            profileRepo.getUserInfoFromMalApi(fields)
        }
    }

    fun clearAnimeList(){
        profileAnimeListDao.deleteAllAnime()
        profileRepo.nextPage = 1
    }

}