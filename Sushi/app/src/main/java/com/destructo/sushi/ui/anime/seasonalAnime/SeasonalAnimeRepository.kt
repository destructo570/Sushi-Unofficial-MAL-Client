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
    private val jikanApi: JikanApi
){

    var seasonalAnime: MutableLiveData<Resource<SeasonalAnime>> = MutableLiveData()
    var seasonArchive: MutableLiveData<Resource<SeasonArchive>> = MutableLiveData()

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
            val getSeasonalDeferred = malApi
                .getSeasonalAnimeAsync(year, season, sort, limit, offset, ALL_ANIME_FIELDS)
            try {
                val seasonalAnimeList = getSeasonalDeferred.await()
                withContext(Dispatchers.Main) {
                    seasonalAnime.value = Resource.success(seasonalAnimeList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    seasonalAnime.value = Resource.error(e.message ?: "", null)
                }
            }
        }
    }

}