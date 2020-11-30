package com.destructo.sushi.ui.anime.animeDetails

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.network.Resource

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

    val userAnimeStatus: LiveData<Resource<UpdateUserAnime>> = animeDetailsRepo.userAnimeStatus

    val userAnimeRemove: LiveData<Resource<Unit>> = animeDetailsRepo.userAnimeRemove

    fun getAnimeDetail(malId: Int, isEdited: Boolean) {
        animeDetailsRepo.getAnimeDetail(malId, isEdited)
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

    fun updateUserAnimeStatus(animeId:String,status:String?=null,
                              is_rewatching:Boolean?=null,score:Int?=null,
                              num_watched_episodes:Int?=null,priority:Int?=null,
                              num_times_rewatched:Int?=null, rewatch_value:Int?=null,
                              tags:List<String>?=null,comments:String?=null){

        animeDetailsRepo.updateAnimeUserList(animeId, status,is_rewatching,score,num_watched_episodes,
                              priority,num_times_rewatched,rewatch_value,tags,comments)

    }

    fun removeAnime(animeId:Int){
        animeDetailsRepo.removeAnimeFromList(animeId.toString())
    }

}