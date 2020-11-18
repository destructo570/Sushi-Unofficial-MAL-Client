package com.destructo.sushi.room

import androidx.room.*
import com.destructo.sushi.model.database.AnimeDetailEntity
import com.destructo.sushi.model.database.AnimeVideosEntity

@Dao
interface AnimeVideoListDao {

    @Query("SELECT * FROM anime_videos WHERE id LIKE :animeId ")
    fun getAnimeVideosById(animeId: Int): AnimeVideosEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeVideos(animeDetails: AnimeVideosEntity)

    @Delete
    fun deleteAllAnimeVideos(animeDetails: MutableList<AnimeVideosEntity>)

    @Delete
    fun deleteAnimeVideos(animeDetails: AnimeVideosEntity)

}