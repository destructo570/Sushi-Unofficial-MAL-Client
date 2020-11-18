package com.destructo.sushi.room

import androidx.room.*
import com.destructo.sushi.model.database.AnimeCharacterListEntity
import com.destructo.sushi.model.database.AnimeReviewsEntity

@Dao
interface AnimeReviewListDao {

    @Query("SELECT * FROM anime_reviews WHERE id LIKE :animeId ")
    fun getAnimeReviewsById(animeId: Int): AnimeReviewsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeReviews(animeReviewList: AnimeReviewsEntity)

    @Delete
    fun deleteAllAnimeReviews(animeReviewList: MutableList<AnimeReviewsEntity>)

    @Delete
    fun deleteAnimeReviews(animeReviewList: AnimeReviewsEntity)

}