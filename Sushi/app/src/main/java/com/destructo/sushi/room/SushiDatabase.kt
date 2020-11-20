package com.destructo.sushi.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.destructo.sushi.model.database.*
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.model.mal.mangaRanking.MangaRankingData
import com.destructo.sushi.room.TypeConverters as TypeConverters1

@TypeConverters(value = [TypeConverters1::class])
@Database(entities = [AnimeDetailEntity::class, AnimeCharacterListEntity::class,
    AnimeVideosEntity::class, AnimeReviewsEntity::class, MangaDetailsEntity::class,
    MangaCharacterListEntity::class, MangaReviewsEntity::class, AnimeRankingData::class,
    MangaRankingData::class], version = 1, exportSchema = false)
abstract class SushiDatabase: RoomDatabase() {

    abstract fun animeDetailsDao():AnimeDetailsDao
    abstract fun animeCharacterListDao():AnimeCharacterListDao
    abstract fun animeVideosDao():AnimeVideoListDao
    abstract fun animeReviewsDao():AnimeReviewListDao
    abstract fun mangaDetailsDao():MangaDetailsDao
    abstract fun mangaCharacterListDao():MangaCharacterListDao
    abstract fun mangaReviewListDao():MangaReviewListDao
    abstract fun animeRankingListDao():AnimeRankingDao
    abstract fun mangaRankingListDao():MangaRankingDao

    companion object{

        val DATABASE_NAME = "sushi_database"

    }
}