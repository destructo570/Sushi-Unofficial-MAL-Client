package com.destructo.sushi.ui.manga

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.model.jikan.top.TopManga
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.model.mal.mangaRanking.MangaRanking
import com.destructo.sushi.model.mal.mangaRanking.MangaRankingData
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeRankingDao
import com.destructo.sushi.room.MangaRankingDao
import kotlinx.coroutines.launch
import timber.log.Timber

class MangaViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val mangaRepo: MangaRepository,
    private val mangaRankingDao: MangaRankingDao
):ViewModel(){

    val topMangaNextPage: LiveData<Resource<MangaRanking>> =
        mangaRepo.topMangaListNextPage

    val mangaRankingList: MutableLiveData<Resource<MutableList<MangaRankingData?>>> =
        mangaRepo.mangaRankingList

    val listOfAllTopManga = mangaRankingDao.getAllMangaRanking()

    fun getTopMangaNextPage(){
        mangaRepo.getTopMangaNext()
    }

    fun getMangaRankingList(offset:String?, limit:String?){
        mangaRepo.getMangaRankingList(offset,limit)
    }

    fun clearMangaList(){
        viewModelScope.launch{
            mangaRankingDao.clear()
        }
    }

    fun setRankingType(ranking_type:String){
        mangaRepo.rankingType = ranking_type
    }

}