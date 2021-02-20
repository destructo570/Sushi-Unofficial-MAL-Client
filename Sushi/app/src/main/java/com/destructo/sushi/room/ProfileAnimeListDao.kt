package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.destructo.sushi.model.jikan.user.animeList.Anime

@Dao
interface ProfileAnimeListDao {
    @Query("SELECT * FROM jikan_profile_anime_list")
    fun getAnimeList():  LiveData<List<Anime>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeList(animeList: List<Anime?>?)

    @Query("DELETE FROM jikan_profile_anime_list")
    fun deleteAllAnime()

}