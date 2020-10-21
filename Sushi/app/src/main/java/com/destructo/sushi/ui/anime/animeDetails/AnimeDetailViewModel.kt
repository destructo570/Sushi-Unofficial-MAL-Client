package com.destructo.sushi.ui.anime.animeDetails

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class AnimeDetailViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    val jikanApi: JikanApi,
    val malApi: MalApi
) : ViewModel() {

    private val _animeDetail: MutableLiveData<Anime> = MutableLiveData()
    val animeDetail: LiveData<Anime>
        get() = _animeDetail

    private val _animeCharacterAndStaff: MutableLiveData<AnimeCharacterAndStaff> = MutableLiveData()
    val animeCharacterAndStaff: LiveData<AnimeCharacterAndStaff>
        get() = _animeCharacterAndStaff


    private val _animeVideosAndEpisodes: MutableLiveData<AnimeVideo> = MutableLiveData()
    val animeVideosAndEpisodes: LiveData<AnimeVideo>
        get() = _animeVideosAndEpisodes

    private val _animeReview: MutableLiveData<AnimeReviews> = MutableLiveData()
    val animeReview: LiveData<AnimeReviews>
        get() = _animeReview


    fun getAnimeDetail(malId: Int) {
        viewModelScope.launch {
            val animeId: String = malId.toString()
            val getAnimeByIdDeferred = malApi.getAnimeByIdAsync(animeId, ALL_ANIME_FIELDS)
            try {
                val animeDetails = getAnimeByIdDeferred.await()
                _animeDetail.value = animeDetails

            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }

        }
    }

    fun getAnimeCharacters(malId: Int) {
        viewModelScope.launch {
            val animeId: String = malId.toString()
            val getAnimeCharactersDeferred = jikanApi.getCharacterAndStaffAsync(animeId)
            try {
                val animeCharactersAndStaff = getAnimeCharactersDeferred.await()
                _animeCharacterAndStaff.value = animeCharactersAndStaff

            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }

        }
    }


    fun getAnimeVideos(malId: Int) {
        viewModelScope.launch {
            val animeId: String = malId.toString()
            val getAnimeVideosDeferred = jikanApi.getAnimeVideosAsync(animeId)
            try {
                val animeVideosAndEpisodes = getAnimeVideosDeferred.await()
                _animeVideosAndEpisodes.value = animeVideosAndEpisodes

            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }

        }
    }


    fun getAnimeReviews(malId: Int) {
        viewModelScope.launch {
            val animeId: String = malId.toString()
            val getAnimeReviewsDeferred = jikanApi.getAnimeReviewsAsync(animeId)
            try {
                val animeReviews = getAnimeReviewsDeferred.await()
                _animeReview.value = animeReviews

            } catch (e: Exception) {
                Timber.e("Error: %s", e.message)
            }

        }
    }


}