package com.destructo.sushi.ui.anime.seasonalAnime

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.season.SeasonArchive
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.SeasonAnimeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
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
                val seasonArchiveEntity = getSeasonArchiveDeferred.await()
                withContext(Dispatchers.Main){
                    seasonArchive.value = Resource.success(seasonArchiveEntity)
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
            val getSeasonalAnimeDeferred = malApi.getSeasonalAnimeNext(next)
            val response = getSeasonalAnimeDeferred.await()
            val animeList = response.data
            nextPage = response.paging?.next
            seasonAnimeDao.insertSeasonAnimeList(animeList!!)

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
            val seasonalAnimeResponse = getSeasonalDeferred.await()
            val seasonAnimeList = seasonalAnimeResponse.data
            nextPage = seasonalAnimeResponse.paging?.next
            seasonAnimeDao.insertSeasonAnimeList(seasonAnimeList!!)

            withContext(Dispatchers.Main) {
                seasonalAnime.value = Resource.success(seasonalAnimeResponse)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main){
                seasonalAnime.value = Resource.error(e.message ?: "", null)
            }
        }
    }

}