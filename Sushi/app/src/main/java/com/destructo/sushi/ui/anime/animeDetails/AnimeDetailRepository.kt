package com.destructo.sushi.ui.anime.animeDetails

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.DEFAULT_USER_LIST_PAGE_LIMIT
import com.destructo.sushi.model.database.AnimeCharacterListEntity
import com.destructo.sushi.model.database.AnimeDetailEntity
import com.destructo.sushi.model.database.AnimeReviewsEntity
import com.destructo.sushi.model.database.AnimeVideosEntity
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.model.params.AnimeUpdateParams
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
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
    private val userAnimeListDao: UserAnimeListDao

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

       if(animeDetailCache != null && !animeDetailCache.isCacheExpired() && !isEdited){
            withContext(Dispatchers.Main) {
                animeDetail.value = Resource.success(animeDetailCache.anime) }
        }else animeDetailCall(malId)

    }

    suspend fun getAnimeCharacters(malId: Int) {
        animeCharacterAndStaff.value = Resource.loading(null)
            val animeCharacterListCache = animeCharacterListDao.getAnimeCharactersById(malId)

            if (animeCharacterListCache != null && !animeCharacterListCache.isCacheExpired()){
                withContext(Dispatchers.Main) {
                    animeCharacterAndStaff.value = Resource.success(animeCharacterListCache.characterAndStaffList) }
            }else{
                animeCharacterCall(malId)
        }
    }


    suspend fun getAnimeVideos(malId: Int) {
        animeVideosAndEpisodes.value = Resource.loading(null)
            val animeVideosListCache = animeVideosDao.getAnimeVideosById(malId)

            if (animeVideosListCache != null && !animeVideosListCache.isCacheExpired() ){
                 withContext(Dispatchers.Main) {
                     animeVideosAndEpisodes.value = Resource.success(animeVideosListCache.videosAndEpisodes) }
            }else animeVideoCall(malId)
    }


    suspend fun getAnimeReviews(malId: Int, page: String) {
        animeReview.value = Resource.loading(null)
            val animeReviewListCache = animeReviewsDao.getAnimeReviewsById(malId)

            if (animeReviewListCache != null && !animeReviewListCache.isCacheExpired()){
                    withContext(Dispatchers.Main) {
                        animeReview.value = Resource.success(animeReviewListCache.reviewList) }
            }else animeReviewCall(malId, page)

    }

    suspend fun updateAnimeUserList(updateParam: AnimeUpdateParams) {
        userAnimeStatus.value = Resource.loading(null)

            val addEpisodeDeferred = malApi.updateUserAnime(
                updateParam.animeId, updateParam.status,updateParam.is_rewatching,
                updateParam.score,updateParam.num_watched_episodes, updateParam.priority,
                updateParam.num_times_rewatched, updateParam.rewatch_value,
                updateParam.tags,updateParam.comments)
            try {
                val animeStatus = addEpisodeDeferred.await()
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.success(animeStatus)
                    updateUserAnimeList(updateParam.animeId.toInt())
                }
            }catch (e: java.lang.Exception){
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.error(e.message ?: "", null)
                }
            }
    }


    suspend fun removeAnimeFromList(animeId: String){

            try {
               malApi.deleteAnimeFromList(animeId).await()
                withContext(Dispatchers.Main){
                    userAnimeRemove.value = Resource.success(Unit)
                }
            }catch (e: java.lang.Exception){
                withContext(Dispatchers.Main){
                    userAnimeRemove.value = Resource.error(e.message ?: "", null)
                    Timber.e("Error: %s",e.message)
                }
            }
    }

    private suspend fun animeDetailCall(malId:Int){

        val animeId: String = malId.toString()
        val getAnimeByIdDeferred = malApi.getAnimeByIdAsync(animeId, ALL_ANIME_FIELDS)
        try {
            val animeDetails = getAnimeByIdDeferred.await()
            val animeRequest = AnimeDetailEntity(
                anime = animeDetails,
                id = animeDetails.id!!,
                time = System.currentTimeMillis()
            )
            animeDetailsDao.insertAnimeDetails(animeRequest)
            withContext(Dispatchers.Main) {
                animeDetail.value = Resource.success(animeRequest.anime)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                animeDetail.value = Resource.error(e.message ?: "", null)
            }
        }
    }

    private suspend fun animeCharacterCall(malId:Int) {

        val animeId: String = malId.toString()
        val getAnimeCharactersDeferred = jikanApi.getCharacterAndStaffAsync(animeId)
        try {
            val animeCharactersAndStaffList = getAnimeCharactersDeferred.await()
            val animeCharacterListEntity = AnimeCharacterListEntity(
                characterAndStaffList = animeCharactersAndStaffList,
                time = System.currentTimeMillis(),
                id = malId
            )
            animeCharacterListDao.insertAnimeCharacters(animeCharacterListEntity)
            withContext(Dispatchers.Main) {
                animeCharacterAndStaff
                    .value = Resource.success(animeCharacterListEntity.characterAndStaffList)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                animeCharacterAndStaff.value = Resource.error(e.message ?: "", null)
            }
        }
    }

    private suspend fun animeVideoCall(malId:Int) {
        val animeId: String = malId.toString()
        val getAnimeVideosDeferred = jikanApi.getAnimeVideosAsync(animeId)
        try {
            val animeVideosList = getAnimeVideosDeferred.await()
            val animeVideoListEntity = AnimeVideosEntity(
                videosAndEpisodes = animeVideosList,
                time = System.currentTimeMillis(),
                id = malId,

            )
            animeVideosDao.insertAnimeVideos(animeVideoListEntity)
            withContext(Dispatchers.Main) {
                animeVideosAndEpisodes
                    .value = Resource.success(animeVideoListEntity.videosAndEpisodes)
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                animeVideosAndEpisodes.value = Resource.error(e.message ?: "", null)
            }
        }
    }

    private suspend fun animeReviewCall(malId:Int, page:String) {
        val animeId: String = malId.toString()
        val getAnimeReviewsDeferred = jikanApi.getAnimeReviewsAsync(animeId, page)
        try {
            val animeReviews = getAnimeReviewsDeferred.await()
            val animeReviewListEntity = AnimeReviewsEntity(
                reviewList = animeReviews,
                time = System.currentTimeMillis(),
                id = malId,
                currentPage = page
            )
            animeReviewsDao.insertAnimeReviews(animeReviewListEntity)
            withContext(Dispatchers.Main) {
                animeReview.value = Resource.success(animeReviewListEntity.reviewList)
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                animeReview.value = Resource.error(e.message ?: "", null)
            }
        }
    }

    private suspend fun updateUserAnimeList(animeId: Int){
        val anime = userAnimeListDao.getUserAnimeById(animeId)
        anime.status?.let {
            loadPage(it, anime.offset, animeId)
        }
    }

    private suspend fun loadPage(
        animeStatus: String,
        offset:String?,
        animeId:Int
    ) {
        if(!offset.isNullOrBlank()){
                val getUserAnimeDeferred = malApi.getUserAnimeListAsync(
                    "@me", DEFAULT_USER_LIST_PAGE_LIMIT,
                    animeStatus, null, offset,ALL_ANIME_FIELDS)
                try {
                    val userAnime = getUserAnimeDeferred.await()
                    val userAnimeList = userAnime.data
                    setUserAnimeData(userAnime)
                    userAnimeListDao.deleteUserAnimeById(animeId)
                    userAnimeListDao.insertUseAnimeList(userAnimeList!!)

                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                    }
                }
        }
    }

    private fun setUserAnimeData(userAnime: UserAnimeList){
        val userAnimeList = userAnime.data
        if (userAnimeList != null) {
            for (anime in userAnimeList){
                anime?.status = anime?.anime?.myAnimeListStatus?.status
                anime?.title =  anime?.anime?.title
                anime?.animeId = anime?.anime?.id
                val next = userAnime.paging?.next
                val prev = userAnime.paging?.previous
                anime?.offset = calcOffset(next, prev)
            }
        }
    }

    private fun calcOffset(nextPage: String?, prevPage:String?): String{
        var currentOffset = "0"
        if(!nextPage.isNullOrBlank()){
            val nextOffset = getOffset(nextPage)
            if (!nextOffset.isNullOrBlank()){
                val temp = nextOffset.toInt().minus(DEFAULT_USER_LIST_PAGE_LIMIT.toInt())
                if (temp>=0){
                    currentOffset = temp.toString()
                }
            }
            return currentOffset
        }else{
            val prevOffset = getOffset(prevPage)
            if (!prevOffset.isNullOrBlank()){
                val temp = prevOffset.toInt().plus(DEFAULT_USER_LIST_PAGE_LIMIT.toInt())
                if (temp>=0){
                    currentOffset = temp.toString()
                }
            }
            return currentOffset
        }

    }

    private fun getOffset(url: String?): String?{

        return if (!url.isNullOrBlank()){
            val uri = url.toUri()
            uri.getQueryParameter("offset").toString()
        }else{
            null
        }
    }

    }