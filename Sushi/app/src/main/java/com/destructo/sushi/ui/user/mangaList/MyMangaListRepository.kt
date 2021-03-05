package com.destructo.sushi.ui.user.mangaList

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_MANGA_FIELDS
import com.destructo.sushi.DEFAULT_USER_LIST_PAGE_LIMIT
import com.destructo.sushi.model.database.UserMangaEntity
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserMangaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyMangaListRepository
@Inject
constructor(private val malApi: MalApi,
            private val userMangaDao: UserMangaDao
){

    var userMangaList: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

    var userMangaStatus: MutableLiveData<Resource<UpdateUserManga>> = MutableLiveData()

    var nextPage: MutableLiveData<String> = MutableLiveData()

    suspend fun getNextPage() {

        if(!nextPage.value.isNullOrBlank()){
            userMangaList.value = Resource.loading(null)

                val getUserAnimeDeferred = malApi.getUserMangaNextAsync(nextPage.value!!)
                try {
                    val userManga = getUserAnimeDeferred.await()
                    userMangaDao.insertUserMangaList(
                        UserMangaEntity.fromListOfUserMangaData(userManga.data!!))
                    withContext(Dispatchers.Main){
                        setNextPage(userManga)
                        userMangaList.value = Resource.success(userManga)
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Timber.e("Error: %s", e.message)
                        userMangaList.value = Resource.error(e.message ?: "", null)}
                }
            }
    }

    suspend fun getUserMangaList(sortType:String) {

        userMangaList.value = Resource.loading(null)

            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", DEFAULT_USER_LIST_PAGE_LIMIT,
                null, sortType, "", BASIC_MANGA_FIELDS, true)
            try {
                val userManga = getUserMangaDeferred.await()
                userMangaDao.insertUserMangaList(
                    UserMangaEntity.fromListOfUserMangaData(userManga.data!!))
                withContext(Dispatchers.Main){
                    setNextPage(userManga)
                    userMangaList.value = Resource.success(userManga)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Timber.e("Error: %s", e.message)
                    userMangaList.value = Resource.error(e.message ?: "", null)}
            }
    }

    suspend fun addChapter(mangaId:String,numberOfCh:Int?, status: String?){
        userMangaStatus.value = Resource.loading(null)

            val addChapterDeferred = malApi.updateUserManga(mangaId,
                status,null,null,null,
                numberOfCh,null,null,
                null,null,null, null, null)

            try {
                val mangaStatus = addChapterDeferred.await()
                updateCachedUserManga(mangaId, mangaStatus)
                withContext(Dispatchers.Main){
                    userMangaStatus.value = Resource.success(mangaStatus)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    userMangaStatus.value = Resource.error(e.message ?: "", null)
                }
            }
    }

    private fun updateCachedUserManga(
        mangaId: String,
        mangaStatus: UpdateUserManga
    ) {
        val manga = userMangaDao.getUserMangaById(mangaId.toInt())
        manga.updateUserStatus(mangaStatus)
        userMangaDao.deleteUserMangaById(mangaId.toInt())
        userMangaDao.insertUserManga(manga)
    }

    private fun setNextPage(userManga: UserMangaList){
        nextPage.value = if (!userManga.paging?.next.isNullOrEmpty()
            && userManga.paging?.next != nextPage.value
        ) {
            userManga.paging?.next
        } else null
    }

}