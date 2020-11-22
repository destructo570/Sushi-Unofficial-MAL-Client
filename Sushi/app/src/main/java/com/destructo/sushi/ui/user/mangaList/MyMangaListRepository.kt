package com.destructo.sushi.ui.user.mangaList

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.DEFAULT_USER_LIST_PAGE_LIMIT
import com.destructo.sushi.enum.mal.UserMangaSort
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserMangaListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyMangaListRepository
@Inject
constructor(private val malApi: MalApi,
            private val userMangaListDao: UserMangaListDao
){

     var userMangaListAll: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaListReading: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaListCompleted: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaListPlanToRead: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaListOnHold: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaListDropped: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaStatus: MutableLiveData<Resource<UpdateUserManga>> = MutableLiveData()

    var userMangaListReadingNext: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

    var userMangaListCompletedNext: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

    var userMangaListPlanToReadNext: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

    var userMangaListOnHoldNext: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

    var userMangaListDroppedNext: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

    private var readingNextPage: String? = null
    private var completedNextPage: String? = null
    private var ptrNextPage: String? = null
    private var onHoldNextPage: String? = null
    private var droppedNextPage: String? = null

    fun getUserManga(mangaStatus:String?){
        when(mangaStatus){
            UserMangaStatus.COMPLETED.value ->{
                getUserMangaList(mangaStatus, userMangaListCompleted)
            }
            UserMangaStatus.READING.value ->{
                getUserMangaList(mangaStatus, userMangaListReading)
            }
            UserMangaStatus.ON_HOLD.value ->{
                getUserMangaList(mangaStatus, userMangaListOnHold)
            }
            UserMangaStatus.PLAN_TO_READ.value ->{
                getUserMangaList(mangaStatus, userMangaListPlanToRead)
            }
            UserMangaStatus.DROPPED.value ->{
                getUserMangaList(mangaStatus, userMangaListDropped)
            }
            else ->{
                Timber.d("Unknown Status")
            }
        }
    }


    fun getUserMangaNext(mangaStatus:String?){
        when(mangaStatus){
            UserMangaStatus.COMPLETED.value ->{
                getNextPage(mangaStatus, userMangaListCompletedNext, completedNextPage)
            }
            UserMangaStatus.READING.value ->{
                getNextPage(mangaStatus, userMangaListReadingNext, readingNextPage)
            }
            UserMangaStatus.ON_HOLD.value ->{
                getNextPage(mangaStatus, userMangaListOnHoldNext, onHoldNextPage)
            }
            UserMangaStatus.PLAN_TO_READ.value ->{
                getNextPage(mangaStatus, userMangaListPlanToReadNext, ptrNextPage)
            }
            UserMangaStatus.DROPPED.value ->{
                getNextPage(mangaStatus, userMangaListDroppedNext, droppedNextPage)
            }
            else ->{
                Timber.d("Unknown Status")
            }

        }
    }


    private fun getNextPage(
        mangaStatus:String?,
        mangaList:MutableLiveData<Resource<UserMangaList>>,
        nextPage:String?
    ) {

        if(!nextPage.isNullOrBlank()){
            mangaList.value = Resource.loading(null)

            GlobalScope.launch {
                val getUserAnimeDeferred = malApi.getUserMangaNextAsync(nextPage)
                try {
                    val userManga = getUserAnimeDeferred.await()
                    val userMangaList = userManga.data
                    if (userMangaList != null){
                        for (manga in userMangaList){
                            manga?.status = manga?.manga?.myMangaListStatus?.status
                            manga?.title =  manga?.manga?.title
                        }
                    }
                    if (mangaStatus == UserMangaStatus.READING.value){
                        Timber.e("Nexxt Link: ${userManga.paging?.next}")
                    }
                    userMangaListDao.insertUseMangaList(userMangaList!!)
                    setNextPage(mangaStatus, userManga.paging?.next)
                    withContext(Dispatchers.Main){
                        mangaList.value = Resource.success(userManga)
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        mangaList.value = Resource.error(e.message ?: "", null)}
                }
            }
        }
    }

    fun getUserMangaList(mangaStatus:String?,
                         mangaList: MutableLiveData<Resource<UserMangaList>>) {

        mangaList.value = Resource.loading(null)

        GlobalScope.launch {
            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", "10",
                mangaStatus, UserMangaSort.MANGA_TITLE.value, "", ALL_MANGA_FIELDS)
            try {
                val userManga = getUserMangaDeferred.await()
                Timber.e("object: ${userManga}")

                val userMangaList = userManga.data
                if (userMangaList != null){
                    for (manga in userMangaList){
                        manga?.status = manga?.manga?.myMangaListStatus?.status
                        manga?.title =  manga?.manga?.title
                    }
                }
                if (mangaStatus == UserMangaStatus.READING.value){
                    Timber.e("Nexxt Link: ${userManga.paging?.next}")
                    //Timber.e("object: ${userManga.toString()}")

                }
                userMangaListDao.insertUseMangaList(userMangaList!!)
                setNextPage(mangaStatus, userManga.paging?.next)
                withContext(Dispatchers.Main){
                    mangaList.value = Resource.success(userManga)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    mangaList.value = Resource.error(e.message ?: "", null)}
            }
        }
    }

    fun addChapter(mangaId:String,numberOfCh:Int?){
        userMangaStatus.value = Resource.loading(null)

        GlobalScope.launch {
            val addChapterDeferred = malApi.updateUserManga(mangaId,
                null,null,null,null,
                numberOfCh,null,null,
                null,null,null)
            try {
                val mangaStatus = addChapterDeferred.await()
                withContext(Dispatchers.Main){
                    userMangaStatus.value = Resource.success(mangaStatus)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    userMangaStatus.value = Resource.error(e.message ?: "", null)}
            }
        }
    }


    private fun setNextPage(mangaStatus:String?, nextPage: String?){

        when(mangaStatus){
            UserMangaStatus.COMPLETED.value ->{
                completedNextPage = nextPage
            }
            UserMangaStatus.READING.value ->{
                readingNextPage = nextPage
            }
            UserMangaStatus.ON_HOLD.value ->{
                onHoldNextPage = nextPage
            }
            UserMangaStatus.PLAN_TO_READ.value ->{
                ptrNextPage = nextPage
            }
            UserMangaStatus.DROPPED.value ->{
                droppedNextPage = nextPage
            }
            else ->{
                Timber.e("Can't set next page: Uknown status")
            }

        }
    }
}