package com.destructo.sushi.ui.user.mangaList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.enum.mal.UserAnimeSort
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.enum.mal.UserMangaSort
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class MyMangaListRepository
@Inject
constructor(private val malApi: MalApi){

     var userMangaListAll: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaListReading: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaListCompleted: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaListPlanToRead: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaListOnHold: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaListDropped: MutableLiveData<Resource<UserMangaList>> = MutableLiveData()

     var userMangaStatus: MutableLiveData<Resource<UpdateUserManga>> = MutableLiveData()


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
                getUserMangaList(mangaStatus, userMangaListAll)
            }
        }
    }

    fun getUserMangaList(mangaStatus:String?,
                         mangaList: MutableLiveData<Resource<UserMangaList>>) {

        mangaList.value = Resource.loading(null)

        GlobalScope.launch {
            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", "100",
                mangaStatus, UserMangaSort.MANGA_TITLE.value, ""
                , ALL_MANGA_FIELDS)
            try {
                val userManga = getUserMangaDeferred.await()
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
}