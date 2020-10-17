package com.destructo.sushi.ui.anime.seasonalAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.jikan.season.Season
import com.destructo.sushi.model.jikan.season.SeasonArchive
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.JikanApi
import kotlinx.coroutines.launch

class SeasonalAnimeViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
        val jikanApi:JikanApi
    ): ViewModel() {

    private val _seasonalAnime: MutableLiveData<SeasonalAnime> = MutableLiveData()
    val seasonalAnime: LiveData<SeasonalAnime>
        get() = _seasonalAnime

    private val _seasonArchive: MutableLiveData<SeasonArchive> = MutableLiveData()
    val seasonArchive: LiveData<SeasonArchive>
        get() = _seasonArchive


    fun getSeasonArchive(){
        viewModelScope.launch {
            var getSeasonArchiveDeferred = jikanApi.getSeasonArchiveAsync()
            try {
                val seasonArchiveEntity = getSeasonArchiveDeferred.await()
                _seasonArchive.value = seasonArchiveEntity
            }catch (e:Exception){}
        }
    }

    fun insertSeasonAnime(seasonAnime: SeasonalAnime){
        _seasonalAnime.value = seasonAnime
    }
}