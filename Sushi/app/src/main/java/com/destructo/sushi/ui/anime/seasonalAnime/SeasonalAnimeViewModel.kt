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
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

class SeasonalAnimeViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
        private val seasonalAnimeRepo:SeasonalAnimeRepository
    ): ViewModel() {

    val seasonalAnime: LiveData<Resource<SeasonalAnime>> = seasonalAnimeRepo.seasonalAnime

    val seasonArchive: LiveData<Resource<SeasonArchive>> = seasonalAnimeRepo.seasonArchive


    fun getSeasonArchive(){
        seasonalAnimeRepo.getSeasonArchive()
    }


    fun getSeasonalAnime(year:String,season:String,sort:String?,
                         limit:String?,offset:String?){
        seasonalAnimeRepo.getSeasonalAnime(year, season, sort, limit, offset)
    }

}