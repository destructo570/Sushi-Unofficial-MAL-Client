package com.destructo.sushi.ui.anime.animeRecom

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.network.Resource

class AnimeRecomViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
        private val animeRecomRepo:AnimeRecomRepo
    )
    :ViewModel() {

    private var _animeRecom: MutableLiveData<Resource<List<Anime>>> = MutableLiveData()
    val animeRecom: MutableLiveData<Resource<List<Anime>>>
        get() = _animeRecom

    fun getAnimeRecomm(offset:String?, limit:String?, nsfw: Boolean) {
        _animeRecom = animeRecomRepo.getAnimeRecom(offset, limit, nsfw)
    }

}