package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.destructo.sushi.model.database.AnimeDetailEntity
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData

@Dao
interface AnimeRankingDao {

    @Query("SELECT * FROM anime_ranking_list WHERE ranking_type LIKE :rankingType ORDER BY rank ASC")
    fun getAnimeRankingByRankingType(rankingType: String): LiveData<List<AnimeRankingData>>

    @Query("SELECT * FROM anime_ranking_list ORDER BY rank ASC")
    fun getAllAnimeRanking(): LiveData<List<AnimeRankingData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeRanking(animeRanking: AnimeRankingData?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeRankingList(animeRanking: List<AnimeRankingData?>)

    @Query("DELETE FROM anime_ranking_list")
    fun clear()

    @Delete
    fun deleteAnimeRanking(animeRanking: AnimeRankingData)
}