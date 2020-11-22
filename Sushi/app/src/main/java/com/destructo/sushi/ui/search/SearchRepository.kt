package com.destructo.sushi.ui.search

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.mal.animeList.AnimeList
import com.destructo.sushi.model.mal.mangaList.MangaList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.SearchAnimeDao
import com.destructo.sushi.room.SearchMangaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class SearchRepository
@Inject
constructor(
    val malApi: MalApi,
    private val searchAnimeDao: SearchAnimeDao,
    private val searchMangaDao: SearchMangaDao

){

    var animeResult: MutableLiveData<Resource<AnimeList>> = MutableLiveData()

    var mangaResult: MutableLiveData<Resource<MangaList>> = MutableLiveData()

    var animeResultNext: MutableLiveData<Resource<AnimeList>> = MutableLiveData()

    var mangaResultNext: MutableLiveData<Resource<MangaList>> = MutableLiveData()

    private var animeNextPage: String? = null
    private var mangaNextPage: String? = null


    fun getAnimeResult(query:String, field:String, limit:String, offset:String) {

        animeResult.value = Resource.loading(null)
        GlobalScope.launch {
            val getAnimeResultDeferred = malApi.searchAnimeAsync(
                query = query,
                limit = limit,
                offset = offset,
                fields = field,
                nsfw ="true")
            try {
                val response = getAnimeResultDeferred.await()
                val animeList = response.data
                searchAnimeDao.insertAnimeList(animeList!!)
                animeNextPage = response.paging?.next

                withContext(Dispatchers.Main){
                    animeResult.value = Resource.success(response)
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
                fields = field,
                nsfw ="true")
            try {
                val response = getAnimeResultDeferred.await()
                val mangaList = response.data
                searchMangaDao.insertMangaList(mangaList!!)
                mangaNextPage = response.paging?.next

                withContext(Dispatchers.Main){
                    mangaResult.value = Resource.success(response)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    mangaResult.value = Resource.error(e.message ?: "", null)}
            }
        }
    }



    fun getAnimeNext() {

        if (!animeNextPage.isNullOrBlank()) {
            animeResultNext.value = Resource.loading(null)
            GlobalScope.launch {
                animeNextPageCall(
                    next = animeNextPage!!,
                )
            }
        }
    }

    private suspend fun animeNextPageCall(next: String) {
        try {
            val getTopAnimeDeferred = malApi.getSearchAnimeNextAsync(next)
            val animeRanking = getTopAnimeDeferred.await()
            val animeList = animeRanking.data
            animeNextPage = animeRanking.paging?.next
            searchAnimeDao.insertAnimeList(animeList!!)

            withContext(Dispatchers.Main) {
                animeResultNext.value = Resource.success(animeRanking)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                animeResultNext.value = Resource.error(e.message ?: "", null)
            }
        }

    }

    fun getMangaNext() {

        if (!mangaNextPage.isNullOrBlank()) {
            animeResultNext.value = Resource.loading(null)
            GlobalScope.launch {
                mangaNextPageCall(
                    next = mangaNextPage!!,
                )
            }
        }
    }

    private suspend fun mangaNextPageCall(next: String) {
        try {
            val getMangaSearchDefferred = malApi.getSearchMangaNextAsync(next)
            val response = getMangaSearchDefferred.await()
            val mangaList = response.data
            mangaNextPage = response.paging?.next
            searchMangaDao.insertMangaList(mangaList!!)

            withContext(Dispatchers.Main) {
                mangaResultNext.value = Resource.success(response)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mangaResultNext.value = Resource.error(e.message ?: "", null)
            }
        }

    }



}