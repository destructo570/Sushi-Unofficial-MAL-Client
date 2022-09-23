package com.destructo.sushi.ui.user.profile.mangalist


import androidx.lifecycle.*
import com.destructo.sushi.model.jikan.user.mangaList.ProfileUserMangaList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileMangaListDao
import com.destructo.sushi.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileUserMangaViewModel
@Inject
constructor(

    savedStateHandle: SavedStateHandle,
    private val profileRepo: ProfileUserMangaRepository,

    private val profileUserMangaListDao: ProfileMangaListDao


): ViewModel(){

    private val _showErrorToast = MutableLiveData<Event<String>>()

    val  showErrorToast: LiveData<Event<String>>
        get() = _showErrorToast

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

    fun showError(message: String){
        _showErrorToast.value = Event(message)
    }

}