package com.destructo.sushi.ui.anime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.news.NewsItem
import com.destructo.sushi.model.mal.promotion.PromotionalItem
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch

class AnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val animeRepo: AnimeRepository
) : ViewModel() {

    private var _upcomingAnime: MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()
    val upcomingAnime: MutableLiveData<Resource<AnimeRanking>>
        get() = _upcomingAnime

    private var _currentlyAiring: MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()
    val currentlyAiring: MutableLiveData<Resource<AnimeRanking>>
        get() = _currentlyAiring

    private var _animeRecom: MutableLiveData<Resource<List<Anime>>> = MutableLiveData()
    val animeRecom: MutableLiveData<Resource<List<Anime>>>
        get() = _animeRecom

    private var _newsList: MutableLiveData<Resource<MutableList<NewsItem>>> = MutableLiveData()
    val newsList: MutableLiveData<Resource<MutableList<NewsItem>>>
        get() = _newsList

    private var _promotionList: MutableLiveData<Resource<MutableList<PromotionalItem>>> =
        MutableLiveData()
    val promotionList: MutableLiveData<Resource<MutableList<PromotionalItem>>>
        get() = _promotionList


    fun getUpcomingAnime(
        ranking_type: String, offset: String?, limit: String?,
        nsfw: Boolean
    ) {
        viewModelScope.launch {
            _upcomingAnime = animeRepo.getTopAnime(ranking_type, offset, limit, nsfw)
        }
    }

    fun getCurrentlyAiringAnime(
        ranking_type: String, offset: String?, limit: String?,
        nsfw: Boolean
    ) {
        viewModelScope.launch {
            _currentlyAiring = animeRepo.getTopAnime(ranking_type, offset, limit, nsfw)
        }
    }

    fun getAnimeRecomm(offset: String?, limit: String?, nsfw: Boolean) {
        viewModelScope.launch { _animeRecom = animeRepo.getAnimeRecom(offset, limit, nsfw) }
    }

    fun getNews() {
        _newsList = animeRepo.getLatestNews()
    }

    fun getLatestPromotional() {
        _promotionList = animeRepo.getLatestPromotional()
    }


}