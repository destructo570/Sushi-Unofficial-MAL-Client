package com.destructo.sushi.ui.manga.mangaDetails

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception


class MangaDetailViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    val jikanApi: JikanApi,
    val malApi: MalApi
) : ViewModel() {

    private val _mangaDetail: MutableLiveData<Manga> = MutableLiveData()
    val mangaDetail: LiveData<Manga>
        get() = _mangaDetail

    private val _mangaCharacter: MutableLiveData<MangaCharacter> = MutableLiveData()
    val mangaCharacter: LiveData<MangaCharacter>
        get() = _mangaCharacter

    private val _mangaReview: MutableLiveData<MangaReview> = MutableLiveData()
    val mangaReview: LiveData<MangaReview>
        get() = _mangaReview


    fun getMangaDetail(malId: Int) {
        viewModelScope.launch {
            val mangaId: String = malId.toString()
            val getmangaByIdDeferred = malApi.getMangaByIdAsync(mangaId, ALL_MANGA_FIELDS)
            try {
                val mangaDetails = getmangaByIdDeferred.await()
                _mangaDetail.value = mangaDetails

            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }

        }
    }

    fun getMangaCharacters(malId: Int) {
        viewModelScope.launch {
            val mangaId: String = malId.toString()
            val getMangaCharactersDeferred = jikanApi.getMangaCharactersAsync(mangaId)
            try {
                val mangaCharacter = getMangaCharactersDeferred.await()
                _mangaCharacter.value = mangaCharacter

            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }

        }
    }


    fun getMangaReviews(malId: Int) {
        viewModelScope.launch {
            val mangaId: String = malId.toString()
            val getMangaReviewsDeferred = jikanApi.getMangaReviewsAsync(mangaId)
            try {
                val mangaReviews = getMangaReviewsDeferred.await()
                _mangaReview.value = mangaReviews

            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }

        }
    }


}