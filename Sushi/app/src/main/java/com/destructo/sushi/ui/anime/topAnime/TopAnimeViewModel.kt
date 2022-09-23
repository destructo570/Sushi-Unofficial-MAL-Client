package com.destructo.sushi.ui.anime.topAnime


import androidx.lifecycle.*
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeRankingDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopAnimeViewModel
@Inject
constructor(

    savedStateHandle: SavedStateHandle,
    private val topAnimeRepo: TopAnimeRepository,
    private val animeRankingDao: AnimeRankingDao
):ViewModel(){

    val topAnimeNextPage: LiveData<Resource<AnimeRanking>> =
        topAnimeRepo.topAnimeListNextPage

    val animeRankingList: MutableLiveData<Resource<MutableList<AnimeRankingData?>>> =
        topAnimeRepo.animeRankingList

    val listOfAllTopAnime = animeRankingDao.getAllAnimeRanking()

    fun getTopAnimeNextPage( nsfw: Boolean){
        viewModelScope.launch { topAnimeRepo.getTopAnimeNext(nsfw) }
    }

    fun getAnimeRankingList(offset:String?, limit:String?, nsfw: Boolean){
        viewModelScope.launch { topAnimeRepo.getAnimeRankingList(offset, limit, nsfw) }
    }

    fun clearAnimeList(){
            animeRankingDao.clear()
    }

    fun setRankingType(ranking_type:String){
        topAnimeRepo.rankingType = ranking_type
    }

}