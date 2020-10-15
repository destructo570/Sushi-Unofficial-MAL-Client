package com.destructo.sushi.ui.anime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import kotlinx.coroutines.launch
import timber.log.Timber

class AnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val jikanApi: JikanApi,
    private val malApi: MalApi
): ViewModel() {


    private val _topAnimeList: MutableLiveData<AnimeRanking> = MutableLiveData()
    val topAnimeList: LiveData<AnimeRanking>
        get() = _topAnimeList

    private val _upcomingAnime:MutableLiveData<AnimeRanking> = MutableLiveData()
    val upcomingAnime:LiveData<AnimeRanking>
        get() = _upcomingAnime


    private val _currentlyAiring:MutableLiveData<AnimeRanking> = MutableLiveData()
    val currentlyAiring:LiveData<AnimeRanking>
        get() = _currentlyAiring


    private val _seasonalAnime:MutableLiveData<SeasonalAnime> = MutableLiveData()
    val seasonalAnime:LiveData<SeasonalAnime>
        get() = _seasonalAnime


    fun getTopAnime(offset:String?, limit:String?){
        viewModelScope.launch {
            var getTopAnimeDeferred = malApi.getAnimeRankingAsync("all",limit,offset,
                ALL_ANIME_FIELDS)
            try {
                val getAnimeRanking = getTopAnimeDeferred.await()
                _topAnimeList.value = getAnimeRanking
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUpcomingAnime(offset:String?, limit:String?){
        viewModelScope.launch {
            var getUpcomingDeferred = malApi.getAnimeRankingAsync("upcoming",limit,offset,
                ALL_ANIME_FIELDS)
            try {
                val getAnimeRanking = getUpcomingDeferred.await()
                _upcomingAnime.value = getAnimeRanking
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getCurrentlyAiringAnime(offset:String?, limit:String?){
        viewModelScope.launch {
            var getAiringDeferred = malApi.getAnimeRankingAsync("airing",limit,offset,
                ALL_ANIME_FIELDS)
            try {
                val getAnimeRanking = getAiringDeferred.await()
                _currentlyAiring.value = getAnimeRanking
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }


    fun getSeasonalAnime(year:String,season:String,sort:String?,
                         limit:String?,offset:String?){
        viewModelScope.launch {
            var getcurrentlyAiringDeferred = malApi
                .getSeasonalAnimeAsync(year,season,sort,limit,offset, ALL_ANIME_FIELDS)
            try {
                val currentlyAiring = getcurrentlyAiringDeferred.await()
                _seasonalAnime.value = currentlyAiring
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }




}