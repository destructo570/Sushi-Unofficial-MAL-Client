package com.destructo.sushi.ui.anime.animeReviews


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.database.AnimeReviewsEntity
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.common.Review
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeReviewListDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeReviewsViewModel
@Inject
constructor(

    savedStateHandle: SavedStateHandle,
    private val animeReviewsDao: AnimeReviewListDao,
    private val jikanApi: JikanApi,

    ) : ViewModel() {

    var animeReview: MutableLiveData<Resource<AnimeReviews>> = MutableLiveData()

    fun getReviewListById(malId: Int): AnimeReviews? {
        return animeReviewsDao.getAnimeReviewsById(malId)?.reviewList
    }

    fun getAnimeReviews(malId: Int, page: String) {
        animeReview.value = Resource.loading(null)
        viewModelScope.launch {
            val animeReviewListCache = animeReviewsDao.getAnimeReviewsById(malId)

            if (animeReviewListCache != null && !animeReviewListCache.isCacheExpired()) {
                animeReview.value = Resource.success(animeReviewListCache.reviewList)
            } else animeReviewCall(malId, page)
        }
    }

    private suspend fun animeReviewCall(malId: Int, page: String) {
        try {
            val animeReviews = jikanApi.getAnimeReviewsAsync(malId.toString(), page)
            val animeReviewListEntity = AnimeReviewsEntity(
                reviewList = animeReviews,
                time = System.currentTimeMillis(),
                id = malId,
                currentPage = page
            )
            animeReviewsDao.insertAnimeReviews(animeReviewListEntity)
            animeReview.value = Resource.success(animeReviewListEntity.reviewList)
        } catch (e: Exception) {
            animeReview.value = Resource.error(e.message ?: "", null)
        }
    }


    fun loadNextPage(malId: Int) {
        animeReview.value = Resource.loading(null)
        viewModelScope.launch {
            val animeId: String = malId.toString()
            val page = animeReviewsDao.getAnimeReviewsById(malId)?.currentPage?.toInt()?.plus(1)
            try {
                val animeReviews = jikanApi.getAnimeReviewsAsync(animeId, page.toString())
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