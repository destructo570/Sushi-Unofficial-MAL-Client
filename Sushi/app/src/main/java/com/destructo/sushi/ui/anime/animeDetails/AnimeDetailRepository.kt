package com.destructo.sushi.ui.anime.animeDetails

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
}