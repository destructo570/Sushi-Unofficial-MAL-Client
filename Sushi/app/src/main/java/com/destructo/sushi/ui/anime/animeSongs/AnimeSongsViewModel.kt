package com.destructo.sushi.ui.anime.animeSongs


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.anime.core.Anime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeSongsViewModel
@Inject
constructor(
    val jikanApi: JikanApi,
) : ViewModel() {

    val animeDetails: MutableLiveData<Resource<Anime>> = MutableLiveData()

    val openingSongs: MutableLiveData<List<String?>> = MutableLiveData()
    val endingSongs: MutableLiveData<List<String?>> = MutableLiveData()


    fun getAnimeById(animeId: Int) {

        animeDetails.value = Resource.loading(null)

        viewModelScope.launch {
            try {
                val animeInfo = jikanApi.getAnimeInfoAsync(animeId.toString())

                animeDetails.value = Resource.success(animeInfo)
                openingSongs.value = animeInfo.openingThemes
                endingSongs.value = animeInfo.endingThemes

            } catch (e: Exception) {
                animeDetails.value = Resource.error(e.message ?: "", null)
            }

        }
    }
}