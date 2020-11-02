package com.destructo.sushi.ui.anime.currentlyAiring

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

class CurrentlyAiringViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
        private val currentlyAiringRepo:CurrentlyAiringRepository
    ):ViewModel() {

    val currentlyAiring: LiveData<Resource<AnimeRanking>> = currentlyAiringRepo.currentlyAiring

    fun getCurrentlyAiringAnime(offset:String?, limit:String?){
        currentlyAiringRepo.getCurrentlyAiring(offset, limit)
    }
}