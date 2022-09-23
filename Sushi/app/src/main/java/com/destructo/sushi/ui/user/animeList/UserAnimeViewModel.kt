package com.destructo.sushi.ui.user.animeList

import androidx.lifecycle.*
import com.destructo.sushi.enum.UserAnimeListSort
import com.destructo.sushi.enum.mal.AiringStatus
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.model.database.UserAnimeEntity
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeDetailsDao
import com.destructo.sushi.room.UserAnimeDao
import com.destructo.sushi.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserAnimeViewModel
@Inject
constructor(

    savedStateHandle: SavedStateHandle,
    private val myAnimeListRepo: MyAnimeListRepository,
    private val userAnimeListDao: UserAnimeDao,
    private val animeDetailsDao: AnimeDetailsDao
) : ViewModel() {

    val userAnimeListState: LiveData<Resource<List<UserAnimeEntity>>> = myAnimeListRepo.userAnimeList

    val userAnimeStatus: LiveData<Resource<UpdateUserAnime>> = myAnimeListRepo.userAnimeStatus

    var userAnimeList = userAnimeListDao.getUserAnimeList()

    val userAnimeWatching: LiveData<Resource<List<UserAnimeEntity>>> = myAnimeListRepo.userAnimeWatching

    val userAnimeCompleted: LiveData<Resource<List<UserAnimeEntity>>> = myAnimeListRepo.userAnimeCompleted

    val userAnimeOnHold: LiveData<Resource<List<UserAnimeEntity>>> = myAnimeListRepo.userAnimeOnHold

    val userAnimeDropped: LiveData<Resource<List<UserAnimeEntity>>> = myAnimeListRepo.userAnimeDropped

    val userAnimePlanToWatch: LiveData<Resource<List<UserAnimeEntity>>> = myAnimeListRepo.userAnimePlanToWatch


    var userSortType = MutableLiveData(Event(UserAnimeListSort.BY_TITLE.value))

    val nextPage = myAnimeListRepo.nextPage

    fun addEpisodeAnime(animeId: String, numberOfEp: Int?, status: String?) {
        viewModelScope.launch { myAnimeListRepo.addEpisode(animeId, numberOfEp, status) }
    }

    fun getUserAnimeList(sortType: String) {
        viewModelScope.launch { myAnimeListRepo.getUserAnimeList(sortType) }
    }

    fun getNextPage() {
        viewModelScope.launch { myAnimeListRepo.getNextPage() }
    }

    fun setSortType(sort_by: String) {
        userSortType.value = Event(sort_by)
    }

    fun getAnimeListByStatus(status: String){
        viewModelScope.launch(Dispatchers.IO) {
            myAnimeListRepo.getAnimeListByStatus(status)
        }
    }

    fun getRandomAnime(status: String): Int? {
        val list = userAnimeListDao.getUserAnimeListByStatus(status)?.filter {
            (it.myAnimeListStatus?.status == status)
        }
        when (status){
            UserAnimeStatus.PLAN_TO_WATCH.value ->{
                list?.filter {
                    it.status == AiringStatus.FINISHED_AIRING.value ||
                            it.status == AiringStatus.CURRENTLY_AIRING.value
                }
                if (!list.isNullOrEmpty()) return list.random().malId
            }
            else -> {
                if (!list.isNullOrEmpty()) return list.random().malId
            }
        }
        return null
    }

    fun clearList() {
        viewModelScope.launch { userAnimeListDao.clear() }
    }

    fun clearAnimeDetails(animeId: Int) {
        viewModelScope.launch { animeDetailsDao.deleteAnimeDetailById(animeId) }
    }

}

