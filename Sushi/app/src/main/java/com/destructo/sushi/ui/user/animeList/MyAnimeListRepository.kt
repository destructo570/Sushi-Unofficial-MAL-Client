package com.destructo.sushi.ui.user.animeList

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.DEFAULT_USER_LIST_PAGE_LIMIT
import com.destructo.sushi.enum.UserAnimeListSort
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.model.database.UserAnimeEntity
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserAnimeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyAnimeListRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val userAnimeListDao: UserAnimeDao
    ){

    var animeSortType: String = UserAnimeListSort.BY_TITLE.value

    //var userAnimeList: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeStatus: MutableLiveData<Resource<UpdateUserAnime>> = MutableLiveData()

    var userAnimeListWatching: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListCompleted: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListPlanToWatch: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListOnHold: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListDropped: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListWatchingNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListCompletedNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListPlanToWatchNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListOnHoldNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListDroppedNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var watchingNextPage: String? = null
    var completedNextPage: String? = null
    var ptwNextPage: String? = null
    var onHoldNextPage: String? = null
    var droppedNextPage: String? = null

    fun getUserAnime(animeStatus:String?){
        when(animeStatus){
            UserAnimeStatus.COMPLETED.value ->{
                getUserAnimeList(animeStatus, userAnimeListCompleted)
            }
            UserAnimeStatus.WATCHING.value ->{
                getUserAnimeList(animeStatus, userAnimeListWatching)
            }
            UserAnimeStatus.ON_HOLD.value ->{
                getUserAnimeList(animeStatus, userAnimeListOnHold)
            }
            UserAnimeStatus.PLAN_TO_WATCH.value ->{
                getUserAnimeList(animeStatus, userAnimeListPlanToWatch)
            }
            UserAnimeStatus.DROPPED.value ->{
                getUserAnimeList(animeStatus, userAnimeListDropped)
            }
            else ->{
                Timber.d("Unknown Status")
            }
        }
    }


    fun getUserAnimeNext(animeStatus:String?){
        when(animeStatus){
            UserAnimeStatus.COMPLETED.value ->{
                getNextPage(animeStatus, userAnimeListCompletedNext, completedNextPage)
            }
            UserAnimeStatus.WATCHING.value ->{
                getNextPage(animeStatus, userAnimeListWatchingNext, watchingNextPage)
            }
            UserAnimeStatus.ON_HOLD.value ->{
                getNextPage(animeStatus, userAnimeListOnHoldNext, onHoldNextPage)
            }
            UserAnimeStatus.PLAN_TO_WATCH.value ->{
                getNextPage(animeStatus, userAnimeListPlanToWatchNext, ptwNextPage)
            }
            UserAnimeStatus.DROPPED.value ->{
                getNextPage(animeStatus, userAnimeListDroppedNext, droppedNextPage)
            }
            else ->{
                Timber.e("Unknown Status")
            }

        }
    }

    private fun getNextPage(
        animeStatus:String?,
        animeList:MutableLiveData<Resource<UserAnimeList>>,
        nextPage:String?
    ) {

        if(!nextPage.isNullOrBlank()){
            animeList.value = Resource.loading(null)

            GlobalScope.launch {
                val getUserAnimeDeferred = malApi.getUserAnimeNextAsync(nextPage)
                try {
                    val userAnime = getUserAnimeDeferred.await()
                    setNextPage(animeStatus, userAnime.paging?.next)
                    userAnimeListDao.insertUserAnimeList(UserAnimeEntity.fromListOfUpdateUserAnime(userAnime.data!!))
                    withContext(Dispatchers.Main){
                        animeList.value = Resource.success(userAnime)
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        animeList.value = Resource.error(e.message ?: "", null)}
                }
            }
        }
    }


    fun getUserAnimeList(animeStatus:String?,
                         animeList: MutableLiveData<Resource<UserAnimeList>>) {
        animeList.value = Resource.loading(null)

        GlobalScope.launch {
            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                "@me", DEFAULT_USER_LIST_PAGE_LIMIT,
                animeStatus, animeSortType, "", BASIC_ANIME_FIELDS, true)
            try {
                val userAnime = getUserAnimeDeferred.await()
                setNextPage(animeStatus, userAnime.paging?.next)
                userAnimeListDao.insertUserAnimeList(UserAnimeEntity.fromListOfUpdateUserAnime(userAnime.data!!))
                withContext(Dispatchers.Main){
                    animeList.value = Resource.success(userAnime)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    Timber.e("Error : ${e.message}")
                    animeList.value = Resource.error(e.message ?: "", null)}
            }
        }
    }

    fun addEpisode(animeId:String,numberOfEp:Int?,status: String?) {
        userAnimeStatus.value = Resource.loading(null)

        GlobalScope.launch {
            val addEpisodeDeferred = malApi.updateUserAnime(animeId,
                status,null,null,numberOfEp,
                null,null,null,null,null,
                null,null)
            try {
                val animeStatus = addEpisodeDeferred.await()
                updateCachedUserAnime(animeId, animeStatus)
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.success(animeStatus)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    Timber.e("Error: ${e.message}")
                    userAnimeStatus.value = Resource.error(e.message ?: "", null)
                }
            }
        }
    }

    private fun updateCachedUserAnime(
        animeId: String,
        animeStatus: UpdateUserAnime
    ) {
        val anime = userAnimeListDao.getUserAnimeById(animeId.toInt())
        anime.updateUserStatus(animeStatus)
        userAnimeListDao.deleteUserAnimeById(animeId.toInt())
        userAnimeListDao.insertUserAnime(anime)
    }

    private fun setNextPage(AnimeStatus:String?, nextPage: String?){

        when(AnimeStatus){
            UserAnimeStatus.COMPLETED.value ->{
                completedNextPage = nextPage
            }
            UserAnimeStatus.WATCHING.value ->{
                watchingNextPage = nextPage
            }
            UserAnimeStatus.ON_HOLD.value ->{
                onHoldNextPage = nextPage
            }
            UserAnimeStatus.PLAN_TO_WATCH.value ->{
                ptwNextPage = nextPage
            }
            UserAnimeStatus.DROPPED.value ->{
                droppedNextPage = nextPage
            }
            else ->{
                Timber.e("Can't set next page: UnKnown status")
            }

        }
    }


}