package com.destructo.sushi.ui.user.profile.animelist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.jikan.user.animeList.ProfileUserAnimeList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileAnimeListDao
import com.destructo.sushi.util.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileUserAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val profileRepo: ProfileUserAnimeRepository,
    private val profileAnimeListDao: ProfileAnimeListDao
): ViewModel() {

    private val _showErrorToast = MutableLiveData<Event<String>>()

    val  showErrorToast: LiveData<Event<String>>
    get() = _showErrorToast

    val userAnimeList: LiveData<Resource<ProfileUserAnimeList>> = profileRepo.userAnimeList

    val getAnimeList = profileAnimeListDao.getAnimeList()

    fun getUserAnimeList(username:String, status: String) {
        viewModelScope.launch {
            Timber.e("Called")
            profileRepo.getUserAnimeList(username, status)
        }
    }

    fun clearAnimeList(){
        profileAnimeListDao.deleteAllAnime()
        profileRepo.nextAnimePage = 1
    }

    fun showError(message: String){
        _showErrorToast.value = Event(message)
    }
}