package com.destructo.sushi.ui.anime.animeRecom

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.animeRecom.SuggestedAnime
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimeRecomRepo
    @Inject
    constructor(
    val malApi: MalApi
) {

    fun getAnimeRecom(
        offset: String?,
        limit: String?,
        nsfw: Boolean
    ): MutableLiveData<Resource<List<Anime>>> {

        val result = MutableLiveData<Resource<List<Anime>>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val animeRecomDeferred = malApi
                .getAnimeRecomAsync(limit, offset, BASIC_ANIME_FIELDS, nsfw)
            try {
                val response = animeRecomDeferred.await()
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(extractAnimeList(response))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.value = Resource.error(e.message ?: "", null)
                }
            }
        }
        return result
    }


    private fun extractAnimeList(suggestedAnime: SuggestedAnime): List<Anime> {
        val animeList = mutableListOf<Anime>()
        suggestedAnime.data?.forEach { it?.anime?.let { anime ->
            animeList.add(anime) }
        }
        return animeList
    }


}