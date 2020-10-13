package com.destructo.sushi.ui.anime.seasonalAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.season.Season
import com.destructo.sushi.model.top.TopAnime

class SeasonalAnimeViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
    ): ViewModel() {

    private val _seasonalAnime: MutableLiveData<Season> = MutableLiveData()
    val seasonalAnime: LiveData<Season>
        get() = _seasonalAnime


    fun insertSeasonAnime(seasonAnime:Season){
        _seasonalAnime.value = seasonAnime
    }
}