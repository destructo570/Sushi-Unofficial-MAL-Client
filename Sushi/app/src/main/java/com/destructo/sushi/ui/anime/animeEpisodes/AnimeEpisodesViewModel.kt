package com.destructo.sushi.ui.anime.animeEpisodes

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.database.AnimeVideosEntity
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeVideoListDao
import kotlinx.coroutines.launch

class AnimeEpisodesViewModel
@ViewModelInject
constructor(
    @Assisted
    val savedStateHandle: SavedStateHandle,
    val jikanApi: JikanApi,
    private val animeVideoListDao: AnimeVideoListDao
) : ViewModel() {

    var animeEpisodeList: MutableLiveData<Resource<AnimeVideo>> = MutableLiveData()


    fun getAnimeVideos(malId: Int) {
        animeEpisodeList.value = Resource.loading(null)
        viewModelScope.launch {
            val animeVideosListCache = animeVideoListDao.getAnimeVideosById(malId)
            if (animeVideosListCache != null && !animeVideosListCache.isCacheExpired()) {
                animeEpisodeList.value =
                    Resource.success(animeVideosListCache.videosAndEpisodes)
            } else animeVideoCall(malId)
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
            animeVideoListDao.insertAnimeVideos(animeVideoListEntity)
            animeEpisodeList
                .value = Resource.success(animeVideoListEntity.videosAndEpisodes)

        } catch (e: Exception) {
            animeEpisodeList.value = Resource.error(e.message ?: "", null)
        }
    }

}