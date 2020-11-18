package com.destructo.sushi.ui.anime.animeDetails

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.database.AnimeCharacterListEntity
import com.destructo.sushi.model.database.AnimeDetailEntity
import com.destructo.sushi.model.database.AnimeReviewsEntity
import com.destructo.sushi.model.database.AnimeVideosEntity
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeCharacterListDao
import com.destructo.sushi.room.AnimeDetailsDao
import com.destructo.sushi.room.AnimeReviewListDao
import com.destructo.sushi.room.AnimeVideoListDao
import kotlinx.coroutines.*
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
    private val animeReviewsDao: AnimeReviewListDao

) {

    var animeDetail: MutableLiveData<Resource<Anime>> = MutableLiveData()

    var animeCharacterAndStaff: MutableLiveData<Resource<AnimeCharacterAndStaff>> =
        MutableLiveData()

    var animeVideosAndEpisodes: MutableLiveData<Resource<AnimeVideo>> = MutableLiveData()

    var animeReview: MutableLiveData<Resource<AnimeReviews>> = MutableLiveData()

    var userAnimeStatus: MutableLiveData<Resource<UpdateUserAnime>> = MutableLiveData()

    var userAnimeRemove: MutableLiveData<Resource<Unit>> = MutableLiveData()

    fun getAnimeDetail(malId: Int, isEdited: Boolean) {
        animeDetail.value = Resource.loading(null)

        GlobalScope.launch {
            val animeDetailCache = animeDetailsDao.getAnimeDetailsById(malId)

            if (animeDetailCache != null){

                    if((System.currentTimeMillis() - animeDetailCache.time) > 20000
                        || isEdited) {
                        animeDetailCall(malId)
                    }else{
                        val mAnime = animeDetailCache.anime
                        withContext(Dispatchers.Main) {
                        animeDetail.value = Resource.success(mAnime)
                        }
                    }
            }else{
                animeDetailCall(malId)
            }
        }
    }

    fun getAnimeCharacters(malId: Int) {
        animeCharacterAndStaff.value = Resource.loading(null)
        GlobalScope.launch {
            val animeCharacterListCache = animeCharacterListDao.getAnimeCharactersById(malId)

            if (animeCharacterListCache != null){

                if((System.currentTimeMillis() - animeCharacterListCache.time) > 30000) {
                    animeCharacterCall(malId)
                }else{
                    val mAnime = animeCharacterListCache.characterAndStaffList
                    withContext(Dispatchers.Main) {
                        animeCharacterAndStaff.value = Resource.success(mAnime)
                    }
                }
            }else{
                animeCharacterCall(malId)
            }

        }
    }


    fun getAnimeVideos(malId: Int) {
        animeVideosAndEpisodes.value = Resource.loading(null)
        GlobalScope.launch {
            val animeVideosListCache = animeVideosDao.getAnimeVideosById(malId)

            if (animeVideosListCache != null){

                if((System.currentTimeMillis() - animeVideosListCache.time) > 30000) {
                    animeVideoCall(malId)
                }else{
                    val mAnime = animeVideosListCache.videosAndEpisodes
                    withContext(Dispatchers.Main) {
                        animeVideosAndEpisodes.value = Resource.success(mAnime)
                    }
                }
            }else{
                animeVideoCall(malId)
            }
        }
    }


    fun getAnimeReviews(malId: Int) {
        animeReview.value = Resource.loading(null)
        GlobalScope.launch {
            val animeReviewListCache = animeReviewsDao.getAnimeReviewsById(malId)

            if (animeReviewListCache != null){

                if((System.currentTimeMillis() - animeReviewListCache.time) > 30000) {
                    animeReviewCall(malId)
                }else{
                    val mAnime = animeReviewListCache.reviewList
                    withContext(Dispatchers.Main) {
                        animeReview.value = Resource.success(mAnime)
                    }
                }
            }else{
                animeReviewCall(malId)
            }
        }
    }


    fun updateAnimeUserList(animeId:String, status:String?=null,
                            is_rewatching:Boolean?=null, score:Int?=null,
                            num_watched_episodes:Int?=null, priority:Int?=null,
                            num_times_rewatched:Int?=null, rewatch_value:Int?=null,
                            tags:List<String>?=null, comments:String?=null) {
        userAnimeStatus.value = Resource.loading(null)

        GlobalScope.launch {
            val addEpisodeDeferred = malApi.updateUserAnime(animeId,
                status,is_rewatching,score,num_watched_episodes,
                priority,num_times_rewatched,rewatch_value,tags,comments)
            try {
                val animeStatus = addEpisodeDeferred.await()
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.success(animeStatus)
                }
            }catch (e: java.lang.Exception){
                withContext(Dispatchers.Main){
                    userAnimeStatus.value = Resource.error(e.message ?: "", null)
                }
            }
        }
    }


    fun removeAnimeFromList(animeId: String){

        GlobalScope.launch {
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

    }

    suspend fun animeDetailCall(malId:Int){

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
                id = malId
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

    suspend fun animeReviewCall(malId:Int) {
        val animeId: String = malId.toString()
        val getAnimeReviewsDeferred = jikanApi.getAnimeReviewsAsync(animeId)
        try {
            val animeReviews = getAnimeReviewsDeferred.await()
            val animeReviewListEntity = AnimeReviewsEntity(
                reviewList = animeReviews,
                time = System.currentTimeMillis(),
                id = malId
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


    }