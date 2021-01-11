package com.destructo.sushi.ui.anime

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRecom.SuggestedAnime
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimeRepository
@Inject
constructor(
    val malApi: MalApi
) {

    fun getTopAnime(
        ranking_type: String,
        offset: String?,
        limit: String?,
        nsfw: Boolean
    ): MutableLiveData<Resource<AnimeRanking>> {

        val result = MutableLiveData<Resource<AnimeRanking>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val topAnimeDeferred = malApi.getAnimeRankingAsync(
                ranking_type, limit, offset,
                BASIC_ANIME_FIELDS,
                nsfw
            )
            try {
                val topAnimeList = topAnimeDeferred.await()
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(topAnimeList)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.value = Resource.error(e.message ?: "", null)
                }
            }
        }
        return result
    }

    fun getSeasonalAnime(
        year: String,
        season: String,
        sort: String?,
        limit: String?,
        offset: String?
    ): MutableLiveData<Resource<SeasonalAnime>> {

        val result = MutableLiveData<Resource<SeasonalAnime>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val seasonalAnimeDeferred = malApi
                .getSeasonalAnimeAsync(year, season, sort, limit, offset, BASIC_ANIME_FIELDS)
            try {
                val seasonaAnime = seasonalAnimeDeferred.await()
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(seasonaAnime)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.value = Resource.error(e.message ?: "", null)
                }
            }
        }
        return result
    }


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