package com.destructo.sushi.ui.manga.mangaDetails

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.network.Resource


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

    val userMangaStatus: LiveData<Resource<UpdateUserManga>> = mangaDetailsRepo.userMangaStatus

    val userMangaRemove: LiveData<Resource<Unit>> = mangaDetailsRepo.userMangaRemove


    fun getMangaDetail(malId: Int, isEdited: Boolean) {
        mangaDetailsRepo.getMangaDetail(malId, isEdited)
    }

    fun getMangaCharacters(malId: Int) {
        mangaDetailsRepo.getMangaCharacters(malId)
    }

    fun getMangaReviews(malId: Int) {
        mangaDetailsRepo.getMangaReviews(malId)
    }

    fun removeAnime(mangaId:Int){
        mangaDetailsRepo.removeMangaFromList(mangaId.toString())
    }


    fun updateUserMangaStatus(mangaId:String,status:String?=null,
                              is_rereading:Boolean?=null,score:Int?=null,
                              num_volumes_read:Int?=null,num_chapters_read:Int?=null,priority:Int?=null,
                              num_times_reread:Int?=null, reread_value:Int?=null,
                              tags:List<String>?=null,comments:String?=null){

        mangaDetailsRepo.updateUserMangaList(mangaId,
            status,is_rereading,score,num_volumes_read,num_chapters_read,
            priority,num_times_reread,reread_value,tags,comments)

    }


}