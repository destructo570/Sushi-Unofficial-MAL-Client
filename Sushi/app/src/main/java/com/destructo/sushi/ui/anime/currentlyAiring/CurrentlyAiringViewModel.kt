package com.destructo.sushi.ui.anime.currentlyAiring


import androidx.lifecycle.*
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeRankingDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentlyAiringViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val currentlyAiringRepo: CurrentlyAiringRepository,
    private val animeRankingDao: AnimeRankingDao
) : ViewModel() {

    val nextPage: LiveData<Resource<AnimeRanking>> =
        currentlyAiringRepo.airingAnimeListNextPage

    val currentlyAiringList: MutableLiveData<Resource<MutableList<AnimeRankingData?>>> =
        currentlyAiringRepo.airingAnimeList

    val currentlyAiring = animeRankingDao.getAllAnimeRanking()

    fun getTopAnimeNextPage(nsfw: Boolean) {
        viewModelScope.launch {  currentlyAiringRepo.getTopAnimeNext(nsfw) }
    }

    fun getAnimeRankingList(offset: String?, limit: String?, nsfw: Boolean) {
        viewModelScope.launch { currentlyAiringRepo.getAnimeRankingList(offset, limit, nsfw) }
    }

    fun clearAnimeList() {
        animeRankingDao.clear()
    }
}