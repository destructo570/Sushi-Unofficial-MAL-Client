package com.destructo.sushi.ui.anime.animeDetails

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import timber.log.Timber
import javax.inject.Inject

class AnimeDetailRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val jikanApi: JikanApi
) {

    var animeDetail: MutableLiveData<Resource<Anime>> = MutableLiveData()

    var animeCharacterAndStaff: MutableLiveData<Resource<AnimeCharacterAndStaff>> =
        MutableLiveData()

    var animeVideosAndEpisodes: MutableLiveData<Resource<AnimeVideo>> = MutableLiveData()

    var animeReview: MutableLiveData<Resource<AnimeReviews>> = MutableLiveData()

    var userAnimeStatus: MutableLiveData<Resource<UpdateUserAnime>> = MutableLiveData()

    var userAnimeRemove: MutableLiveData<Resource<Unit>> = MutableLiveData()



    fun getAnimeDetail(malId: Int) {
        animeDetail.value = Resource.loading(null)

        GlobalScope.launch {
            val animeId: String = malId.toString()
            val getAnimeByIdDeferred = malApi.getAnimeByIdAsync(animeId, ALL_ANIME_FIELDS)
            try {
                val animeDetails = getAnimeByIdDeferred.await()
                withContext(Dispatchers.Main) {
                    animeDetail.value = Resource.success(animeDetails)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    animeDetail.value = Resource.error(e.message ?: "", null)

                }
            }
        }
    }

    fun getAnimeCharacters(malId: Int) {
        animeCharacterAndStaff.value = Resource.loading(null)
        GlobalScope.launch {
            val animeId: String = malId.toString()
            val getAnimeCharactersDeferred = jikanApi.getCharacterAndStaffAsync(animeId)
            try {
                val animeCharactersAndStaff = getAnimeCharactersDeferred.await()
                withContext(Dispatchers.Main) {
                    animeCharacterAndStaff.value = Resource.success(animeCharactersAndStaff)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    animeCharacterAndStaff.value = Resource.error(e.message ?: "", null)
                }
            }
        }
    }


    fun getAnimeVideos(malId: Int) {
        animeVideosAndEpisodes.value = Resource.loading(null)
        GlobalScope.launch {
            val animeId: String = malId.toString()
            val getAnimeVideosDeferred = jikanApi.getAnimeVideosAsync(animeId)
            try {
                val animeVideosList = getAnimeVideosDeferred.await()
                withContext(Dispatchers.Main) {
                    animeVideosAndEpisodes.value = Resource.success(animeVideosList)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    animeVideosAndEpisodes.value = Resource.error(e.message ?: "", null)
                }
            }
        }
    }


    fun getAnimeReviews(malId: Int) {
        animeReview.value = Resource.loading(null)
        GlobalScope.launch {
            val animeId: String = malId.toString()
            val getAnimeReviewsDeferred = jikanApi.getAnimeReviewsAsync(animeId)
            try {
                val animeReviews = getAnimeReviewsDeferred.await()
                withContext(Dispatchers.Main) {
                    animeReview.value = Resource.success(animeReviews)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    animeReview.value = Resource.error(e.message ?: "", null)
                }
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

}