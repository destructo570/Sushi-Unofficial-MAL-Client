package com.destructo.sushi.room

import androidx.room.*
import com.destructo.sushi.model.database.MangaCharacterListEntity

@Dao
interface MangaCharacterListDao {

    @Query("SELECT * FROM manga_characters_list WHERE id LIKE :mangaId ")
    fun getMangaCharacterListById(mangaId: Int): MangaCharacterListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMangaCharacterList(mangaDetails: MangaCharacterListEntity)

    @Delete
    fun deleteAllMangaCharacterList(mangaDetails: MutableList<MangaCharacterListEntity>)

    @Delete
    fun deleteMangaCharacterList(mangaDetails: MangaCharacterListEntity)

}