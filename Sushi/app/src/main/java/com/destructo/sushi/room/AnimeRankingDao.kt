package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.destructo.sushi.model.database.AnimeDetailEntity
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData

@Dao
interface AnimeRankingDao {

    @Query("SELECT * FROM anime_ranking_list WHERE ranking_type LIKE :rankingType ORDER BY rank ASC")
    fun getAnimeRankingByRankingType(rankingType: String): LiveData<MutableList<AnimeRankingData>>

    @Query("SELECT * FROM anime_ranking_list ORDER BY rank ASC")
    fun getAllAnimeRanking(): LiveData<MutableList<AnimeRankingData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeRanking(animeRanking: AnimeRankingData?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeRankingList(animeRanking: MutableList<AnimeRankingData?>)


    @Delete
    fun deleteAllAnimeRankingList(animeRanking: MutableList<AnimeRankingData>)

    @Delete
    fun deleteAnimeRanking(animeRanking: AnimeRankingData)
}