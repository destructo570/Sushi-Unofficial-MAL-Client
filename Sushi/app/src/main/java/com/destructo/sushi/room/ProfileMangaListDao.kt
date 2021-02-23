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

    @Query("SELECT * FROM jikan_profile_manga_list")
    fun getMangaListAll(): List<Manga>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMangaList(mangaList: List<Manga?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertManga(manga:Manga?)

    @Query("DELETE FROM jikan_profile_manga_list")
    fun deleteAllManga()


}