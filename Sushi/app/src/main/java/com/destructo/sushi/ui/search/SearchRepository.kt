package com.destructo.sushi.ui.search

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.mal.animeList.AnimeList
import com.destructo.sushi.model.mal.mangaList.MangaList
import com.destructo.sushi.model.mal.userInfo.UserInfo
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class SearchRepository
@Inject
constructor(
    val malApi: MalApi
){

    var animeResult: MutableLiveData<Resource<AnimeList>> = MutableLiveData()

    var mangaResult: MutableLiveData<Resource<MangaList>> = MutableLiveData()

    fun getAnimeResult(query:String, field:String, limit:String, offset:String) {

        animeResult.value = Resource.loading(null)
        GlobalScope.launch {
            val getAnimeResultDeferred = malApi.searchAnimeAsync(query,  limit, offset, field)
            try {
                val animeList = getAnimeResultDeferred.await()
                withContext(Dispatchers.Main){
                    animeResult.value = Resource.success(animeList)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    animeResult.value = Resource.error(e.message ?: "", null)}
            }
        }
    }


    fun getMangaResult(query:String, field:String, limit:String, offset:String) {

        mangaResult.value = Resource.loading(null)

        GlobalScope.launch {
            val getAnimeResultDeferred = malApi.searchMangaAsync(
                query = query,
                limit = limit,
                offset = offset,
                fields = field)
            try {
                val mangaList = getAnimeResultDeferred.await()
                withContext(Dispatchers.Main){
                    mangaResult.value = Resource.success(mangaList)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    mangaResult.value = Resource.error(e.message ?: "", null)}
            }
        }
    }

}