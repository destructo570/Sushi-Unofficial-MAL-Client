package com.destructo.sushi.ui.anime.currentlyAiring

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.top.TopAnime

class CurrentlyAiringViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
    ):ViewModel() {

    private val _currentlyAiring: MutableLiveData<TopAnime> = MutableLiveData()
    val currentlyAiring: LiveData<TopAnime>
        get() = _currentlyAiring


    fun insertUpcomingAnime(upcomingAnime:TopAnime){
        _currentlyAiring.value = upcomingAnime
    }
}