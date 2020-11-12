package com.destructo.sushi.ui.user.profile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.userInfo.UserInfo
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.Resource

class ProfileViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val profileRepo:ProfileRepository
): ViewModel(){

    val userInformation: LiveData<Resource<UserInfo>> = profileRepo.userInfo

    fun getUserInfo(fields:String) {
        profileRepo.getUserInfo(fields)
    }

}