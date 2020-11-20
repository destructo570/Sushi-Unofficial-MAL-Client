package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.destructo.sushi.model.mal.mangaRanking.MangaRankingData
import com.destructo.sushi.model.mal.seasonalAnime.SeasonAnimeData

@Dao
interface SeasonAnimeDao {

    @Query("SELECT * FROM seasonal_anime_list")
    fun getAllSeasonAnime(): LiveData<List<SeasonAnimeData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeasonAnime(seasonData: SeasonAnimeData?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeasonAnimeList(seasonData: List<SeasonAnimeData?>)

    @Query("DELETE FROM seasonal_anime_list")
    fun clear()

    @Delete
    fun deleteMangaRanking(seasonData: SeasonAnimeData)

}