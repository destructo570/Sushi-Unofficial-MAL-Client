package com.destructo.sushi.ui.anime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.Resource

class AnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val animeRepo:AnimeRepository
): ViewModel() {

    private var _seasonalAnime:MutableLiveData<Resource<SeasonalAnime>> = MutableLiveData()
    val seasonalAnime:LiveData<Resource<SeasonalAnime>>
        get() = _seasonalAnime

    private var _topAnime:MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()
    val topAnime:MutableLiveData<Resource<AnimeRanking>>
        get() = _topAnime

    private var _upcomingAnime:MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()
    val upcomingAnime:MutableLiveData<Resource<AnimeRanking>>
        get() = _upcomingAnime

    private var _currentlyAiring:MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()
    val currentlyAiring:MutableLiveData<Resource<AnimeRanking>>
        get() = _currentlyAiring


    fun getTopAnime(ranking_type:String,offset:String?, limit:String?,
                    nsfw: Boolean) {
        _topAnime = animeRepo.getTopAnime(ranking_type, offset, limit, nsfw)
    }

    fun getUpcomingAnime(ranking_type:String,offset:String?, limit:String?,
                         nsfw: Boolean) {
        _upcomingAnime = animeRepo.getTopAnime(ranking_type, offset, limit, nsfw)
    }

    fun getCurrentlyAiringAnime(ranking_type:String,offset:String?, limit:String?,
                                nsfw: Boolean) {
        _currentlyAiring = animeRepo.getTopAnime(ranking_type, offset, limit, nsfw)
    }

    fun getSeasonalAnime(year:String,season:String,sort:String?,
                         limit:String?,offset:String?){
        _seasonalAnime = animeRepo.getSeasonalAnime(year, season, sort, limit, offset)
    }

}