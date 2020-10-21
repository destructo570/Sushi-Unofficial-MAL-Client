package com.destructo.sushi.ui.anime.upcomingAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.JikanApi

class UpcomingAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    jikanApi: JikanApi
)
    :ViewModel() {

    private val _upcomingAnime:MutableLiveData<AnimeRanking> = MutableLiveData()
    val upcomingAnime:LiveData<AnimeRanking>
        get() = _upcomingAnime


    fun insertUpcomingAnime(upcomingAnime: AnimeRanking){
        _upcomingAnime.value = upcomingAnime
    }


}