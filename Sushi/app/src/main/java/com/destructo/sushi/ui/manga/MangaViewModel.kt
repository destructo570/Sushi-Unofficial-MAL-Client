package com.destructo.sushi.ui.manga

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.model.jikan.top.TopManga
import com.destructo.sushi.model.mal.mangaRanking.MangaRanking
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

class MangaViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val mangaRepo: MangaRepository,

    ) : ViewModel() {

    val topManga: LiveData<Resource<MangaRanking>> = mangaRepo.topManga

    fun getTopMangaList(ranking_type: String, limit: String?, offset: String?) {
        mangaRepo.getTopManga(ranking_type, limit, offset)
    }
}