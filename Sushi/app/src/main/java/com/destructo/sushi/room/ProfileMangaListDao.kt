package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.destructo.sushi.model.jikan.user.mangaList.Manga

@Dao
interface ProfileMangaListDao {
    @Query("SELECT * FROM jikan_profile_manga_list")
    fun getMangaList(): LiveData<List<Manga>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMangaList(animeList: List<Manga?>?)

    @Query("DELETE FROM jikan_profile_manga_list")
    fun deleteAllManga()

}