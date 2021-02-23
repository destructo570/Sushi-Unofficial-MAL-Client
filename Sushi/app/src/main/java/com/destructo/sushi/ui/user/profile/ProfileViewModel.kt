package com.destructo.sushi.ui.user.profile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.user.UserInfo
import com.destructo.sushi.model.jikan.user.friends.Friend
import com.destructo.sushi.model.jikan.user.friends.UserFriends
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

    val userFriendList: LiveData<Resource<UserFriends>> = profileRepo.userFriendList

    val userInformationMalApi: LiveData<Resource<com.destructo.sushi.model.mal.userInfo.UserInfo>> = profileRepo.userInfoMalApi

    fun getUserInfo(fields:String) {
        viewModelScope.launch {
            profileRepo.getUserInfo(fields)
        }
    }

    fun getUserFriendList(username:String) {
        viewModelScope.launch {
            profileRepo.getUserFriendList(username)
        }
    }

    fun getUserFriendListByUsername(username:String): LiveData<List<Friend>> {
        return profileUserFriendListDao.getFriendListByUsername(username)
    }

    fun getUserInfoFromMalApi(fields:String) {
        viewModelScope.launch {
            profileRepo.getUserInfoFromMalApi(fields)
        }
    }


    fun clearFriendList(username: String){
        profileUserFriendListDao.deleteAllFriendsByUsername(username)
    }

    fun resetFriendPage(){
        profileRepo.nextFriendPage = 1
    }

}