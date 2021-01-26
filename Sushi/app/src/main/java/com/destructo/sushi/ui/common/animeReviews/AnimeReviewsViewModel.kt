package com.destructo.sushi.ui.common.animeReviews

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.CACHE_EXPIRE_TIME_LIMIT
import com.destructo.sushi.model.database.AnimeReviewsEntity
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.common.Review
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeReviewListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimeReviewsViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val animeReviewsDao: AnimeReviewListDao,
    private val jikanApi: JikanApi,

    ): ViewModel(){

    var animeReview: MutableLiveData<Resource<AnimeReviews>> = MutableLiveData()

    fun getReviewListById(malId: Int): AnimeReviews? {
        return animeReviewsDao.getAnimeReviewsById(malId)?.reviewList
    }

    fun getAnimeReviews(malId: Int, page: String) {
        animeReview.value = Resource.loading(null)
        viewModelScope.launch {
            val animeReviewListCache = animeReviewsDao.getAnimeReviewsById(malId)

            if (animeReviewListCache != null){

                if((System.currentTimeMillis() - animeReviewListCache.time) > CACHE_EXPIRE_TIME_LIMIT) {
                    animeReviewCall(malId, page)
                }else{
                    val mAnime = animeReviewListCache.reviewList
                    withContext(Dispatchers.Main) {
                        animeReview.value = Resource.success(mAnime)
                    }
                }
            }else{
                animeReviewCall(malId, page)
            }
        }
    }

    private suspend fun animeReviewCall(malId:Int, page: String) {
        val animeId: String = malId.toString()
        val getAnimeReviewsDeferred = jikanApi.getAnimeReviewsAsync(animeId, page)
        try {
            val animeReviews = getAnimeReviewsDeferred.await()
            val animeReviewListEntity = AnimeReviewsEntity(
                reviewList = animeReviews,
                time = System.currentTimeMillis(),
                id = malId,
                currentPage = page
            )
            animeReviewsDao.insertAnimeReviews(animeReviewListEntity)
            withContext(Dispatchers.Main) {
                animeReview.value = Resource.success(animeReviewListEntity.reviewList)
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                animeReview.value = Resource.error(e.message ?: "", null)
            }
        }
    }



     fun loadNextPage(malId:Int){
            animeReview.value = Resource.loading(null)
         viewModelScope.launch{
             val animeId: String = malId.toString()
             val page = animeReviewsDao.getAnimeReviewsById(malId)?.currentPage?.toInt()?.plus(1)
             val getAnimeReviewsDeferred = jikanApi.getAnimeReviewsAsync(animeId, page.toString())
             try {
                 val animeReviews = getAnimeReviewsDeferred.await()
                 val oldReviewList = animeReviewsDao.getAnimeReviewsById(malId)?.reviewList?.reviews
                 animeReviews.reviews?.addAll(0, oldReviewList as Collection<Review?>)

                 val animeReviewListEntity = AnimeReviewsEntity(
                     reviewList = animeReviews,
                     time = System.currentTimeMillis(),
                     id = malId,
                     currentPage = page.toString()
                 )

                 animeReviewsDao.insertAnimeReviews(animeReviewListEntity)
                 animeReview.value = Resource.success(animeReviewListEntity.reviewList)


             } catch (e: Exception) {
                     animeReview.value = Resource.error(e.message ?: "", null)
             }
         }
    }


}