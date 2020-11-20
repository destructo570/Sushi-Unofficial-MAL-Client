package com.destructo.sushi.ui.anime.topAnime

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeRankingDao
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class TopAnimeRepository
@Inject
constructor(
    val malApi: MalApi,
    val animeRankingDao: AnimeRankingDao
) {

    var topAnimeListNextPage: MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()

    var animeRankingList: MutableLiveData<Resource<MutableList<AnimeRankingData?>>> =
        MutableLiveData()

    var nextPage: NextPage? = null


    fun getTopAnimeNext() {

        if (nextPage != null) {
            topAnimeListNextPage.value = Resource.loading(null)
            GlobalScope.launch {
                if (!nextPage!!.next.isNullOrBlank()  && !nextPage!!.ranking_type.isNullOrBlank() ){
                    nextPageCall(
                        next = nextPage!!.next,
                        ranking_type = nextPage!!.ranking_type)
                }
            }
        }
    }

    suspend fun nextPageCall(next: String, ranking_type: String) {
        try {
            val getTopAnimeDeferred = malApi.getAnimeRankingNext(next)
            val animeRanking = getTopAnimeDeferred.await()
            val animeList = animeRanking.data

            animeList?.forEach { it ->
                it?.let { animeRankingData ->
                    animeRankingData.ranking_type = ranking_type
                    animeRankingDao.insertAnimeRanking(animeRankingData)
                }
            }
            withContext(Dispatchers.Main) {
                topAnimeListNextPage.value = Resource.success(animeRanking)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                topAnimeListNextPage.value = Resource.error(e.message ?: "", null)
            }
        }

    }

    fun getAnimeRankingList(ranking_type: String, offset: String?, limit: String?) {

        animeRankingList.value = Resource.loading(null)
        GlobalScope.launch {
            animeRankingCall(
                ranking_type = ranking_type,
                offset = offset,
                limit = limit
            )
        }
    }

    suspend fun animeRankingCall(ranking_type: String, offset: String?, limit: String?){
        try {
            val getTopAnimeDeferred = malApi.getAnimeRankingAsync(
                ranking_type, limit, offset,
                BASIC_ANIME_FIELDS
        )
            val animeRanking = getTopAnimeDeferred.await()
            val animeList = animeRanking.data

            animeList?.forEach { it ->
                it?.let { animeRankingData ->
                    animeRankingData.ranking_type = ranking_type
                    animeRankingDao.insertAnimeRanking(animeRankingData)
                }
            }

        withContext(Dispatchers.Main) {

            animeRankingList.value = Resource.success(animeList?.toMutableList())

        }
    } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                animeRankingList.value = Resource.error(e.message ?: "", null)
            }
        }
    }
}

data class NextPage(
    val next: String = "",
    val ranking_type: String = ""
)