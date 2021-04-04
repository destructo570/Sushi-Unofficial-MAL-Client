package com.destructo.sushi.ui.anime.animeDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.params.AnimeUpdateParams
import com.destructo.sushi.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel
@Inject
constructor(
    private val animeDetailsRepo: AnimeDetailRepository
) : ViewModel() {

    val animeDetail: LiveData<Resource<Anime>> = animeDetailsRepo.animeDetail

    val animeCharacterAndStaff: LiveData<Resource<AnimeCharacterAndStaff>> = animeDetailsRepo.animeCharacterAndStaff

    val animeVideosAndEpisodes: LiveData<Resource<AnimeVideo>> = animeDetailsRepo.animeVideosAndEpisodes

    val animeReview: LiveData<Resource<AnimeReviews>> = animeDetailsRepo.animeReview

    val userAnimeStatus: LiveData<Resource<UpdateUserAnime>> = animeDetailsRepo.userAnimeStatus

    val userAnimeRemove: LiveData<Resource<Unit>> = animeDetailsRepo.userAnimeRemove

    fun getAnimeDetail(malId: Int, isEdited: Boolean) {
        viewModelScope.launch{ animeDetailsRepo.getAnimeDetail(malId, isEdited) }
    }

    fun getAnimeCharacters(malId: Int) {
        viewModelScope.launch{ animeDetailsRepo.getAnimeCharacters(malId) }
    }

    fun getAnimeVideos(malId: Int) {
        viewModelScope.launch{ animeDetailsRepo.getAnimeVideos(malId) }
    }

    fun getAnimeReviews(malId: Int, page: String ) {
        viewModelScope.launch{ animeDetailsRepo.getAnimeReviews(malId, page) }
    }

    fun updateUserAnimeStatus(updateParam: AnimeUpdateParams){
        viewModelScope.launch{ animeDetailsRepo.updateAnimeUserList(updateParam) }
    }

    fun removeAnime(animeId:Int){
        viewModelScope.launch { animeDetailsRepo.removeAnimeFromList(animeId.toString()) }
    }





}