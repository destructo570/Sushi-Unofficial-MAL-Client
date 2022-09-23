package com.destructo.sushi.ui.manga.mangaDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.params.MangaUpdateParams
import com.destructo.sushi.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaDetailViewModel
@Inject
constructor(

    savedStateHandle: SavedStateHandle,
    private val mangaDetailsRepo: MangaDetailsRepository

) : ViewModel() {

    val mangaDetail: LiveData<Resource<Manga>> = mangaDetailsRepo.mangaDetail

    val mangaCharacter: LiveData<Resource<MangaCharacter>> = mangaDetailsRepo.mangaCharacter

    val mangaReview: LiveData<Resource<MangaReview>> = mangaDetailsRepo.mangaReview

    val userMangaStatus: LiveData<Resource<UpdateUserManga>> = mangaDetailsRepo.userMangaStatus

    val userMangaRemove: LiveData<Resource<Unit>> = mangaDetailsRepo.userMangaRemove

    fun getMangaDetail(malId: Int, isEdited: Boolean) {
        viewModelScope.launch{ mangaDetailsRepo.getMangaDetail(malId, isEdited) }
    }

    fun getMangaCharacters(malId: Int) {
        viewModelScope.launch{ mangaDetailsRepo.getMangaCharacters(malId) }
    }

    fun getMangaReviews(malId: Int, page: String) {
        viewModelScope.launch{ mangaDetailsRepo.getMangaReviews(malId, page) }
    }

    fun removeAnime(mangaId:Int){
        viewModelScope.launch{ mangaDetailsRepo.removeMangaFromList(mangaId.toString()) }
    }

    fun updateUserMangaStatus(mangaUpdateParams: MangaUpdateParams){
        viewModelScope.launch{ mangaDetailsRepo.updateUserMangaList(mangaUpdateParams) }
    }


}