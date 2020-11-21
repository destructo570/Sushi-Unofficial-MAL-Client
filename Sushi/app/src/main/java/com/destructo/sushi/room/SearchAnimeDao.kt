package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.destructo.sushi.model.mal.animeList.AnimeListData
import com.destructo.sushi.model.mal.mangaRanking.MangaRankingData

@Dao
interface SearchAnimeDao {

    @Query("SELECT * FROM search_anime_list")
    fun getAnimeList(): LiveData<List<AnimeListData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeList(animeList: List<AnimeListData?>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnime(anime: AnimeListData?)

    @Query("DELETE FROM search_anime_list")
    fun clear()

    @Delete
    fun deleteAnime(anime: AnimeListData)
}