package com.destructo.sushi.room

import androidx.room.*
import com.destructo.sushi.model.database.MangaReviewsEntity

@Dao
interface MangaReviewListDao {

    @Query("SELECT * FROM manga_reviews WHERE id LIKE :mangaId ")
    fun getMangaReviewsById(mangaId: Int): MangaReviewsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMangaReviews(mangaReviewList: MangaReviewsEntity)

    @Delete
    fun deleteAllMangaReviews(mangaReviewList: MutableList<MangaReviewsEntity>)

    @Delete
    fun deleteMangaReviews(mangaReviewList: MangaReviewsEntity)

}