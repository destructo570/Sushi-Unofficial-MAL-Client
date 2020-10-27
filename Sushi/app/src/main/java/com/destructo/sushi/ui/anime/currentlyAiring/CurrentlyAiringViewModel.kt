package com.destructo.sushi.ui.anime.currentlyAiring

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.MalApi
import kotlinx.coroutines.launch
import timber.log.Timber

class CurrentlyAiringViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
        val malApi: MalApi
    ):ViewModel() {

    private val _currentlyAiring: MutableLiveData<AnimeRanking> = MutableLiveData()
    val currentlyAiring: LiveData<AnimeRanking>
        get() = _currentlyAiring


    fun getCurrentlyAiringAnime(offset:String?, limit:String?){
        viewModelScope.launch {
            val getAiringDeferred = malApi.getAnimeRankingAsync("airing",limit,offset,
                ALL_ANIME_FIELDS
            )
            try {
                val getAnimeRanking = getAiringDeferred.await()
                _currentlyAiring.value = getAnimeRanking
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }
}