package com.destructo.sushi.ui.user.profile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.user.UserInfo
import com.destructo.sushi.model.jikan.user.animeList.ProfileUserAnimeList
import com.destructo.sushi.model.jikan.user.friends.UserFriends
import com.destructo.sushi.model.jikan.user.mangaList.ProfileUserMangaList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileAnimeListDao
import com.destructo.sushi.room.ProfileMangaListDao
import com.destructo.sushi.room.ProfileUserFriendListDao
import kotlinx.coroutines.launch

class ProfileViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val profileRepo:ProfileRepository,
    private val profileAnimeListDao: ProfileAnimeListDao,
    private val profileUserFriendListDao: ProfileUserFriendListDao,
    private val profileUserMangaListDao: ProfileMangaListDao


): ViewModel(){

    val userInformation: LiveData<Resource<UserInfo>> = profileRepo.userInfo

    val userAnimeList: LiveData<Resource<ProfileUserAnimeList>> = profileRepo.userAnimeList

    val userMangaList: LiveData<Resource<ProfileUserMangaList>> = profileRepo.userMangaList

    val userFriendList: LiveData<Resource<UserFriends>> = profileRepo.userFriendList

    val userInformationMalApi: LiveData<Resource<com.destructo.sushi.model.mal.userInfo.UserInfo>> = profileRepo.userInfoMalApi

    val getMangaList = profileUserMangaListDao.getMangaList()

    val getAnimeList = profileAnimeListDao.getAnimeList()


    val getFriendList = profileUserFriendListDao.getFriendList()


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

    fun getUserMangaList(username:String, status: String) {
        viewModelScope.launch {
            profileRepo.getUserMangaList(username, status)
        }
    }

    fun getUserFriendList(username:String) {
        viewModelScope.launch {
            profileRepo.getUserFriendList(username)
        }
    }

    fun getUserInfoFromMalApi(fields:String) {
        viewModelScope.launch {
            profileRepo.getUserInfoFromMalApi(fields)
        }
    }

    fun clearAnimeList(){
        profileAnimeListDao.deleteAllAnime()
        profileRepo.nextAnimePage = 1
    }

    fun clearMangaList(){
        profileUserMangaListDao.deleteAllManga()
        profileRepo.nextMangaPage = 1
    }

    fun clearFriendList(){
        profileUserFriendListDao.deleteAllFriends()
        profileRepo.nextFriendPage = 1
    }

}