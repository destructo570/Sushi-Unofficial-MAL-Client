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


    fun getAnimeResult(query:String, field:String, limit:String, offset:String, nsfw: Boolean) {

        animeResult.value = Resource.loading(null)
        GlobalScope.launch {
            val getAnimeResultDeferred = malApi.searchAnimeAsync(
                query = query,
                limit = null,
                offset = null,
                fields = field,
                nsfw = nsfw)
            try {
                val response = getAnimeResultDeferred.await()
                if (nsfw) {
                    val animeList = response.data
                    searchAnimeDao.insertAnimeList(animeList!!)
                }else{
                    val animeList = response.data?.filter { it?.anime?.nsfw == "white" }
                    searchAnimeDao.insertAnimeList(animeList!!)
                }
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



    fun getMangaResult(query:String, field:String, limit:String, offset:String, nsfw: Boolean) {

        mangaResult.value = Resource.loading(null)

        GlobalScope.launch {
            val getAnimeResultDeferred = malApi.searchMangaAsync(
                query = query,
                limit = limit,
                offset = null,
                fields = field,
                nsfw = nsfw)
            try {
                val response = getAnimeResultDeferred.await()
                if (nsfw) {
                    val mangaList = response.data
                    searchMangaDao.insertMangaList(mangaList!!)
                }else{
                    val mangaList = response.data?.filter { it?.manga?.nsfw == "white" }
                    searchMangaDao.insertMangaList(mangaList!!)
                }
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



    fun getAnimeNext(nsfw: Boolean) {

        if (!animeNextPage.isNullOrBlank()) {
            animeResultNext.value = Resource.loading(null)
            GlobalScope.launch {
                animeNextPageCall(
                    next = animeNextPage!!,
                    nsfw = nsfw
                )
            }
        }
    }

    private suspend fun animeNextPageCall(next: String, nsfw: Boolean) {
        try {
            val getTopAnimeDeferred = malApi.getSearchAnimeNextAsync(next, nsfw)
            val animeRanking = getTopAnimeDeferred.await()
            if (nsfw) {
                val animeList = animeRanking.data
                searchAnimeDao.insertAnimeList(animeList!!)
            }else{
                val animeList = animeRanking.data?.filter { it?.anime?.nsfw == "white" }
                searchAnimeDao.insertAnimeList(animeList!!)
            }
            animeNextPage = animeRanking.paging?.next
            withContext(Dispatchers.Main) {
                animeResultNext.value = Resource.success(animeRanking)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                animeResultNext.value = Resource.error(e.message ?: "", null)
            }
        }

    }

    fun getMangaNext(nsfw: Boolean) {

        if (!mangaNextPage.isNullOrBlank()) {
            animeResultNext.value = Resource.loading(null)
            GlobalScope.launch {
                mangaNextPageCall(
                    next = mangaNextPage!!,
                    nsfw = nsfw
                )
            }
        }
    }

    private suspend fun mangaNextPageCall(next: String, nsfw: Boolean) {
        try {
            val getMangaSearchDefferred = malApi.getSearchMangaNextAsync(next, nsfw)
            val response = getMangaSearchDefferred.await()
            if (nsfw) {
                val mangaList = response.data
                searchMangaDao.insertMangaList(mangaList!!)
            }else{
                val mangaList = response.data?.filter { it?.manga?.nsfw == "white" }
                searchMangaDao.insertMangaList(mangaList!!)
            }
            mangaNextPage = response.paging?.next
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