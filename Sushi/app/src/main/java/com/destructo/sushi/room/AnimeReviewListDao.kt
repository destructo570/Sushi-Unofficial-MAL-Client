package com.destructo.sushi.room

import androidx.room.*
import com.destructo.sushi.model.database.AnimeReviewsEntity

@Dao
interface AnimeReviewListDao {

    @Query("SELECT * FROM anime_reviews WHERE id LIKE :animeId ORDER BY currentPage")
    fun getAnimeReviewsById(animeId: Int): AnimeReviewsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeReviews(animeReviewList: AnimeReviewsEntity)

    @Query("DELETE FROM anime_reviews WHERE id LIKE :animeId ")
    fun deleteAnimeReviewsById(animeId: Int)

    @Delete
    fun deleteAllAnimeReviews(animeReviewList: MutableList<AnimeReviewsEntity>)

    @Delete
    fun deleteAnimeReviews(animeReviewList: AnimeReviewsEntity)

}