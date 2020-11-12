package com.destructo.sushi.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.mal.animeList.AnimeList
import com.destructo.sushi.model.mal.mangaList.MangaList
import com.destructo.sushi.model.mal.userInfo.UserInfo
import com.destructo.sushi.network.Resource
import com.destructo.sushi.ui.user.profile.ProfileRepository

class SearchViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val searchRepo: SearchRepository
): ViewModel(){

    val animeSearchResult: LiveData<Resource<AnimeList>> = searchRepo.animeResult

    val mangaSearchResult: LiveData<Resource<MangaList>> = searchRepo.mangaResult

    private var _searchQuery: MutableLiveData<String> = MutableLiveData()
    val searchQuery: LiveData<String>
    get() = _searchQuery

    fun getAnimeResult(query:String, field:String, limit:String, offset:String) {
        searchRepo.getAnimeResult(
            query = query,
            limit = limit,
            offset = offset,
            field = field)
    }

    fun getMangaResult(query:String, field:String, limit:String, offset:String) {
        searchRepo.getMangaResult(
            query = query,
            limit = limit,
            offset = offset,
            field = field)
    }

    fun setQueryString(query: String){
        _searchQuery.value = query
    }

}