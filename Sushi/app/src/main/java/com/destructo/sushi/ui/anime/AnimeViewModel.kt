package com.destructo.sushi.ui.anime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.news.NewsItem
import com.destructo.sushi.network.Resource

class AnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val animeRepo:AnimeRepository
): ViewModel() {

    private var _upcomingAnime:MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()
    val upcomingAnime:MutableLiveData<Resource<AnimeRanking>>
        get() = _upcomingAnime

    private var _currentlyAiring:MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()
    val currentlyAiring:MutableLiveData<Resource<AnimeRanking>>
        get() = _currentlyAiring

    private var _animeRecom:MutableLiveData<Resource<List<Anime>>> = MutableLiveData()
    val animeRecom:MutableLiveData<Resource<List<Anime>>>
        get() = _animeRecom

    private var _newsList:MutableLiveData<Resource<MutableList<NewsItem>>> = MutableLiveData()
    val newsList:MutableLiveData<Resource<MutableList<NewsItem>>>
        get() = _newsList


    fun getUpcomingAnime(ranking_type:String,offset:String?, limit:String?,
                         nsfw: Boolean) {
        _upcomingAnime = animeRepo.getTopAnime(ranking_type, offset, limit, nsfw)
    }

    fun getCurrentlyAiringAnime(ranking_type:String,offset:String?, limit:String?,
                                nsfw: Boolean) {
        _currentlyAiring = animeRepo.getTopAnime(ranking_type, offset, limit, nsfw)
    }

    fun getAnimeRecomm(offset:String?, limit:String?, nsfw: Boolean) {
        _animeRecom = animeRepo.getAnimeRecom(offset, limit, nsfw)
    }

    fun getNews() {
        _newsList = animeRepo.getLatestNews()
    }



}