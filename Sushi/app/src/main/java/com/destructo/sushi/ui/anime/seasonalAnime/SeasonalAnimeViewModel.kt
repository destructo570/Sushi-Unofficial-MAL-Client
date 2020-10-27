package com.destructo.sushi.ui.anime.seasonalAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.season.Season
import com.destructo.sushi.model.jikan.season.SeasonArchive
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import kotlinx.coroutines.launch
import timber.log.Timber

class SeasonalAnimeViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
        val jikanApi:JikanApi,
        val malApi: MalApi
    ): ViewModel() {

    private val _seasonalAnime: MutableLiveData<SeasonalAnime> = MutableLiveData()
    val seasonalAnime: LiveData<SeasonalAnime>
        get() = _seasonalAnime

    private val _seasonArchive: MutableLiveData<SeasonArchive> = MutableLiveData()
    val seasonArchive: LiveData<SeasonArchive>
        get() = _seasonArchive


    fun getSeasonArchive(){
        viewModelScope.launch {
            val getSeasonArchiveDeferred = jikanApi.getSeasonArchiveAsync()
            try {
                val seasonArchiveEntity = getSeasonArchiveDeferred.await()
                _seasonArchive.value = seasonArchiveEntity
            }catch (e:Exception){}
        }
    }


    fun getSeasonalAnime(year:String,season:String,sort:String?,
                         limit:String?,offset:String?){
        viewModelScope.launch {
            val getSeasonalDeferred = malApi
                .getSeasonalAnimeAsync(year,season,sort,limit,offset, ALL_ANIME_FIELDS)
            try {
                val seasonalAnime = getSeasonalDeferred.await()
                _seasonalAnime.value = seasonalAnime
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }

}