package com.destructo.sushi.ui.user.animeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.DEFAULT_USER_LIST_PAGE_LIMIT
import com.destructo.sushi.enum.mal.UserAnimeSort
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserAnimeListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyAnimeListRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val userAnimeListDao: UserAnimeListDao){

    var userAnimeListAll: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListWatching: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListCompleted: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListPlanToWatch: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListOnHold: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListDropped: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeStatus: MutableLiveData<Resource<UpdateUserAnime>> = MutableLiveData()

    var userAnimeListAllNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListWatchingNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListCompletedNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListPlanToWatchNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListOnHoldNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeListDroppedNext: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    private var allNextPage: String? = null
    private var watchingNextPage: String? = null
    private var completedNextPage: String? = null
    private var ptwNextPage: String? = null
    private var onHoldNextPage: String? = null
    private var droppedNextPage: String? = null

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
                 getUserAnimeList(animeStatus, userAnimeListAll)
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
                getNextPage(animeStatus, userAnimeListAllNext, allNextPage)
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
                val userAnimeList = userAnime.data
                if (userAnimeList != null){
                    for (anime in userAnimeList){
                        anime?.status = anime?.anime?.myAnimeListStatus?.status
                        anime?.title =  anime?.anime?.title
                    }
                }
                userAnimeListDao.insertUseAnimeList(userAnimeList!!)
                setNextPage(animeStatus, userAnime.paging?.next)
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

    private fun getUserAnimeList(
        animeStatus:String?,
        animeList:MutableLiveData<Resource<UserAnimeList>>
    ) {
        animeList.value = Resource.loading(null)

        GlobalScope.launch {
            val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                "@me", DEFAULT_USER_LIST_PAGE_LIMIT,
                animeStatus, UserAnimeSort.ANIME_TITLE.value, "",ALL_ANIME_FIELDS)
            try {
                val userAnime = getUserAnimeDeferred.await()
                val userAnimeList = userAnime.data
                if (userAnimeList != null){
                    for (anime in userAnimeList){
                        anime?.status = anime?.anime?.myAnimeListStatus?.status
                        anime?.title =  anime?.anime?.title
                    }
                }

                userAnimeListDao.insertUseAnimeList(userAnimeList!!)
                setNextPage(animeStatus, userAnime.paging?.next)
                withContext(Dispatchers.Main){
                    animeList.value = Resource.success(userAnime)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    animeList.value = Resource.error(e.message ?: "", null)}
            }
        }
    }

    fun addEpisode(animeId:String,numberOfEp:Int?) {
        userAnimeStatus.value = Resource.loading(null)

        GlobalScope.launch {
            val addEpisodeDeferred = malApi.updateUserAnime(animeId,
                null,null,null,numberOfEp,
                null,null,null,null,null)
            try {
                val animeStatus = addEpisodeDeferred.await()
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.success(animeStatus)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.error(e.message ?: "", null)}
            }
        }
    }

    private fun setNextPage(animeStatus:String?, nextPage: String?){

         when(animeStatus){
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
                    Timber.e("Can't set next page: Uknown status")
                }

            }
    }

}