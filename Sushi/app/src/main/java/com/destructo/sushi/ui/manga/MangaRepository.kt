package com.destructo.sushi.ui.manga

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_MANGA_FIELDS
import com.destructo.sushi.enum.mal.MangaRankingType
import com.destructo.sushi.model.mal.mangaRanking.MangaRanking
import com.destructo.sushi.model.mal.mangaRanking.MangaRankingData
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.MangaRankingDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class MangaRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val MangaRankingDao: MangaRankingDao
) {
    var rankingType: String = MangaRankingType.ALL.value

    var topMangaListNextPage: MutableLiveData<Resource<MangaRanking>> = MutableLiveData()

    var mangaRankingList: MutableLiveData<Resource<MutableList<MangaRankingData?>>> =
        MutableLiveData()

    private var nextPage: String? = null

    fun getTopMangaNext() {

        if (!nextPage.isNullOrBlank()) {
            topMangaListNextPage.value = Resource.loading(null)
            GlobalScope.launch {
                nextPageCall(
                    next = nextPage!!,
                )
            }
        }
    }

    private suspend fun nextPageCall(next: String) {
        try {
            val getTopMangaDeferred = malApi.getMangaRankingNext(next)
            val mangaRanking = getTopMangaDeferred.await()
            val mangaList = mangaRanking.data
            nextPage = mangaRanking.paging?.next
            MangaRankingDao.insertMangaRankingList(mangaList!!)
            withContext(Dispatchers.Main) {
                topMangaListNextPage.value = Resource.success(mangaRanking)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                topMangaListNextPage.value = Resource.error(e.message ?: "", null)
            }
        }

    }

    fun getMangaRankingList(offset: String?, limit: String?) {

        mangaRankingList.value = Resource.loading(null)
        GlobalScope.launch {
            mangaRankingCall(
                ranking_type = rankingType,
                offset = offset,
                limit = limit
            )
        }
    }

    private suspend fun mangaRankingCall(ranking_type: String, offset: String?, limit: String?){
        try {
            val getTopMangaDeferred = malApi.getMangaRankingAsync(
                ranking_type, limit, offset,
                BASIC_MANGA_FIELDS
            )
            val mangaRanking = getTopMangaDeferred.await()
            if ( nextPage != mangaRanking.paging?.next){
                nextPage = mangaRanking.paging?.next
            }
            val mangaList = mangaRanking.data

            MangaRankingDao.insertMangaRankingList(mangaList!!)
            withContext(Dispatchers.Main) {
                mangaRankingList.value = Resource.success(mangaList.toMutableList())
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mangaRankingList.value = Resource.error(e.message ?: "", null)
            }
        }
    }
}