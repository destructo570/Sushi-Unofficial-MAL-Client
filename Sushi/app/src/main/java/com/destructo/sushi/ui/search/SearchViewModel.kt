package com.destructo.sushi.ui.search


import androidx.lifecycle.*
import com.destructo.sushi.model.mal.animeList.AnimeList
import com.destructo.sushi.model.mal.mangaList.MangaList
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.SearchAnimeDao
import com.destructo.sushi.room.SearchMangaDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(

    savedStateHandle: SavedStateHandle,
    private val searchRepo: SearchRepository,
    private val searchAnimeDao: SearchAnimeDao,
    private val searchMangaDao: SearchMangaDao
) : ViewModel() {

    val animeSearchResult: LiveData<Resource<AnimeList>> = searchRepo.animeResult

    val mangaSearchResult: LiveData<Resource<MangaList>> = searchRepo.mangaResult

    val animeSearchResultNext: LiveData<Resource<AnimeList>> = searchRepo.animeResultNext

    val mangaSearchResultNext: LiveData<Resource<MangaList>> = searchRepo.mangaResultNext

    val searchAnimeResult = searchAnimeDao.getAnimeList()

    val searchMangaResult = searchMangaDao.getMangaList()


    private var _searchQuery: MutableLiveData<String> = MutableLiveData()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    fun getAnimeResult(query: String, field: String, limit: String, offset: String, nsfw: Boolean) {
        viewModelScope.launch {
            searchRepo.getAnimeResult(
                query = query,
                limit = limit,
                offset = offset,
                field = field,
                nsfw = nsfw
            )
        }

    }

    fun getNextAnimePage(nsfw: Boolean) {
        viewModelScope.launch { searchRepo.getAnimeNext(nsfw) }
    }

    fun getNextMangaPage(nsfw: Boolean) {
        viewModelScope.launch { searchRepo.getMangaNext(nsfw) }
    }

    fun clearAnimeList() {
        searchAnimeDao.clear()
    }

    fun clearMangaList() {
        searchMangaDao.clear()
    }

    fun getMangaResult(query: String, field: String, limit: String, offset: String, nsfw: Boolean) {
        viewModelScope.launch {
            searchRepo.getMangaResult(
                query = query,
                limit = limit,
                offset = offset,
                field = field,
                nsfw = nsfw
            )
        }

    }

    fun setQueryString(query: String) {
        _searchQuery.value = query
    }

}