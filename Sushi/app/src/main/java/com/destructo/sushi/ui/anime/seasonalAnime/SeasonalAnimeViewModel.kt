package com.destructo.sushi.ui.anime.seasonalAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.season.SeasonArchive
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.SeasonAnimeDao
import kotlinx.coroutines.launch

class SeasonalAnimeViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
        private val seasonalAnimeRepo:SeasonalAnimeRepository,
        private val seasonalDao: SeasonAnimeDao
    ): ViewModel() {

    val seasonalAnime: LiveData<Resource<SeasonalAnime>> = seasonalAnimeRepo.seasonalAnime

    val seasonArchive: LiveData<Resource<SeasonArchive>> = seasonalAnimeRepo.seasonArchive

    val nextPage: LiveData<Resource<SeasonalAnime>> =
        seasonalAnimeRepo.seasonalNextPage

    val seasonalAnimeList = seasonalDao.getAllSeasonAnime()

    fun getSeasonArchive(){
        seasonalAnimeRepo.getSeasonArchive()
    }

    fun getAnimeNextPage(){
        seasonalAnimeRepo.getSeasonAnimeNext()
    }

    fun getSeasonalAnime(year:String,season:String,sort:String?,
                         limit:String?,offset:String?){
        seasonalAnimeRepo.getSeasonalAnime(year, season, sort, limit, offset)
    }

    fun clearList(){
        viewModelScope.launch {
            seasonalDao.clear()
        }
    }

}