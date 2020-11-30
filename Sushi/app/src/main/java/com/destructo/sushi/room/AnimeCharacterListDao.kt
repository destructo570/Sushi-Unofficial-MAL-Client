package com.destructo.sushi.room

import androidx.room.*
import com.destructo.sushi.model.database.AnimeCharacterListEntity

@Dao
interface AnimeCharacterListDao {

    @Query("SELECT * FROM anime_character_list WHERE id LIKE :animeId ")
    fun getAnimeCharactersById(animeId: Int): AnimeCharacterListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeCharacters(animeCharacterList: AnimeCharacterListEntity)

    @Delete
    fun deleteAllAnimeCharacters(animeCharacterList: MutableList<AnimeCharacterListEntity>)

    @Delete
    fun deleteAnimeCharacters(animeCharacterList: AnimeCharacterListEntity)

}