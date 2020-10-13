package com.destructo.sushi.ui.anime.upcomingAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.top.TopAnime
import com.destructo.sushi.network.JikanApi

class UpcomingAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    jikanApi: JikanApi
)
    :ViewModel() {

    private val _upcomingAnime:MutableLiveData<TopAnime> = MutableLiveData()
    val upcomingAnime:LiveData<TopAnime>
        get() = _upcomingAnime


    fun insertUpcomingAnime(upcomingAnime:TopAnime){
        _upcomingAnime.value = upcomingAnime
    }
}