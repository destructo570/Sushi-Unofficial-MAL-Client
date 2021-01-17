package com.destructo.sushi.ui.user.mangaList

import androidx.core.net.toUri
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
                Timber.e("Unknown Status")
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
                    setUserMangaData(userManga)
                    setNextPage(mangaStatus, userManga.paging?.next)
                    userMangaListDao.insertUseMangaList(userManga.data!!)
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


    private fun loadPage(
        mangaStatus: String,
        offset:String?,
        mangaId:Int
    ) {
        if(!offset.isNullOrBlank()){
            GlobalScope.launch {
                val getUserMangaDeferred = malApi.getUserMangaListAsync(
                    "@me", DEFAULT_USER_LIST_PAGE_LIMIT,
                    mangaStatus, UserMangaSort.MANGA_TITLE.value, offset, ALL_MANGA_FIELDS)
                try {
                    val userManga = getUserMangaDeferred.await()
                    val userMangaList = userManga.data
                    setUserMangaData(userManga)
                    userMangaListDao.deleteUserMangaById(mangaId)
                    userMangaListDao.insertUseMangaList(userMangaList!!)

                }catch (e: java.lang.Exception){
                    withContext(Dispatchers.Main){
                    }
                }
            }
        }
    }

    private fun getUserMangaList(mangaStatus:String?,
                                 mangaList: MutableLiveData<Resource<UserMangaList>>) {

        mangaList.value = Resource.loading(null)

        GlobalScope.launch {
            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", DEFAULT_USER_LIST_PAGE_LIMIT,
                mangaStatus, UserMangaSort.MANGA_TITLE.value, "", ALL_MANGA_FIELDS)
            try {
                val userManga = getUserMangaDeferred.await()
                setUserMangaData(userManga)
                setNextPage(mangaStatus, userManga.paging?.next)
                userMangaListDao.insertUseMangaList(userManga.data!!)
                withContext(Dispatchers.Main){
                    mangaList.value = Resource.success(userManga)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    mangaList.value = Resource.error(e.message ?: "", null)}
            }
        }
    }

    fun addChapter(mangaId:String,numberOfCh:Int?, status: String?){
        userMangaStatus.value = Resource.loading(null)

        GlobalScope.launch {
            val addChapterDeferred = malApi.updateUserManga(mangaId,
                status,null,null,null,
                numberOfCh,null,null,
                null,null,null)
            try {
                val mangaStatus = addChapterDeferred.await()
                val manga = userMangaListDao.getUserMangaById(mangaId.toInt())
                mangaStatus.status?.let { loadPage(it, manga.offset, mangaId.toInt()) }
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


    private fun calcOffset(nextPage: String?, prevPage:String?): String{
        var currentOffset = "0"
        if(!nextPage.isNullOrBlank()){
            val nextOffset = getOffset(nextPage)
            if (!nextOffset.isNullOrBlank()){
                val temp = nextOffset.toInt().minus(DEFAULT_USER_LIST_PAGE_LIMIT.toInt())
                if (temp>=0){
                    currentOffset = temp.toString()
                }
            }
            return currentOffset
        }else{
            val prevOffset = getOffset(prevPage)
            if (!prevOffset.isNullOrBlank()){
                val temp = prevOffset.toInt().plus(DEFAULT_USER_LIST_PAGE_LIMIT.toInt())
                if (temp>=0){
                    currentOffset = temp.toString()
                }
            }
            return currentOffset
        }

    }

    private fun getOffset(url: String?): String?{

        return if (!url.isNullOrBlank()){
            val uri = url.toUri()
            uri.getQueryParameter("offset").toString()
        }else{
            null
        }
    }

    private fun setUserMangaData(userAnime: UserMangaList){
        val userMangaList = userAnime.data
        if (userMangaList != null) {
            for (manga in userMangaList){
                manga?.status = manga?.manga?.myMangaListStatus?.status
                manga?.title =  manga?.manga?.title
                manga?.mangaId = manga?.manga?.id
                val next = userAnime.paging?.next
                val prev = userAnime.paging?.previous
                manga?.offset = calcOffset(next, prev)
            }
        }
    }

}