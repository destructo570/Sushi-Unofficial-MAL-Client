package com.destructo.sushi.ui.anime.animeDetails

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.model.database.AnimeCharacterListEntity
import com.destructo.sushi.model.database.AnimeDetailEntity
import com.destructo.sushi.model.database.AnimeReviewsEntity
import com.destructo.sushi.model.database.AnimeVideosEntity
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.params.AnimeUpdateParams
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.*
import javax.inject.Inject

class AnimeDetailRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val jikanApi: JikanApi,
    private val animeDetailsDao: AnimeDetailsDao,
    private val animeCharacterListDao: AnimeCharacterListDao,
    private val animeVideosDao: AnimeVideoListDao,
    private val animeReviewsDao: AnimeReviewListDao,
    private val userAnimeListDao: UserAnimeDao

) {

    var animeDetail: MutableLiveData<Resource<Anime>> = MutableLiveData()

    var animeCharacterAndStaff: MutableLiveData<Resource<AnimeCharacterAndStaff>> =
        MutableLiveData()

    var animeVideosAndEpisodes: MutableLiveData<Resource<AnimeVideo>> = MutableLiveData()

    var animeReview: MutableLiveData<Resource<AnimeReviews>> = MutableLiveData()

    var userAnimeStatus: MutableLiveData<Resource<UpdateUserAnime>> = MutableLiveData()

    var userAnimeRemove: MutableLiveData<Resource<Unit>> = MutableLiveData()

    suspend fun getAnimeDetail(malId: Int, isEdited: Boolean) {
        animeDetail.value = Resource.loading(null)
        val animeDetailCache = animeDetailsDao.getAnimeDetailsById(malId)

        if (animeDetailCache != null && !animeDetailCache.isCacheExpired() && !isEdited) {
            animeDetail.value = Resource.success(animeDetailCache.anime)
        } else animeDetailCall(malId)

    }

    suspend fun getAnimeCharacters(malId: Int) {
        animeCharacterAndStaff.value = Resource.loading(null)
        val animeCharacterListCache = animeCharacterListDao.getAnimeCharactersById(malId)

        if (animeCharacterListCache != null && !animeCharacterListCache.isCacheExpired()) {
            animeCharacterAndStaff.value =
                Resource.success(animeCharacterListCache.characterAndStaffList)
        } else {
            animeCharacterCall(malId)
        }
    }


    suspend fun getAnimeVideos(malId: Int) {
        animeVideosAndEpisodes.value = Resource.loading(null)
        val animeVideosListCache = animeVideosDao.getAnimeVideosById(malId)

        if (animeVideosListCache != null && !animeVideosListCache.isCacheExpired()) {
            animeVideosAndEpisodes.value =
                Resource.success(animeVideosListCache.videosAndEpisodes)
        } else animeVideoCall(malId)
    }


    suspend fun getAnimeReviews(malId: Int, page: String) {
        animeReview.value = Resource.loading(null)
        val animeReviewListCache = animeReviewsDao.getAnimeReviewsById(malId)

        if (animeReviewListCache != null && !animeReviewListCache.isCacheExpired()) {
            animeReview.value = Resource.success(animeReviewListCache.reviewList)
        } else animeReviewCall(malId, page)

    }

    suspend fun updateAnimeUserList(updateParam: AnimeUpdateParams) {

        if (updateParam.status == UserAnimeStatus.COMPLETED.value
            && updateParam.totalEpisodes != null
            && updateParam.totalEpisodes > 0
        ) {
            updateParam.setNumberOfWatchedEp(updateParam.totalEpisodes)
            updateUserAnimeCall(updateParam)
        } else {
            updateUserAnimeCall(updateParam)
        }

    }

    private suspend fun updateUserAnimeCall(updateParam: AnimeUpdateParams) {
        userAnimeStatus.value = Resource.loading(null)

        try {
            val animeStatus = malApi.updateUserAnime(
                updateParam.animeId, updateParam.status, updateParam.is_rewatching,
                updateParam.score, updateParam.num_watched_episodes, updateParam.priority,
                updateParam.num_times_rewatched, updateParam.rewatch_value,
                updateParam.tags, updateParam.comments, updateParam.start_date, updateParam.finish_date
            )
            userAnimeStatus.value = Resource.success(animeStatus)
            updateCachedUserAnime(updateParam.animeId, animeStatus)
        } catch (e: java.lang.Exception) {
            userAnimeStatus.value = Resource.error(e.message ?: "", null)
        }
    }

    suspend fun removeAnimeFromList(animeId: String) {

        try {
            malApi.deleteAnimeFromList(animeId)
            userAnimeRemove.value = Resource.success(Unit)
        } catch (e: java.lang.Exception) {
            userAnimeRemove.value = Resource.error(e.message ?: "", null)
        }
    }

    private suspend fun animeDetailCall(malId: Int) {
        try {
            val animeDetails = malApi.getAnimeByIdAsync(malId.toString(), ALL_ANIME_FIELDS)
            val animeRequest = AnimeDetailEntity(
                anime = animeDetails,
                id = animeDetails.id!!,
                time = System.currentTimeMillis()
            )
            animeDetailsDao.insertAnimeDetails(animeRequest)
            animeDetail.value = Resource.success(animeRequest.anime)
        } catch (e: Exception) {
            animeDetail.value = Resource.error(e.message ?: "", null)
        }
    }

    private suspend fun animeCharacterCall(malId: Int) {

        try {
            val animeCharactersAndStaffList = jikanApi.getCharacterAndStaffAsync(malId.toString())
            val animeCharacterListEntity = AnimeCharacterListEntity(
                characterAndStaffList = animeCharactersAndStaffList,
                time = System.currentTimeMillis(),
                id = malId
            )
            animeCharacterListDao.insertAnimeCharacters(animeCharacterListEntity)
            animeCharacterAndStaff
                .value = Resource.success(animeCharacterListEntity.characterAndStaffList)
        } catch (e: Exception) {
            animeCharacterAndStaff.value = Resource.error(e.message ?: "", null)
        }
    }

    private suspend fun animeVideoCall(malId: Int) {
        try {
            val animeVideosList = jikanApi.getAnimeVideosAsync(malId.toString())
            val animeVideoListEntity = AnimeVideosEntity(
                videosAndEpisodes = animeVideosList,
                time = System.currentTimeMillis(),
                id = malId,
            )
            animeVideosDao.insertAnimeVideos(animeVideoListEntity)
            animeVideosAndEpisodes
                .value = Resource.success(animeVideoListEntity.videosAndEpisodes)

        } catch (e: Exception) {
            animeVideosAndEpisodes.value = Resource.error(e.message ?: "", null)
        }
    }

    private suspend fun animeReviewCall(malId: Int, page: String) {
        try {
            val animeReviews = jikanApi.getAnimeReviewsAsync(malId.toString(), page)
            val animeReviewListEntity = AnimeReviewsEntity(
                reviewList = animeReviews,
                time = System.currentTimeMillis(),
                id = malId,
                currentPage = page
            )
            animeReviewsDao.insertAnimeReviews(animeReviewListEntity)
            animeReview.value = Resource.success(animeReviewListEntity.reviewList)
        } catch (e: Exception) {
            animeReview.value = Resource.error(e.message ?: "", null)
        }
    }

    private suspend fun updateCachedUserAnime(
        animeId: String,
        animeStatus: UpdateUserAnime
    ) {
        val anime = userAnimeListDao.getUserAnimeById(animeId.toInt())
        anime.updateUserStatus(animeStatus)
        userAnimeListDao.deleteUserAnimeById(animeId.toInt())
        userAnimeListDao.insertUserAnime(anime)
    }


}