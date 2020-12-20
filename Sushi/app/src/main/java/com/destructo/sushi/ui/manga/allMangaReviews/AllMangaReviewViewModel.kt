package com.destructo.sushi.ui.manga.allMangaReviews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.CACHE_EXPIRE_TIME_LIMIT
import com.destructo.sushi.model.database.MangaReviewsEntity
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.MangaReviewListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllMangaReviewViewModel
    @ViewModelInject
    constructor(
        private val jikanApi: JikanApi,
        private val mangaReviewListDao: MangaReviewListDao
        ): ViewModel() {

    var mangaReview: MutableLiveData<Resource<MangaReview>> = MutableLiveData()


    fun getMangaReviews(malId: Int) {
        mangaReview.value = Resource.loading(null)

        viewModelScope.launch {
            val mangaReviewListCache = mangaReviewListDao.getMangaReviewsById(malId)

            if (mangaReviewListCache != null){

                if((System.currentTimeMillis() - mangaReviewListCache.time) > CACHE_EXPIRE_TIME_LIMIT) {
                    mangaReviewsCall(malId)
                }else{
                    val mangaCharacterList = mangaReviewListCache.reviewList
                    withContext(Dispatchers.Main) {
                        mangaReview.value = Resource.success(mangaCharacterList)
                    }
                }
            }else{
                mangaReviewsCall(malId)
            }
        }
    }

    private suspend fun mangaReviewsCall(malId:Int) {
        val mangaId: String = malId.toString()
        val getMangaReviewsDeferred = jikanApi.getMangaReviewsAsync(mangaId)
        try {
            val mangaCharacterList = getMangaReviewsDeferred.await()
            val mangaReviewRequest = MangaReviewsEntity(
                reviewList = mangaCharacterList,
                id = malId,
                time = System.currentTimeMillis()
            )
            mangaReviewListDao.insertMangaReviews(mangaReviewRequest)
            withContext(Dispatchers.Main) {
                mangaReview.value = Resource.success(mangaReviewRequest.reviewList)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mangaReview.value = Resource.error(e.message ?: "", null)

            }
        }
    }

}