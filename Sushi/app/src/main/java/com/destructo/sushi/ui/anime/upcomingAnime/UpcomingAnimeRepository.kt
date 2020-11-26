package com.destructo.sushi.ui.anime.upcomingAnime

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.enum.mal.AnimeRankingType
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeRankingDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class UpcomingAnimeRepository@Inject
constructor(val malApi: MalApi,
            private val animeRankingDao: AnimeRankingDao
) {
    private var rankingType: String = AnimeRankingType.UPCOMING.value

    var upcomingAnimeListNextPage: MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()

    var upcomingAnimeList: MutableLiveData<Resource<MutableList<AnimeRankingData?>>> =
        MutableLiveData()

    private var nextPage: String? = null

    fun getTopAnimeNext( nsfw: Boolean) {

        if (!nextPage.isNullOrBlank()) {
            upcomingAnimeListNextPage.value = Resource.loading(null)
            GlobalScope.launch {
                nextPageCall(
                    next = nextPage!!,
                    nsfw=nsfw
                )
            }
        }
    }

    private suspend fun nextPageCall(next: String, nsfw: Boolean) {
        try {
            val getTopAnimeDeferred = malApi.getAnimeRankingNextAsync(next, nsfw)
            val animeRanking = getTopAnimeDeferred.await()
            if (nsfw) {
                val animeList = animeRanking.data
                animeRankingDao.insertAnimeRankingList(animeList!!)
            }else{
                val animeList = animeRanking.data?.filter { it?.anime?.nsfw == "white" }
                animeRankingDao.insertAnimeRankingList(animeList!!)
            }
            nextPage = animeRanking.paging?.next
            withContext(Dispatchers.Main) {
                upcomingAnimeListNextPage.value = Resource.success(animeRanking)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                upcomingAnimeListNextPage.value = Resource.error(e.message ?: "", null)
            }
        }

    }

    fun getAnimeRankingList(offset: String?, limit: String?, nsfw: Boolean) {

        upcomingAnimeList.value = Resource.loading(null)
        GlobalScope.launch {
            animeRankingCall(
                offset = offset,
                limit = limit,
                nsfw = nsfw
            )
        }
    }

    private suspend fun animeRankingCall(offset: String?, limit: String?, nsfw: Boolean){
        try {
            val getTopAnimeDeferred = malApi.getAnimeRankingAsync(
                rankingType, limit, offset,
                BASIC_ANIME_FIELDS,
                nsfw
            )
            val animeRanking = getTopAnimeDeferred.await()
            if ( nextPage != animeRanking.paging?.next){
                nextPage = animeRanking.paging?.next
            }
            val animeList = animeRanking.data

            animeRankingDao.insertAnimeRankingList(animeList!!)
            withContext(Dispatchers.Main) {
                upcomingAnimeList.value = Resource.success(animeList.toMutableList())
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                upcomingAnimeList.value = Resource.error(e.message ?: "", null)
            }
        }
    }
}