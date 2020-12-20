package com.destructo.sushi.ui.common.allReviews

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.CACHE_EXPIRE_TIME_LIMIT
import com.destructo.sushi.model.database.AnimeReviewsEntity
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeReviewListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllReviewViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val animeReviewsDao: AnimeReviewListDao,
    private val jikanApi: JikanApi,

    ): ViewModel(){

    var animeReview: MutableLiveData<Resource<AnimeReviews>> = MutableLiveData()


    fun getAnimeReviews(malId: Int) {
        animeReview.value = Resource.loading(null)
        viewModelScope.launch {
            val animeReviewListCache = animeReviewsDao.getAnimeReviewsById(malId)

            if (animeReviewListCache != null){

                if((System.currentTimeMillis() - animeReviewListCache.time) > CACHE_EXPIRE_TIME_LIMIT) {
                    animeReviewCall(malId)
                }else{
                    val mAnime = animeReviewListCache.reviewList
                    withContext(Dispatchers.Main) {
                        animeReview.value = Resource.success(mAnime)
                    }
                }
            }else{
                animeReviewCall(malId)
            }
        }
    }

    suspend fun animeReviewCall(malId:Int) {
        val animeId: String = malId.toString()
        val getAnimeReviewsDeferred = jikanApi.getAnimeReviewsAsync(animeId)
        try {
            val animeReviews = getAnimeReviewsDeferred.await()
            val animeReviewListEntity = AnimeReviewsEntity(
                reviewList = animeReviews,
                time = System.currentTimeMillis(),
                id = malId
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

}