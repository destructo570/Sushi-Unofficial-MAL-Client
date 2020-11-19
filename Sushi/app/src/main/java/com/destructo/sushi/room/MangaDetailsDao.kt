package com.destructo.sushi.room

import androidx.room.*
import com.destructo.sushi.model.database.AnimeDetailEntity
import com.destructo.sushi.model.database.MangaDetailsEntity

@Dao
interface MangaDetailsDao {

    @Query("SELECT * FROM manga_details WHERE id LIKE :mangaId ")
    fun getMangaDetailsById(mangaId: Int): MangaDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMangaDetails(mangaDetails: MangaDetailsEntity)

    @Delete
    fun deleteAllMangaDetails(mangaDetails: MutableList<MangaDetailsEntity>)

    @Delete
    fun deleteMangaDetails(mangaDetails: MangaDetailsEntity)

}