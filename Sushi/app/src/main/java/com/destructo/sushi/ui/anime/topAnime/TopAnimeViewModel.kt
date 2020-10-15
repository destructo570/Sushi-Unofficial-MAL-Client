package com.destructo.sushi.ui.anime.topAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.network.JikanApi

class TopAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    jikanApi: JikanApi
):ViewModel(){

    private val _topAnimeList: MutableLiveData<TopAnime> = MutableLiveData()
    val topAnimeList: LiveData<TopAnime>
        get() = _topAnimeList


    fun insertTopAnime(topAnime: TopAnime){
        _topAnimeList.value = topAnime
    }
}