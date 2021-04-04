package com.destructo.sushi.ui.manga.topManga

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_MANGA_FIELDS
import com.destructo.sushi.enum.mal.MangaRankingType
import com.destructo.sushi.model.mal.mangaRanking.MangaRanking
import com.destructo.sushi.model.mal.mangaRanking.MangaRankingData
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.MangaRankingDao
import javax.inject.Inject

class TopMangaRepository
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

    suspend fun getTopMangaNext(nsfw: Boolean) {

        if (!nextPage.isNullOrBlank()) {
            topMangaListNextPage.value = Resource.loading(null)
            nextPageCall(
                next = nextPage!!,
                nsfw = nsfw
            )
        }
    }

    private suspend fun nextPageCall(next: String, nsfw: Boolean) {
        try {
            val getTopMangaDeferred = malApi.getMangaRankingNextAsync(next, nsfw)
            val mangaRanking = getTopMangaDeferred.await()
            if (nsfw) {
                val mangaList = mangaRanking.data
                MangaRankingDao.insertMangaRankingList(mangaList!!)
            } else {
                val mangaList = mangaRanking.data?.filter { it?.manga?.nsfw == "white" }
                MangaRankingDao.insertMangaRankingList(mangaList!!)
            }
            nextPage = mangaRanking.paging?.next
            topMangaListNextPage.value = Resource.success(mangaRanking)
        } catch (e: Exception) {
            topMangaListNextPage.value = Resource.error(e.message ?: "", null)
        }

    }

    suspend fun getMangaRankingList(offset: String?, limit: String?, nsfw: Boolean) {

        mangaRankingList.value = Resource.loading(null)
        mangaRankingCall(
            ranking_type = rankingType,
            offset = offset,
            limit = limit,
            nsfw = nsfw
        )
    }

    private suspend fun mangaRankingCall(
        ranking_type: String,
        offset: String?,
        limit: String?,
        nsfw: Boolean
    ) {
        try {
            val getTopMangaDeferred = malApi.getMangaRankingAsync(
                ranking_type, limit, offset,
                BASIC_MANGA_FIELDS,
                nsfw = nsfw
            )
            val mangaRanking = getTopMangaDeferred.await()
            if (nextPage != mangaRanking.paging?.next) {
                nextPage = mangaRanking.paging?.next
            }
            if (nsfw) {
                val mangaList = mangaRanking.data
                MangaRankingDao.insertMangaRankingList(mangaList!!)
            } else {
                val mangaList = mangaRanking.data?.filter { it?.manga?.nsfw == "white" }
                MangaRankingDao.insertMangaRankingList(mangaList!!)
            }
            mangaRankingList.value = Resource.success(mangaRanking.data.toMutableList())
        } catch (e: Exception) {
            mangaRankingList.value = Resource.error(e.message ?: "", null)
        }
    }
}