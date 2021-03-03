package com.destructo.sushi.room

import androidx.room.*
import com.destructo.sushi.model.database.AnimeDetailEntity

@Dao
interface AnimeDetailsDao {

    @Query("SELECT * FROM anime_detail WHERE id LIKE :animeId ")
    fun getAnimeDetailsById(animeId: Int): AnimeDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeDetails(animeDetails: AnimeDetailEntity)

    @Delete
    fun deleteAllAnimeDetails(animeDetails: MutableList<AnimeDetailEntity>)

    @Query("DELETE FROM anime_detail WHERE id LIKE :animeId")
    fun deleteAnimeDetailById(animeId: Int)



    @Delete
    fun deleteAnimeDetails(animeDetails: AnimeDetailEntity)

}