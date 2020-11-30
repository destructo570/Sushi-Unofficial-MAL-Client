package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData

@Dao
interface UserAnimeListDao {

    @Query("SELECT * FROM user_anime_list ORDER BY title ASC")
    fun getUserAnimeList(): LiveData<List<UserAnimeData>>

    @Query("SELECT * FROM user_anime_list WHERE status LIKE :status ORDER BY title ASC")
    fun getUserAnimeListByStatus(status: String): LiveData<List<UserAnimeData>>

    @Query("SELECT * FROM user_anime_list WHERE animeId LIKE :id")
    fun getUserAnimeById(id: Int): UserAnimeData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUseAnimeList(animeList: List<UserAnimeData?>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUseAnime(anime: UserAnimeData?)

    @Query("DELETE FROM user_anime_list")
    fun clear()

    @Query("DELETE FROM user_anime_list WHERE `offset` LIKE :offset")
    fun deleteUserAnimeListByOffset(offset: Int)

    @Delete
    fun deleteUseAnime(anime: UserAnimeData)
}