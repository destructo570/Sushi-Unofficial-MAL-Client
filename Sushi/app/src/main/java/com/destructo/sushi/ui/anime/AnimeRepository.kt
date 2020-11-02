package com.destructo.sushi.ui.anime

import android.provider.Contacts
import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AnimeRepository
@Inject
constructor(
    val malApi: MalApi
) {

    fun getTopAnime(
        ranking_type: String,
        offset: String?,
        limit: String?
    ): MutableLiveData<Resource<AnimeRanking>> {

        val result = MutableLiveData<Resource<AnimeRanking>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val topAnimeDeferred = malApi.getAnimeRankingAsync(
                ranking_type, limit, offset,
                BASIC_ANIME_FIELDS
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


}