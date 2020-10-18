package com.destructo.sushi.ui.manga

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.model.jikan.top.TopManga
import com.destructo.sushi.model.mal.mangaRanking.MangaRanking
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import kotlinx.coroutines.launch
import timber.log.Timber

class MangaViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val jikanApi: JikanApi,
    private val malApi: MalApi
):ViewModel(){


    private val _topManga:MutableLiveData<MangaRanking> = MutableLiveData()
    val topManga:LiveData<MangaRanking>
    get() = _topManga

    fun getTopMangaList(ranking_type:String,limit:String?,offset:String?){
        viewModelScope.launch{
            val getTopMangaDeferred = malApi.getMangaRankingAsync(
                ranking_type, limit, offset, ALL_MANGA_FIELDS)
        try {
            val topManga = getTopMangaDeferred.await()
            _topManga.value = topManga
        }catch (e:Exception){
            Timber.e("Error: ${e.message}")
        }
        }
    }
}