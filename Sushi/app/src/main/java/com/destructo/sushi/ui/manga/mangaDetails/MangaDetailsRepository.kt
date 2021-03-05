package com.destructo.sushi.ui.manga.mangaDetails

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.model.database.MangaCharacterListEntity
import com.destructo.sushi.model.database.MangaDetailsEntity
import com.destructo.sushi.model.database.MangaReviewsEntity
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.params.MangaUpdateParams
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.MangaCharacterListDao
import com.destructo.sushi.room.MangaDetailsDao
import com.destructo.sushi.room.MangaReviewListDao
import com.destructo.sushi.room.UserMangaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MangaDetailsRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val jikanApi: JikanApi,
    private val mangaDetailsDao: MangaDetailsDao,
    private val mangaCharacterListDao: MangaCharacterListDao,
    private val mangaReviewListDao: MangaReviewListDao,
    private val userMangaListDao: UserMangaDao
) {

    var mangaDetail: MutableLiveData<Resource<Manga>> = MutableLiveData()

    var mangaCharacter: MutableLiveData<Resource<MangaCharacter>> = MutableLiveData()

    var mangaReview: MutableLiveData<Resource<MangaReview>> = MutableLiveData()

    var userMangaStatus: MutableLiveData<Resource<UpdateUserManga>> = MutableLiveData()

    var userMangaRemove: MutableLiveData<Resource<Unit>> = MutableLiveData()

    suspend fun getMangaDetail(malId: Int, isEdited: Boolean) {
        mangaDetail.value = Resource.loading(null)

        val mangaDetailCache = mangaDetailsDao.getMangaDetailsById(malId)

        if (mangaDetailCache != null && !mangaDetailCache.isCacheExpired() && !isEdited) {
            withContext(Dispatchers.Main) {
                mangaDetail.value = Resource.success(mangaDetailCache.manga)
            }
        } else mangaDetailsCall(malId)
    }

    suspend fun getMangaCharacters(malId: Int) {
        mangaCharacter.value = Resource.loading(null)

        val mangaCharacterListCache = mangaCharacterListDao.getMangaCharacterListById(malId)

        if (mangaCharacterListCache != null && !mangaCharacterListCache.isCacheExpired()) {
            withContext(Dispatchers.Main) {
                mangaCharacter.value =
                    Resource.success(mangaCharacterListCache.mangaCharacterList)
            }
        } else mangaCharactersCall(malId)
    }

    suspend fun getMangaReviews(malId: Int, page: String) {

        mangaReview.value = Resource.loading(null)
        val mangaReviewListCache = mangaReviewListDao.getMangaReviewsById(malId)

        if (mangaReviewListCache != null && !mangaReviewListCache.isCacheExpired()) {
            withContext(Dispatchers.Main) {
                mangaReview.value = Resource.success(mangaReviewListCache.reviewList)
            }
        } else mangaReviewsCall(malId, page)
    }


    suspend fun updateUserMangaList(updateParam: MangaUpdateParams) {

        if (updateParam.status == UserAnimeStatus.COMPLETED.value
            && updateParam.totalChapters != null
            && updateParam.totalChapters > 0
        ) {
            updateParam.setNumberOfChaptersRead(updateParam.totalChapters)
            updateParam.totalVolumes?.let { updateParam.setNumberOfVolumesRead(it) }
            updateUserMangaListCall(updateParam)
        } else {
            updateUserMangaListCall(updateParam)
        }
    }

    private suspend fun updateUserMangaListCall(mangaUpdateParams: MangaUpdateParams){
        userMangaStatus.value = Resource.loading(null)

        val addEpisodeDeferred = malApi.updateUserManga(
            mangaUpdateParams.mangaId, mangaUpdateParams.status, mangaUpdateParams.is_rereading,
            mangaUpdateParams.score, mangaUpdateParams.num_volumes_read,
            mangaUpdateParams.num_chapters_read, mangaUpdateParams.priority,
            mangaUpdateParams.num_times_reread, mangaUpdateParams.reread_value,
            mangaUpdateParams.tags, mangaUpdateParams.comments,
            mangaUpdateParams.start_date,mangaUpdateParams.finish_date
        )
        try {
            val mangaStatus = addEpisodeDeferred.await()
            withContext(Dispatchers.Main) {
                userMangaStatus.value = Resource.success(mangaStatus)
                updateCachedUserManga(mangaUpdateParams.mangaId, mangaStatus)
            }
        } catch (e: java.lang.Exception) {
            withContext(Dispatchers.Main) {
                userMangaStatus.value = Resource.error(e.message ?: "", null)
            }
        }
    }

    suspend fun removeMangaFromList(mangaId: String) {
        try {
            malApi.deleteMangaFromList(mangaId).await()
            withContext(Dispatchers.Main) {
                userMangaRemove.value = Resource.success(Unit)
            }
        } catch (e: java.lang.Exception) {
            withContext(Dispatchers.Main) {
                userMangaRemove.value = Resource.error(e.message ?: "", null)
                Timber.e("Error: %s", e.message)
            }
        }
    }

    private suspend fun mangaDetailsCall(malId: Int) {

        val mangaId: String = malId.toString()
        val getMangaByIdDeferred = malApi.getMangaByIdAsync(mangaId, ALL_MANGA_FIELDS)
        try {
            val mangaDetails = getMangaByIdDeferred.await()
            val mangaRequest = MangaDetailsEntity(
                manga = mangaDetails,
                id = malId,
                time = System.currentTimeMillis()
            )
            mangaDetailsDao.insertMangaDetails(mangaRequest)
            withContext(Dispatchers.Main) {
                mangaDetail.value = Resource.success(mangaDetails)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mangaDetail.value = Resource.error(e.message ?: "", null)

            }
        }

    }

    private suspend fun mangaCharactersCall(malId: Int) {
        val mangaId: String = malId.toString()
        val getMangaCharactersDeferred = jikanApi.getMangaCharactersAsync(mangaId)
        try {
            val mangaCharacterList = getMangaCharactersDeferred.await()
            val mangaCharacterRequest = MangaCharacterListEntity(
                mangaCharacterList = mangaCharacterList,
                id = malId,
                time = System.currentTimeMillis()
            )
            mangaCharacterListDao.insertMangaCharacterList(mangaCharacterRequest)
            withContext(Dispatchers.Main) {
                mangaCharacter.value = Resource.success(mangaCharacterRequest.mangaCharacterList)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mangaCharacter.value = Resource.error(e.message ?: "", null)

            }
        }
    }

    private suspend fun mangaReviewsCall(malId: Int, page: String) {
        val mangaId: String = malId.toString()
        val getMangaReviewsDeferred = jikanApi.getMangaReviewsAsync(mangaId, page)
        try {
            val mangaCharacterList = getMangaReviewsDeferred.await()
            val mangaReviewRequest = MangaReviewsEntity(
                reviewList = mangaCharacterList,
                id = malId,
                time = System.currentTimeMillis(),
                currentPage = page
            )
            mangaReviewListDao.insertMangaReviews(mangaReviewRequest)
            withContext(Dispatchers.Main) {
                mangaReview.value = Resource.success(mangaReviewRequest.reviewList)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mangaReview.value = Resource.error(e.message ?: "", null)

            }
        }
    }

    private fun updateCachedUserManga(
        mangaId: String,
        mangaStatus: UpdateUserManga
    ) {
        val anime = userMangaListDao.getUserMangaById(mangaId.toInt())
        anime.updateUserStatus(mangaStatus)
        userMangaListDao.deleteUserMangaById(mangaId.toInt())
        userMangaListDao.insertUserManga(anime)
    }



}