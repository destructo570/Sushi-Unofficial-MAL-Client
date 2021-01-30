package com.destructo.sushi.ui.anime.animeSongs

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.anime.core.Anime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimeSongsViewModel
@ViewModelInject
constructor(
    val jikanApi: JikanApi,
): ViewModel(){

    val animeDetails: MutableLiveData<Resource<Anime>> = MutableLiveData()

    val openingSongs: MutableLiveData<List<String?>> = MutableLiveData()
    val endingSongs: MutableLiveData<List<String?>> = MutableLiveData()



    fun getAnimeById(animeId: Int){

        animeDetails.value = Resource.loading(null)

        viewModelScope.launch {
            val getAnimeInfoDeferred = jikanApi.getAnimeInfoAsync(animeId.toString())
            try {
                val animeInfo = getAnimeInfoDeferred.await()

                withContext(Dispatchers.Main) {
                    animeDetails.value = Resource.success(animeInfo)
                    openingSongs.value = animeInfo.openingThemes
                    endingSongs.value = animeInfo.endingThemes
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    animeDetails.value = Resource.error(e.message ?: "", null)
                }
            }

        }
    }
}