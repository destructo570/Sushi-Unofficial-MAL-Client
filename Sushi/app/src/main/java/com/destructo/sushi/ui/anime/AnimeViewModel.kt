package com.destructo.sushi.ui.anime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.ui.anime.upcomingAnime.AnimeRepository
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

    private val animeRepo = AnimeRepository(malApi)

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


    fun getTopAnime(ranking_type:String,offset:String?, limit:String?) {
        _topAnime = animeRepo.getTopAnime(ranking_type, offset, limit)
    }

    fun getUpcomingAnime(ranking_type:String,offset:String?, limit:String?) {
        _upcomingAnime = animeRepo.getTopAnime(ranking_type, offset, limit)
    }

    fun getCurrentlyAiringAnime(ranking_type:String,offset:String?, limit:String?) {
        _currentlyAiring = animeRepo.getTopAnime(ranking_type, offset, limit)
    }

    fun getSeasonalAnime(year:String,season:String,sort:String?,
                         limit:String?,offset:String?){
        _seasonalAnime = animeRepo.getSeasonalAnime(year, season, sort, limit, offset)
    }
}