package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.destructo.sushi.model.mal.mangaList.MangaListData

@Dao
interface SearchMangaDao {

    @Query("SELECT * FROM search_manga_list")
    fun getMangaList(): LiveData<List<MangaListData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMangaList(mangaList: List<MangaListData?>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertManga(manga: MangaListData)

    @Query("DELETE FROM search_manga_list")
    fun clear()

    @Delete
    fun deleteManga(manga: MangaListData)
}