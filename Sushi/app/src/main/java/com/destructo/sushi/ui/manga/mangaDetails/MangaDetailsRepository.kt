package com.destructo.sushi.ui.manga.mangaDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.BASIC_MANGA_FIELDS
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MangaDetailsRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val jikanApi: JikanApi
){

    var mangaDetail: MutableLiveData<Resource<Manga>> = MutableLiveData()

    var mangaCharacter: MutableLiveData<Resource<MangaCharacter>> = MutableLiveData()

    var mangaReview: MutableLiveData<Resource<MangaReview>> = MutableLiveData()

    var userMangaStatus: MutableLiveData<Resource<UpdateUserManga>> = MutableLiveData()


    fun getMangaDetail(malId: Int) {
        mangaDetail.value = Resource.loading(null)

        GlobalScope.launch {
            val mangaId: String = malId.toString()
            val getMangaByIdDeferred = malApi.getMangaByIdAsync(mangaId, ALL_MANGA_FIELDS)
            try {
                val mangaDetails = getMangaByIdDeferred.await()
                withContext(Dispatchers.Main) {
                    mangaDetail.value = Resource.success(mangaDetails)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mangaDetail.value = Resource.error(e.message ?: "", null)

                }
            }
        }
    }

    fun getMangaCharacters(malId: Int) {
        mangaCharacter.value = Resource.loading(null)

        GlobalScope.launch {
            val mangaId: String = malId.toString()
            val getMangaCharactersDeferred = jikanApi.getMangaCharactersAsync(mangaId)
            try {
                val mangaCharacterList = getMangaCharactersDeferred.await()
                withContext(Dispatchers.Main) {
                    mangaCharacter.value = Resource.success(mangaCharacterList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mangaCharacter.value = Resource.error(e.message ?: "", null)

                }
            }
        }
    }

    fun getMangaReviews(malId: Int) {
        mangaReview.value = Resource.loading(null)

        GlobalScope.launch {
            val mangaId: String = malId.toString()
            val getMangaReviewsDeferred = jikanApi.getMangaReviewsAsync(mangaId)
            try {
                val mangaCharacterList = getMangaReviewsDeferred.await()
                withContext(Dispatchers.Main) {
                    mangaReview.value = Resource.success(mangaCharacterList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mangaCharacter.value = Resource.error(e.message ?: "", null)

                }
            }
        }
    }


    fun updateUserMangaList(animeId:String,status:String?=null,
                       is_rereading:Boolean?=null,score:Int?=null,
                       num_volumes_read:Int?=null,num_chapters_read:Int?=null,priority:Int?=null,
                       num_times_reread:Int?=null, reread_value:Int?=null,
                       tags:List<String>?=null,comments:String?=null) {
        userMangaStatus.value = Resource.loading(null)

        GlobalScope.launch {
            val addEpisodeDeferred = malApi.updateUserManga(animeId,
                status,is_rereading,score,num_volumes_read,num_chapters_read,
                priority,num_times_reread,reread_value,tags,comments)
            try {
                val mangaStatus = addEpisodeDeferred.await()
                withContext(Dispatchers.Main){
                    userMangaStatus.value = Resource.success(mangaStatus)
                }
            }catch (e: java.lang.Exception){
                withContext(Dispatchers.Main){
                    userMangaStatus.value = Resource.error(e.message ?: "", null)}
            }
        }
    }

}