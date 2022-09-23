package com.destructo.sushi.ui.anime.seasonalAnime


import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.season.SeasonArchive
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.SeasonAnimeDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonalAnimeViewModel
    @Inject
    constructor(

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
        viewModelScope.launch { seasonalAnimeRepo.getSeasonArchive() }
    }

    fun getAnimeNextPage(){
        viewModelScope.launch { seasonalAnimeRepo.getSeasonAnimeNext() }
    }

    fun getSeasonalAnime(year:String,season:String,sort:String?,
                         limit:String?,offset:String?){
        viewModelScope.launch { seasonalAnimeRepo.getSeasonalAnime(year, season, sort, limit, offset) }
    }

    fun clearList(){ seasonalDao.clear() }

}