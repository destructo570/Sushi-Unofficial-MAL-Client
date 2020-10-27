package com.destructo.sushi.ui.anime.upcomingAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import kotlinx.coroutines.launch
import timber.log.Timber

class UpcomingAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    val jikanApi: JikanApi,
    val malApi: MalApi
)
    :ViewModel() {

    private val _upcomingAnime:MutableLiveData<AnimeRanking> = MutableLiveData()
    val upcomingAnime:LiveData<AnimeRanking>
        get() = _upcomingAnime


    fun getUpcomingAnime(offset:String?, limit:String?){
        viewModelScope.launch {
            val getUpcomingDeferred = malApi.getAnimeRankingAsync("upcoming",limit,offset,
                ALL_ANIME_FIELDS
            )
            try {
                val getAnimeRanking = getUpcomingDeferred.await()
                _upcomingAnime.value = getAnimeRanking
            }catch (e:Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }

}