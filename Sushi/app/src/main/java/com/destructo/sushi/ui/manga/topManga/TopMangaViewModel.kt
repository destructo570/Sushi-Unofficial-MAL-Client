package com.destructo.sushi.ui.manga.topManga

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.mal.mangaRanking.MangaRanking
import com.destructo.sushi.model.mal.mangaRanking.MangaRankingData
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.MangaRankingDao
import kotlinx.coroutines.launch

class TopMangaViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val topMangaRepo: TopMangaRepository,
    private val mangaRankingDao: MangaRankingDao
):ViewModel(){

    val topMangaNextPage: LiveData<Resource<MangaRanking>> =
        topMangaRepo.topMangaListNextPage

    val mangaRankingList: MutableLiveData<Resource<MutableList<MangaRankingData?>>> =
        topMangaRepo.mangaRankingList

    val listOfAllTopManga = mangaRankingDao.getAllMangaRanking()

    fun getTopMangaNextPage(nsfw:Boolean){
        topMangaRepo.getTopMangaNext(nsfw)
    }

    fun getMangaRankingList(offset:String?, limit:String?, nsfw:Boolean){
        topMangaRepo.getMangaRankingList(offset,limit,nsfw)
    }

    fun clearMangaList(){
        viewModelScope.launch{
            mangaRankingDao.clear()
        }
    }

    fun setRankingType(ranking_type:String){
        topMangaRepo.rankingType = ranking_type
    }

}