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
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception


class MangaDetailViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val mangaDetailsRepo: MangaDetailsRepository

) : ViewModel() {

    val mangaDetail: LiveData<Resource<Manga>> = mangaDetailsRepo.mangaDetail

    val mangaCharacter: LiveData<Resource<MangaCharacter>> = mangaDetailsRepo.mangaCharacter

    val mangaReview: LiveData<Resource<MangaReview>> = mangaDetailsRepo.mangaReview


    fun getMangaDetail(malId: Int) {
        mangaDetailsRepo.getMangaDetail(malId)
    }

    fun getMangaCharacters(malId: Int) {
        mangaDetailsRepo.getMangaCharacters(malId)
    }

    fun getMangaReviews(malId: Int) {
        mangaDetailsRepo.getMangaReviews(malId)
    }


}