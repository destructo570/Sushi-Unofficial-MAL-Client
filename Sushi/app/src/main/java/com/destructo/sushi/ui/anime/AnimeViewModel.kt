package com.destructo.sushi.ui.anime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.season.Season
import com.destructo.sushi.model.top.TopAnime
import com.destructo.sushi.network.JikanApi
import kotlinx.coroutines.launch
import timber.log.Timber

class AnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val jikanApi: JikanApi,
): ViewModel() {


    private val _topAnimeList: MutableLiveData<TopAnime> = MutableLiveData()
    val topAnimeList: LiveData<TopAnime>
        get() = _topAnimeList

    private val _upcomingAnime:MutableLiveData<TopAnime> = MutableLiveData()
    val upcomingAnime:LiveData<TopAnime>
        get() = _upcomingAnime


    private val _currentlyAiring:MutableLiveData<TopAnime> = MutableLiveData()
    val currentlyAiring:LiveData<TopAnime>
        get() = _currentlyAiring


    private val _seasonalAnime:MutableLiveData<Season> = MutableLiveData()
    val seasonalAnime:LiveData<Season>
        get() = _seasonalAnime

    fun getTopAnime(page:String, subtype:String){
        viewModelScope.launch {
            var getTopAnimeDeferred = jikanApi.getTopAnimeAsync(page, subtype)
            try {
                val topAnimeList = getTopAnimeDeferred.await()
                _topAnimeList.value = topAnimeList
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun getUpcomingAnime(page:String){
        viewModelScope.launch {
            var getUpcomingAnimeDeferred = jikanApi.getUpcomingAnimeAsync(page)
            try {
                val upcomingAnime = getUpcomingAnimeDeferred.await()
                _upcomingAnime.value = upcomingAnime
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




}