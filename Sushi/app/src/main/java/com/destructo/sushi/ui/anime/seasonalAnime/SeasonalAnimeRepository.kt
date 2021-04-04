package com.destructo.sushi.ui.anime.seasonalAnime

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.jikan.season.SeasonArchive
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.SeasonAnimeDao
import javax.inject.Inject

class SeasonalAnimeRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val jikanApi: JikanApi,
    private val seasonAnimeDao: SeasonAnimeDao
) {

    var seasonalAnime: MutableLiveData<Resource<SeasonalAnime>> = MutableLiveData()
    var seasonArchive: MutableLiveData<Resource<SeasonArchive>> = MutableLiveData()
    var seasonalNextPage: MutableLiveData<Resource<SeasonalAnime>> = MutableLiveData()

    private var nextPage: String? = ""

    suspend fun getSeasonArchive() {
        seasonArchive.value = Resource.loading(null)
        try {
            val response = jikanApi.getSeasonArchiveAsync()
            seasonArchive.value = Resource.success(response)
        } catch (e: Exception) {
            seasonArchive.value = Resource.error(e.message ?: "", null)
        }
    }

    suspend fun getSeasonalAnime(
        year: String, season: String, sort: String?,
        limit: String?, offset: String?
    ) {

        seasonalAnime.value = Resource.loading(null)
        seasonalAnimeCall(
            year = year,
            season = season,
            sort = sort,
            limit = limit,
            offset = offset
        )
    }

    suspend fun getSeasonAnimeNext() {

        if (!nextPage.isNullOrBlank()) {
            seasonalNextPage.value = Resource.loading(null)
            nextPageCall(
                next = nextPage!!,
            )

        }
    }

    private suspend fun nextPageCall(next: String) {
        try {
            val getSeasonalAnimeDeferred = malApi.getSeasonalAnimeNextAsync(next)
            val response = getSeasonalAnimeDeferred.await()
            nextPage = response.paging?.next
            seasonAnimeDao.insertSeasonAnimeList(response.data!!)
            seasonalNextPage.value = Resource.success(response)

        } catch (e: java.lang.Exception) {
            seasonalNextPage.value = Resource.error(e.message ?: "", null)

        }

    }

    private suspend fun seasonalAnimeCall(
        year: String, season: String, sort: String?,
        limit: String?, offset: String?
    ) {
        try {
            val response = malApi
                .getSeasonalAnimeAsync(year, season, sort, limit, offset, ALL_ANIME_FIELDS)
            nextPage = response.paging?.next
            seasonAnimeDao.insertSeasonAnimeList(response.data!!)
            seasonalAnime.value = Resource.success(response)
        } catch (e: Exception) {
            seasonalAnime.value = Resource.error(e.message ?: "", null)
        }
    }

}