package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.destructo.sushi.model.mal.mangaRanking.MangaRankingData

@Dao
interface MangaRankingDao {

    @Query("SELECT * FROM manga_ranking_list ORDER BY rank ASC")
    fun getAllMangaRanking(): LiveData<List<MangaRankingData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMangaRanking(MangaRanking: MangaRankingData?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMangaRankingList(MangaRanking: List<MangaRankingData?>)

    @Query("DELETE FROM manga_ranking_list")
    fun clear()

    @Delete
    fun deleteMangaRanking(MangaRanking: MangaRankingData)
    
}