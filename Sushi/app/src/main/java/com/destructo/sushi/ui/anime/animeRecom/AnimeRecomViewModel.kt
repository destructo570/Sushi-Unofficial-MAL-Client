package com.destructo.sushi.ui.anime.animeRecom

import androidx.lifecycle.*
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeRecomViewModel
    @Inject
    constructor(

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