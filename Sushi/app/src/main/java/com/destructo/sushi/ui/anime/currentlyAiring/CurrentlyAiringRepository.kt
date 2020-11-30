package com.destructo.sushi.ui.anime.currentlyAiring

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.NSFW_WHITE
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
import javax.inject.Inject

class CurrentlyAiringRepository
@Inject
constructor(val malApi: MalApi,
            private val animeRankingDao: AnimeRankingDao
) {
    private var rankingType: String = AnimeRankingType.AIRING.value

    var airingAnimeListNextPage: MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()

    var airingAnimeList: MutableLiveData<Resource<MutableList<AnimeRankingData?>>> =
        MutableLiveData()

    private var nextPage: String? = null

    fun getTopAnimeNext(nsfw: Boolean) {
        if (!nextPage.isNullOrBlank()) {
            airingAnimeListNextPage.value = Resource.loading(null)
            GlobalScope.launch {
                nextPageCall(
                    next = nextPage!!,
                    nsfw = nsfw
                )
            }
        }
    }

    fun getAnimeRankingList(offset: String?, limit: String?, nsfw: Boolean) {

        airingAnimeList.value = Resource.loading(null)
        GlobalScope.launch {
            animeRankingCall(
                offset = offset,
                limit = limit,
                nsfw = nsfw
            )
        }
    }

    private suspend fun nextPageCall(next: String, nsfw: Boolean) {
        try {
            val getTopAnimeDeferred = malApi.getAnimeRankingNextAsync(next, nsfw)
            val animeRanking = getTopAnimeDeferred.await()
            if (nsfw) {
                animeRankingDao.insertAnimeRankingList(animeRanking.data!!)
            }else{
                val animeList = animeRanking.data?.filter { it?.anime?.nsfw == NSFW_WHITE }
                animeRankingDao.insertAnimeRankingList(animeList!!)
            }
            nextPage = animeRanking.paging?.next
            withContext(Dispatchers.Main) {
                airingAnimeListNextPage.value = Resource.success(animeRanking)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                airingAnimeListNextPage.value = Resource.error(e.message ?: "", null)
            }
        }

    }

    private suspend fun animeRankingCall(offset: String?, limit: String?, nsfw: Boolean){
        try {
            val getTopAnimeDeferred = malApi.getAnimeRankingAsync(
                ranking_type = rankingType,
                limit = limit,
                offset = offset,
                fields = BASIC_ANIME_FIELDS,
                nsfw = nsfw
            )
            val animeRanking = getTopAnimeDeferred.await()
            if ( nextPage != animeRanking.paging?.next){
                nextPage = animeRanking.paging?.next
            }

            animeRankingDao.insertAnimeRankingList(animeRanking.data!!)
            withContext(Dispatchers.Main) {
                airingAnimeList.value = Resource.success(animeRanking.data.toMutableList())
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                airingAnimeList.value = Resource.error(e.message ?: "", null)
            }
        }
    }
}