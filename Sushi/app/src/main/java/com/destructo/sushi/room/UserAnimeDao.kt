package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.destructo.sushi.model.database.UserAnimeEntity

@Dao
interface UserAnimeDao {
    @Query("SELECT * FROM user_anime_list")
    fun getUserAnimeList(): LiveData<List<UserAnimeEntity>>

    @Query("SELECT * FROM user_anime_list WHERE malId LIKE :malId")
    fun getUserAnimeById(malId: Int): UserAnimeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserAnimeList(animeList: List<UserAnimeEntity>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserAnime(anime: UserAnimeEntity?)

    @Query("DELETE FROM user_anime_list")
    fun clear()

    @Query("DELETE FROM user_anime_list WHERE malId LIKE :id")
    fun deleteUserAnimeById(id: Int)

    @Delete
    fun deleteUserAnime(anime: UserAnimeEntity)
}