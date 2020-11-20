package com.destructo.sushi.ui.anime.topAnime

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class TopAnimeRepository
@Inject
constructor(val malApi: MalApi) {

    var topAnimeListNextPage: MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()

    var animeRankingList: MutableLiveData<Resource<MutableList<AnimeRankingData>>> =
        MutableLiveData()

    var nextPage: String? = null


    fun getTopAnimeNext() {

        if (!nextPage.isNullOrBlank()) {
            topAnimeListNextPage.value = Resource.loading(null)
            GlobalScope.launch {
                try {
                    val getTopAnimeDeferred = malApi.getAnimeRankingNext(nextPage!!)
                    val animeRanking = getTopAnimeDeferred.await()
                    withContext(Dispatchers.Main) {
                        val animeList = animeRanking.data as MutableList<AnimeRankingData>
                        Timber.e("$animeList")
                        topAnimeListNextPage.value = Resource.success(animeRanking)
                        animeRankingList.value?.data?.addAll(animeList)
                        nextPage = animeRanking.paging?.next.toString()

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        topAnimeListNextPage.value = Resource.error(e.message ?: "", null)
                    }
                }
            }
        }
    }

    fun getAnimeRankingList(ranking_type: String, offset: String?, limit: String?) {

        animeRankingList.value = Resource.loading(null)
        GlobalScope.launch {
            try {
                val getTopAnimeDeferred = malApi.getAnimeRankingAsync(
                    ranking_type, limit, offset,
                    BASIC_ANIME_FIELDS
                )
                val animeRanking = getTopAnimeDeferred.await()
                withContext(Dispatchers.Main) {
                    val animeList = animeRanking.data as MutableList<AnimeRankingData>
                    animeRankingList.value = Resource.success(animeList)
                    nextPage = animeRanking.paging?.next.toString()

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    animeRankingList.value = Resource.error(e.message ?: "", null)
                }
            }
        }
    }
}