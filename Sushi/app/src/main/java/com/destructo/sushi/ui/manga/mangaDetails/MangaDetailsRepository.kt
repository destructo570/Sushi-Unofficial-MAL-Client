package com.destructo.sushi.ui.manga.mangaDetails

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.DEFAULT_USER_LIST_PAGE_LIMIT
import com.destructo.sushi.enum.mal.UserMangaSort
import com.destructo.sushi.model.database.MangaCharacterListEntity
import com.destructo.sushi.model.database.MangaDetailsEntity
import com.destructo.sushi.model.database.MangaReviewsEntity
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.model.params.MangaUpdateParams
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.MangaCharacterListDao
import com.destructo.sushi.room.MangaDetailsDao
import com.destructo.sushi.room.MangaReviewListDao
import com.destructo.sushi.room.UserMangaListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    private val userMangaListDao: UserMangaListDao
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


    suspend fun updateUserMangaList(mangaUpdateParams: MangaUpdateParams) {
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
                updateUserMangaList(mangaUpdateParams.mangaId.toInt())
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


    private fun updateUserMangaList(mangaId: Int) {
        val manga = userMangaListDao.getUserMangaById(mangaId)
        manga.status?.let {
            loadPage(it, manga.offset, mangaId)
        }
    }


    private fun loadPage(
        mangaStatus: String,
        offset: String?,
        mangaId: Int
    ) {
        if (!offset.isNullOrBlank()) {
            GlobalScope.launch {
                val getUserMangaDeferred = malApi.getUserMangaListAsync(
                    "@me", DEFAULT_USER_LIST_PAGE_LIMIT,
                    mangaStatus, UserMangaSort.MANGA_TITLE.value, offset, ALL_MANGA_FIELDS,
                true)
                try {
                    val userManga = getUserMangaDeferred.await()
                    val userMangaList = userManga.data
                    setUserMangaData(userManga)
                    userMangaListDao.deleteUserMangaById(mangaId)
                    userMangaListDao.insertUseMangaList(userMangaList!!)

                } catch (e: java.lang.Exception) {
                    withContext(Dispatchers.Main) {
                    }
                }
            }
        }
    }


    private fun calcOffset(nextPage: String?, prevPage: String?): String {
        var currentOffset = "0"
        if (!nextPage.isNullOrBlank()) {
            val nextOffset = getOffset(nextPage)
            if (!nextOffset.isNullOrBlank()) {
                val temp = nextOffset.toInt().minus(DEFAULT_USER_LIST_PAGE_LIMIT.toInt())
                if (temp >= 0) {
                    currentOffset = temp.toString()
                }
            }
            return currentOffset
        } else {
            val prevOffset = getOffset(prevPage)
            if (!prevOffset.isNullOrBlank()) {
                val temp = prevOffset.toInt().plus(DEFAULT_USER_LIST_PAGE_LIMIT.toInt())
                if (temp >= 0) {
                    currentOffset = temp.toString()
                }
            }
            return currentOffset
        }

    }

    private fun getOffset(url: String?): String? {

        return if (!url.isNullOrBlank()) {
            val uri = url.toUri()
            uri.getQueryParameter("offset").toString()
        } else {
            null
        }
    }

    private fun setUserMangaData(userAnime: UserMangaList) {
        val userMangaList = userAnime.data
        if (userMangaList != null) {
            for (manga in userMangaList) {
                manga?.status = manga?.manga?.myMangaListStatus?.status
                manga?.title = manga?.manga?.title
                manga?.mangaId = manga?.manga?.id
                val next = userAnime.paging?.next
                val prev = userAnime.paging?.previous
                manga?.offset = calcOffset(next, prev)
            }
        }
    }

}