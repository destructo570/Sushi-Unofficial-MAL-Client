package com.destructo.sushi.ui.user.mangaList

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_MANGA_FIELDS
import com.destructo.sushi.DEFAULT_USER_LIST_PAGE_LIMIT
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.model.database.UserMangaEntity
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserMangaDao
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyMangaListRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val userMangaDao: UserMangaDao
) {

    var userMangaList: MutableLiveData<Resource<List<UserMangaEntity>>> = MutableLiveData()

    var userMangaStatus: MutableLiveData<Resource<UpdateUserManga>> = MutableLiveData()

    val userMangaReading: MutableLiveData<Resource<List<UserMangaEntity>>> = MutableLiveData()

    val userMangaCompleted: MutableLiveData<Resource<List<UserMangaEntity>>> = MutableLiveData()

    val userMangaOnHold: MutableLiveData<Resource<List<UserMangaEntity>>> = MutableLiveData()

    val userMangaDropped: MutableLiveData<Resource<List<UserMangaEntity>>> = MutableLiveData()

    val userMangaPlanToRead: MutableLiveData<Resource<List<UserMangaEntity>>> = MutableLiveData()

    var nextPage: MutableLiveData<String> = MutableLiveData()

    suspend fun getNextPage() {

        if (!nextPage.value.isNullOrBlank()) {
            userMangaList.postValue(Resource.loading(null))

            try {
                val userManga = malApi.getUserMangaNextAsync(nextPage.value!!)
                val listOfUserManga = UserMangaEntity.fromListOfUserMangaData(userManga.data!!)
                userMangaDao.insertUserMangaList(listOfUserManga)
                setNextPage(userManga)
                userMangaList.postValue(Resource.success(listOfUserManga))
            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
                userMangaList.postValue(Resource.error(e.message ?: "", null))
            }
        }
    }

    suspend fun getUserMangaList(sortType: String) {
        userMangaList.postValue(Resource.loading(null))
        val cache = userMangaDao.getAllUserManga().firstOrNull()
        if (cache != null && !cache.isCacheExpired()) {
            userMangaList.postValue(Resource.success(userMangaDao.getUserMangaList().value))
        } else getUserMangaListCall(sortType)

    }

    private suspend fun getUserMangaListCall(sortType: String){
        try {
            val response =  malApi.getUserMangaListAsync(
                "@me", DEFAULT_USER_LIST_PAGE_LIMIT,
                null, sortType, "", BASIC_MANGA_FIELDS, true
            )
            val listOfUserManga = UserMangaEntity.fromListOfUserMangaData(response.data!!)
            userMangaDao.clear()
            userMangaDao.insertUserMangaList(listOfUserManga)
            setNextPage(response)
            userMangaList.postValue(Resource.success(listOfUserManga))
        } catch (e: Exception) {
            Timber.e("Error: %s", e.message)
            userMangaList.postValue(Resource.error(e.message ?: "", null))
        }
    }

    suspend fun addChapter(mangaId: String, numberOfCh: Int?, status: String?) {
        userMangaStatus.postValue(Resource.loading(null))

        val addChapterDeferred = malApi.updateUserManga(
            mangaId,
            status, null, null, null,
            numberOfCh, null, null,
            null, null, null, null, null
        )

        try {
            val mangaStatus = addChapterDeferred.await()
            updateCachedUserManga(mangaId, mangaStatus)
            userMangaStatus.postValue(Resource.success(mangaStatus))
        } catch (e: Exception) {
            userMangaStatus.postValue(Resource.error(e.message ?: "", null))
        }
    }


    suspend fun getMangaListByStatus(status: String){
        when(status){
            UserMangaStatus.READING.value -> {
                getFilteredUserMangaList(status, userMangaReading)
            }
            UserMangaStatus.COMPLETED.value -> {
                getFilteredUserMangaList(status, userMangaCompleted)
            }
            UserMangaStatus.PLAN_TO_READ.value -> {
                getFilteredUserMangaList(status, userMangaPlanToRead)
            }
            UserMangaStatus.ON_HOLD.value -> {
                getFilteredUserMangaList(status, userMangaOnHold)
            }
            UserMangaStatus.DROPPED.value -> {
                getFilteredUserMangaList(status, userMangaDropped)
            }
        }
    }

    private suspend fun getFilteredUserMangaList(status: String, userList: MutableLiveData<Resource<List<UserMangaEntity>>>){

        val listOfAnime = userMangaDao.getUserMangaListByStatus(status)

        userList.postValue(Resource.success(listOfAnime))

    }

    private suspend fun updateCachedUserManga(
        mangaId: String,
        mangaStatus: UpdateUserManga
    ) {
        val manga = userMangaDao.getUserMangaById(mangaId.toInt())
        manga.updateUserStatus(mangaStatus)
        userMangaDao.deleteUserMangaById(mangaId.toInt())
        userMangaDao.insertUserManga(manga)
    }

    private fun setNextPage(userManga: UserMangaList) {
        if (!userManga.paging?.next.isNullOrEmpty()
            && userManga.paging?.next != nextPage.value
        ) {
           nextPage.postValue(userManga.paging?.next)
        } else nextPage.postValue(null)
    }

}