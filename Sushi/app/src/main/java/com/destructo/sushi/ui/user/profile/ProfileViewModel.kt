package com.destructo.sushi.ui.user.profile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.database.UserInfoEntity
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.userInfo.UserInfo
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserInfoDao
import kotlinx.coroutines.launch

class ProfileViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val profileRepo:ProfileRepository,
    private val userInfoDao: UserInfoDao
): ViewModel(){

    val userInformation: LiveData<Resource<UserInfo>> = profileRepo.userInfo

    fun getUserInfo(fields:String) {
        profileRepo.getUserInfo(fields)
    }

    fun clearUserInfo() {
        userInfoDao.clear()
    }

}