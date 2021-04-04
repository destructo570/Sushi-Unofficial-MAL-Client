package com.destructo.sushi.ui.anime.upcomingAnime

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeRankingDao
import kotlinx.coroutines.launch

class UpcomingAnimeViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val upcomingAnimeRepo: UpcomingAnimeRepository,
    private val animeRankingDao: AnimeRankingDao
) : ViewModel() {

    val nextPage: LiveData<Resource<AnimeRanking>> =
        upcomingAnimeRepo.upcomingAnimeListNextPage

    val upcomingList: MutableLiveData<Resource<MutableList<AnimeRankingData?>>> =
        upcomingAnimeRepo.upcomingAnimeList

    val upcomingAnimeList = animeRankingDao.getAllAnimeRanking()

    fun getNextPage( nsfw: Boolean) {
        viewModelScope.launch { upcomingAnimeRepo.getTopAnimeNext(nsfw) }
    }

    fun getUpcomingList(offset: String?, limit: String?, nsfw: Boolean) {
        viewModelScope.launch { upcomingAnimeRepo.getAnimeRankingList(offset, limit, nsfw) }
    }

    fun clearList() {
        viewModelScope.launch {
            animeRankingDao.clear()
        }
    }
}