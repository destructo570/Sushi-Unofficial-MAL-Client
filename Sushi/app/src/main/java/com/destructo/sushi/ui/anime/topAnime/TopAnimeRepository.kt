package com.destructo.sushi.ui.anime.topAnime

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.enum.mal.AnimeRankingType
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeRankingDao
import javax.inject.Inject

class TopAnimeRepository
@Inject
constructor(
    val malApi: MalApi,
    val animeRankingDao: AnimeRankingDao
) {
    var rankingType: String = AnimeRankingType.ALL.value

    var topAnimeListNextPage: MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()

    var animeRankingList: MutableLiveData<Resource<MutableList<AnimeRankingData?>>> =
        MutableLiveData()

    private var nextPage: String? = null

    suspend fun getTopAnimeNext(nsfw: Boolean) {

        if (!nextPage.isNullOrBlank()) {
            topAnimeListNextPage.value = Resource.loading(null)
            nextPageCall(
                next = nextPage!!,
                nsfw = nsfw
            )
        }
    }

    private suspend fun nextPageCall(next: String, nsfw: Boolean) {
        try {
            val getTopAnimeDeferred = malApi.getAnimeRankingNextAsync(next, nsfw)
            val animeRanking = getTopAnimeDeferred.await()
            if (nsfw) {
                val animeList = animeRanking.data
                animeRankingDao.insertAnimeRankingList(animeList!!)
            } else {
                val animeList = animeRanking.data?.filter { it?.anime?.nsfw == "white" }
                animeRankingDao.insertAnimeRankingList(animeList!!)
            }
            nextPage = animeRanking.paging?.next
                topAnimeListNextPage.value = Resource.success(animeRanking)
        } catch (e: Exception) {
                topAnimeListNextPage.value = Resource.error(e.message ?: "", null)
        }

    }

    suspend fun getAnimeRankingList(offset: String?, limit: String?, nsfw: Boolean) {

        animeRankingList.value = Resource.loading(null)
        animeRankingCall(
            ranking_type = rankingType,
            offset = offset,
            limit = limit,
            nsfw
        )

    }

    private suspend fun animeRankingCall(
        ranking_type: String,
        offset: String?,
        limit: String?,
        nsfw: Boolean
    ) {
        try {
            val getTopAnimeDeferred = malApi.getAnimeRankingAsync(
                ranking_type, limit, offset,
                BASIC_ANIME_FIELDS,
                nsfw
            )
            val animeRanking = getTopAnimeDeferred.await()
            if (nextPage != animeRanking.paging?.next) {
                nextPage = animeRanking.paging?.next
            }
            val animeList = animeRanking.data
            animeRankingDao.insertAnimeRankingList(animeList!!)
            animeRankingList.value = Resource.success(animeList.toMutableList())

        } catch (e: Exception) {
            animeRankingList.value = Resource.error(e.message ?: "", null)
        }
    }
}