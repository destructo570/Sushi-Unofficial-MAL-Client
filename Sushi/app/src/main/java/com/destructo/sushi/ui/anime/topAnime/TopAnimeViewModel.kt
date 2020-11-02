package com.destructo.sushi.ui.anime.topAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import kotlinx.coroutines.launch
import timber.log.Timber

class TopAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val topAnimeRepo: TopAnimeRepository,
):ViewModel(){

    val topAnimeList: LiveData<Resource<AnimeRanking>> = topAnimeRepo.topAnimeListOva

    fun getTopAnime(ranking_type:String,offset:String?, limit:String?){
        topAnimeRepo.getTopAnime(ranking_type,offset,limit)
    }
}