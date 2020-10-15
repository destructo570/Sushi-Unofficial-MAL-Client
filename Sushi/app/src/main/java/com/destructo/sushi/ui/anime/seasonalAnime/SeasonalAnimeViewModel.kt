package com.destructo.sushi.ui.anime.seasonalAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.jikan.season.Season
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime

class SeasonalAnimeViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
    ): ViewModel() {

    private val _seasonalAnime: MutableLiveData<SeasonalAnime> = MutableLiveData()
    val seasonalAnime: LiveData<SeasonalAnime>
        get() = _seasonalAnime


    fun insertSeasonAnime(seasonAnime: SeasonalAnime){
        _seasonalAnime.value = seasonAnime
    }
}