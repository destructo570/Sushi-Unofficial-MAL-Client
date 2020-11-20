package com.destructo.sushi.ui.anime.currentlyAiring

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeRankingDao
import kotlinx.coroutines.launch
import timber.log.Timber

class CurrentlyAiringViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val currentlyAiringRepo: CurrentlyAiringRepository,
    private val animeRankingDao: AnimeRankingDao
) : ViewModel() {

    val nextPage: LiveData<Resource<AnimeRanking>> =
        currentlyAiringRepo.airingAnimeListNextPage

    val currentlyAiringList: MutableLiveData<Resource<MutableList<AnimeRankingData?>>> =
        currentlyAiringRepo.airingAnimeList

    val currentlyAiring = animeRankingDao.getAllAnimeRanking()

    fun getTopAnimeNextPage() {
        currentlyAiringRepo.getTopAnimeNext()
    }

    fun getAnimeRankingList(offset: String?, limit: String?) {
        currentlyAiringRepo.getAnimeRankingList(offset, limit)
    }

    fun clearAnimeList() {
        viewModelScope.launch {
            animeRankingDao.clear()
        }
    }
}