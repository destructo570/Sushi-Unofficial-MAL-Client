package com.destructo.sushi.ui.user.profile.mangalist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.user.mangaList.ProfileUserMangaList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileMangaListDao
import kotlinx.coroutines.launch

class ProfileUserMangaViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val profileRepo: ProfileUserMangaRepository,

    private val profileUserMangaListDao: ProfileMangaListDao


): ViewModel(){

    val userMangaList: LiveData<Resource<ProfileUserMangaList>> = profileRepo.userMangaList
    val getMangaList = profileUserMangaListDao.getMangaList()

    fun getUserMangaList(username:String, status: String) {
        viewModelScope.launch {
            profileRepo.getUserMangaList(username, status)
        }
    }

    fun clearMangaList(){
        profileUserMangaListDao.deleteAllManga()
        profileRepo.nextMangaPage = 1
    }

}