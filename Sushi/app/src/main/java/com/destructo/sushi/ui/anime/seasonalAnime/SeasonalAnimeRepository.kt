package com.destructo.sushi.ui.anime.seasonalAnime

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.season.SeasonArchive
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.SeasonAnimeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SeasonalAnimeRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val jikanApi: JikanApi,
    private val seasonAnimeDao: SeasonAnimeDao
){

    var seasonalAnime: MutableLiveData<Resource<SeasonalAnime>> = MutableLiveData()
    var seasonArchive: MutableLiveData<Resource<SeasonArchive>> = MutableLiveData()
    var seasonalNextPage: MutableLiveData<Resource<SeasonalAnime>> = MutableLiveData()

    private var nextPage: String? =""

    fun getSeasonArchive(){
        seasonArchive.value = Resource.loading(null)
        GlobalScope.launch {
            val getSeasonArchiveDeferred = jikanApi.getSeasonArchiveAsync()
            try {
                val response = getSeasonArchiveDeferred.await()
                withContext(Dispatchers.Main){
                    seasonArchive.value = Resource.success(response)
                }

            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    seasonArchive.value = Resource.error(e.message ?: "", null)
                }}
        }
    }

    fun getSeasonalAnime(year:String,season:String,sort:String?,
                         limit:String?,offset:String?) {

        seasonalAnime.value = Resource.loading(null)

        GlobalScope.launch {
            seasonalAnimeCall(
                year = year,
                season = season,
                sort = sort,
                limit = limit,
                offset = offset)
        }
    }

    fun getSeasonAnimeNext() {

        if (!nextPage.isNullOrBlank()) {
            seasonalNextPage.value = Resource.loading(null)
            GlobalScope.launch {
                nextPageCall(
                    next = nextPage!!,
                )
            }
        }
    }

    private suspend fun nextPageCall(next: String) {
        try {
            val getSeasonalAnimeDeferred = malApi.getSeasonalAnimeNextAsync(next)
            val response = getSeasonalAnimeDeferred.await()
            nextPage = response.paging?.next
            seasonAnimeDao.insertSeasonAnimeList(response.data!!)

            withContext(Dispatchers.Main) {
                seasonalNextPage.value = Resource.success(response)
            }
        } catch (e: java.lang.Exception) {
            withContext(Dispatchers.Main) {
                seasonalNextPage.value = Resource.error(e.message ?: "", null)
            }
        }

    }

    private suspend fun seasonalAnimeCall(year:String,season:String,sort:String?,
                                          limit:String?,offset:String?){
        val getSeasonalDeferred = malApi
            .getSeasonalAnimeAsync(year, season, sort, limit, offset, ALL_ANIME_FIELDS)
        try {
            val response = getSeasonalDeferred.await()
            nextPage = response.paging?.next
            seasonAnimeDao.insertSeasonAnimeList(response.data!!)

            withContext(Dispatchers.Main) {
                seasonalAnime.value = Resource.success(response)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main){
                seasonalAnime.value = Resource.error(e.message ?: "", null)
            }
        }
    }

}