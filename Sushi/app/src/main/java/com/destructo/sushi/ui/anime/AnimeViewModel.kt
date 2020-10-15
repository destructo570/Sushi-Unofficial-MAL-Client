package com.destructo.sushi.ui.anime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.season.Season
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
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


    private val _currentlyAiring:MutableLiveData<TopAnime> = MutableLiveData()
    val currentlyAiring:LiveData<TopAnime>
        get() = _currentlyAiring


    private val _seasonalAnime:MutableLiveData<Season> = MutableLiveData()
    val seasonalAnime:LiveData<Season>
        get() = _seasonalAnime

    private val _animeRanking:MutableLiveData<AnimeRanking> = MutableLiveData()
    val animeRanking:LiveData<AnimeRanking>
        get() = _animeRanking


    fun getTopAnime(offset:String?, limit:String?){
        viewModelScope.launch {
            var getTopAnimeDeferred = malApi.getAnimeRanking("all",limit,offset,
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
            var getUpcomingDeferred = malApi.getAnimeRanking("upcoming",limit,offset,
                ALL_ANIME_FIELDS)
            try {
                val getAnimeRanking = getUpcomingDeferred.await()
                _upcomingAnime.value = getAnimeRanking
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getCurrentlyAiringAnime(page:String){
        viewModelScope.launch {
            var getcurrentlyAiringDeferred = jikanApi.getCurrentlyAiringsync(page)
            try {
                val currentlyAiring = getcurrentlyAiringDeferred.await()
                _currentlyAiring.value = currentlyAiring
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }


    fun getSeasonalAnime(year:String,season:String){
        viewModelScope.launch {
            var getcurrentlyAiringDeferred = jikanApi.getSeasonalAnimeAsync(year,season)
            try {
                val currentlyAiring = getcurrentlyAiringDeferred.await()
                _seasonalAnime.value = currentlyAiring
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getMalAnimeRanking(){
        viewModelScope.launch {
//            var getcurrentlyAiringDeferred = malApi.getAnimeRanking("upcoming","500",null,
//                ALL_ANIME_FIELDS)
//            try {
//                val getAnimeRanking = getcurrentlyAiringDeferred.await()
//                _animeRanking.value = getAnimeRanking
//            }catch (e:Exception){
//                Timber.e("Error: %s", e.message)
//            }
        }
    }




}