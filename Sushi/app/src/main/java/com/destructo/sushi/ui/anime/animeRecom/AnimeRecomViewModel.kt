package com.destructo.sushi.ui.anime.animeRecom

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch

class AnimeRecomViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
        private val animeRecomRepo:AnimeRecomRepo
    )
    :ViewModel() {

    private var _animeRecom: MutableLiveData<Resource<List<Anime>>> = MutableLiveData()
    val animeRecom: LiveData<Resource<List<Anime>>>
        get() = _animeRecom

    fun getAnimeRecomm(offset:String?, limit:String?, nsfw: Boolean) {
        viewModelScope.launch { _animeRecom = animeRecomRepo.getAnimeRecom(offset, limit, nsfw) }
    }

}