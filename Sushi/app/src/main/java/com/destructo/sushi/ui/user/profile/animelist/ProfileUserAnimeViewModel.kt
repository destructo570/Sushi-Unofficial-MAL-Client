package com.destructo.sushi.ui.user.profile.animelist

import androidx.lifecycle.*
import com.destructo.sushi.model.jikan.user.animeList.ProfileUserAnimeList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileAnimeListDao
import com.destructo.sushi.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileUserAnimeViewModel
@Inject
constructor(

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