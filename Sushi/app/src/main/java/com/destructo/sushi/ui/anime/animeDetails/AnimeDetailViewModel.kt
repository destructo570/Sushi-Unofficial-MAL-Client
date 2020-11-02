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
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class AnimeDetailViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val animeDetailsRepo: AnimeDetailRepository
) : ViewModel() {

    val animeDetail: LiveData<Resource<Anime>> = animeDetailsRepo.animeDetail

    val animeCharacterAndStaff: LiveData<Resource<AnimeCharacterAndStaff>> = animeDetailsRepo.animeCharacterAndStaff

    val animeVideosAndEpisodes: LiveData<Resource<AnimeVideo>> = animeDetailsRepo.animeVideosAndEpisodes

    val animeReview: LiveData<Resource<AnimeReviews>> = animeDetailsRepo.animeReview

    fun getAnimeDetail(malId: Int) {
        animeDetailsRepo.getAnimeDetail(malId)
    }

    fun getAnimeCharacters(malId: Int) {
        animeDetailsRepo.getAnimeCharacters(malId)
    }

    fun getAnimeVideos(malId: Int) {
        animeDetailsRepo.getAnimeVideos(malId)
    }

    fun getAnimeReviews(malId: Int) {
        animeDetailsRepo.getAnimeReviews(malId)
    }

}