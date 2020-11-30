package com.destructo.sushi.ui.manga.mangaDetails

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.model.database.MangaCharacterListEntity
import com.destructo.sushi.model.database.MangaDetailsEntity
import com.destructo.sushi.model.database.MangaReviewsEntity
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.MangaCharacterListDao
import com.destructo.sushi.room.MangaDetailsDao
import com.destructo.sushi.room.MangaReviewListDao
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
    private val mangaReviewListDao: MangaReviewListDao
){

    var mangaDetail: MutableLiveData<Resource<Manga>> = MutableLiveData()

    var mangaCharacter: MutableLiveData<Resource<MangaCharacter>> = MutableLiveData()

    var mangaReview: MutableLiveData<Resource<MangaReview>> = MutableLiveData()

    var userMangaStatus: MutableLiveData<Resource<UpdateUserManga>> = MutableLiveData()

    var userMangaRemove: MutableLiveData<Resource<Unit>> = MutableLiveData()



    fun getMangaDetail(malId: Int, isEdited: Boolean) {
        mangaDetail.value = Resource.loading(null)

        GlobalScope.launch {
            val mangaDetailCache = mangaDetailsDao.getMangaDetailsById(malId)

            if (mangaDetailCache != null){

                if((System.currentTimeMillis() - mangaDetailCache.time) > 20000
                    || isEdited) {
                    mangaDetailsCall(malId)
                }else{
                    val mangaDetails = mangaDetailCache.manga
                    withContext(Dispatchers.Main) {
                        mangaDetail.value = Resource.success(mangaDetails)
                    }
                }
            }else{
                mangaDetailsCall(malId)
            }
        }
    }

    fun getMangaCharacters(malId: Int) {
        mangaCharacter.value = Resource.loading(null)

        GlobalScope.launch {
            val mangaCharacterListCache = mangaCharacterListDao.getMangaCharacterListById(malId)

            if (mangaCharacterListCache != null){

                if((System.currentTimeMillis() - mangaCharacterListCache.time) > 20000) {
                    mangaCharactersCall(malId)
                }else{
                    val mangaCharacterList = mangaCharacterListCache.mangaCharacterList
                    withContext(Dispatchers.Main) {
                        mangaCharacter.value = Resource.success(mangaCharacterList)
                    }
                }
            }else{
                mangaCharactersCall(malId)
            }
        }
    }

    fun getMangaReviews(malId: Int) {
        mangaReview.value = Resource.loading(null)

        GlobalScope.launch {
            val mangaReviewListCache = mangaReviewListDao.getMangaReviewsById(malId)

            if (mangaReviewListCache != null){

                if((System.currentTimeMillis() - mangaReviewListCache.time) > 20000) {
                    mangaReviewsCall(malId)
                }else{
                    val mangaCharacterList = mangaReviewListCache.reviewList
                    withContext(Dispatchers.Main) {
                        mangaReview.value = Resource.success(mangaCharacterList)
                    }
                }
            }else{
                mangaReviewsCall(malId)
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

    fun removeMangaFromList(mangaId: String){

        GlobalScope.launch {
            try {
                malApi.deleteMangaFromList(mangaId).await()
                withContext(Dispatchers.Main){
                    userMangaRemove.value = Resource.success(Unit)
                }
            }catch (e: java.lang.Exception){
                withContext(Dispatchers.Main){
                    userMangaRemove.value = Resource.error(e.message ?: "", null)
                    Timber.e("Error: %s",e.message)
                }
            }
        }

    }

    private suspend fun mangaDetailsCall(malId:Int) {

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
    private suspend fun mangaCharactersCall(malId:Int) {
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
    private suspend fun mangaReviewsCall(malId:Int) {
        val mangaId: String = malId.toString()
        val getMangaReviewsDeferred = jikanApi.getMangaReviewsAsync(mangaId)
        try {
            val mangaCharacterList = getMangaReviewsDeferred.await()
            val mangaReviewRequest = MangaReviewsEntity(
                reviewList = mangaCharacterList,
                id = malId,
                time = System.currentTimeMillis()
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

    }