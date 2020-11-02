package com.destructo.sushi.ui.user.mangaList

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.enum.mal.UserAnimeSort
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

    fun getUserMangaList(mangaStatus:String?)
            : MutableLiveData<Resource<UserMangaList>> {
        val result = MutableLiveData<Resource<UserMangaList>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val getUserMangaDeferred = malApi.getUserMangaListAsync(
                "@me", "100",
                mangaStatus, UserMangaSort.MANGA_TITLE.value, ""
                , ALL_MANGA_FIELDS)
            try {
                val userManga = getUserMangaDeferred.await()
                withContext(Dispatchers.Main){
                    result.value = Resource.success(userManga)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    result.value = Resource.error(e.message ?: "", null)}
            }
        }
        return  result
    }

    fun addChapter(mangaId:String,numberOfCh:Int?)
            : MutableLiveData<Resource<UpdateUserManga>> {
        val result = MutableLiveData<Resource<UpdateUserManga>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val addChapterDeferred = malApi.updateUserManga(mangaId,
                null,null,null,null,
                numberOfCh,null,null,
                null,null,null)
            try {
                val mangaStatus = addChapterDeferred.await()
                withContext(Dispatchers.Main){
                    result.value = Resource.success(mangaStatus)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    result.value = Resource.error(e.message ?: "", null)}
            }
        }
        return result
    }
}