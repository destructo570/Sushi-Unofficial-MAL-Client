package com.destructo.sushi.ui.manga.mangaReviews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.CACHE_EXPIRE_TIME_LIMIT
import com.destructo.sushi.model.database.MangaReviewsEntity
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.ReviewEntity
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.MangaReviewListDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MangaReviewsViewModel
    @Inject
    constructor(
        private val jikanApi: JikanApi,
        private val mangaReviewListDao: MangaReviewListDao
        ): ViewModel() {

    var mangaReview: MutableLiveData<Resource<MangaReview>> = MutableLiveData()

    fun getReviewListById(malId: Int): MangaReview? {
        return mangaReviewListDao.getMangaReviewsById(malId)?.reviewList
    }

    fun getMangaReviews(malId: Int, page: String) {
        mangaReview.value = Resource.loading(null)

        viewModelScope.launch {
            val mangaReviewListCache = mangaReviewListDao.getMangaReviewsById(malId)

            if (mangaReviewListCache != null){

                if((System.currentTimeMillis() - mangaReviewListCache.time) > CACHE_EXPIRE_TIME_LIMIT) {
                    mangaReviewsCall(malId, page)
                }else{
                    val mangaCharacterList = mangaReviewListCache.reviewList
                    withContext(Dispatchers.Main) {
                        mangaReview.value = Resource.success(mangaCharacterList)
                    }
                }
            }else{
                mangaReviewsCall(malId, page)
            }
        }
    }

    private suspend fun mangaReviewsCall(malId:Int, page: String) {
        val mangaId: String = malId.toString()
        val getMangaReviewsDeferred = jikanApi.getMangaReviewsAsync(mangaId, page)
        try {
            val mangaCharacterList = getMangaReviewsDeferred.await()
            val mangaReviewRequest = MangaReviewsEntity(
                reviewList = mangaCharacterList,
                id = malId,
                time = System.currentTimeMillis(),
                currentPage = page
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


    fun loadNextPage(malId:Int){
        mangaReview.value = Resource.loading(null)
        viewModelScope.launch{
            val mangaId: String = malId.toString()
            val page = mangaReviewListDao.getMangaReviewsById(malId)?.currentPage?.toInt()?.plus(1)
            val getReviewsDeferred = jikanApi.getMangaReviewsAsync(mangaId, page.toString())
            try {
                val mangaReviews = getReviewsDeferred.await()
                val oldReviewList = mangaReviewListDao.getMangaReviewsById(malId)?.reviewList?.reviews
                mangaReviews.reviews?.addAll(0, oldReviewList as Collection<ReviewEntity?>)

                val animeReviewListEntity = MangaReviewsEntity(
                    reviewList = mangaReviews,
                    time = System.currentTimeMillis(),
                    id = malId,
                    currentPage = page.toString()
                )
                Timber.e("Load Page : $page")
                mangaReviewListDao.insertMangaReviews(animeReviewListEntity)
                mangaReview.value = Resource.success(animeReviewListEntity.reviewList)

            } catch (e: Exception) {
                mangaReview.value = Resource.error(e.message ?: "", null)
            }
        }
    }


}