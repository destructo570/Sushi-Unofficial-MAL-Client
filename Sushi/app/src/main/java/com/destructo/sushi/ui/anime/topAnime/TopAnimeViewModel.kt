package com.destructo.sushi.ui.anime.topAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter

class TopAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    jikanApi: JikanApi
):ViewModel(){

    private val _topAnimeList: MutableLiveData<AnimeRanking> = MutableLiveData()
    val topAnimeList: LiveData<AnimeRanking>
        get() = _topAnimeList


    fun insertTopAnime(topAnime: AnimeRanking){
        _topAnimeList.value = topAnime
    }
}