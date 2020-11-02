package com.destructo.sushi.ui.anime.upcomingAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

class UpcomingAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val upcomingAnimeRepo: UpcomingAnimeRepository
)
    :ViewModel() {

    val upcomingAnime:LiveData<Resource<AnimeRanking>> = upcomingAnimeRepo.upcomingAnime

    fun getUpcomingAnime(offset:String?, limit:String?){
        upcomingAnimeRepo.getUpcomingAnime(offset, limit)
    }

}