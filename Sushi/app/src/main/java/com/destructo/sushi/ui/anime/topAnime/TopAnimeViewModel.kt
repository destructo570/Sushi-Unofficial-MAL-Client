package com.destructo.sushi.ui.anime.topAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import kotlinx.coroutines.launch
import timber.log.Timber

class TopAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    val jikanApi: JikanApi,
    val malApi: MalApi
):ViewModel(){

    private val _topAnimeList: MutableLiveData<AnimeRanking> = MutableLiveData()
    val topAnimeList: LiveData<AnimeRanking>
        get() = _topAnimeList

    fun getTopAnime(ranking_type:String,offset:String?, limit:String?){
        viewModelScope.launch {
            val getTopAnimeDeferred = malApi.getAnimeRankingAsync(ranking_type,limit,offset,
                ALL_ANIME_FIELDS
            )
            try {
                val getAnimeRanking = getTopAnimeDeferred.await()
                _topAnimeList.value = getAnimeRanking
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }
}