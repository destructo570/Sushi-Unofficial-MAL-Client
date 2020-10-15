package com.destructo.sushi.ui.anime.currentlyAiring

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking

class CurrentlyAiringViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
    ):ViewModel() {

    private val _currentlyAiring: MutableLiveData<AnimeRanking> = MutableLiveData()
    val currentlyAiring: LiveData<AnimeRanking>
        get() = _currentlyAiring


    fun insertUpcomingAnime(upcomingAnime: AnimeRanking){
        _currentlyAiring.value = upcomingAnime
    }
}