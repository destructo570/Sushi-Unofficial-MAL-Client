package com.destructo.sushi.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.destructo.sushi.model.database.AnimeCharacterListEntity
import com.destructo.sushi.model.database.AnimeDetailEntity
import com.destructo.sushi.model.database.AnimeReviewsEntity
import com.destructo.sushi.model.database.AnimeVideosEntity
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.room.TypeConverters as TypeConverters1

@TypeConverters(value = [TypeConverters1::class])
@Database(entities = [AnimeDetailEntity::class, AnimeCharacterListEntity::class,
    AnimeVideosEntity::class, AnimeReviewsEntity::class], version = 1, exportSchema = false)
abstract class SushiDatabase: RoomDatabase() {

    abstract fun animeDetailsDao():AnimeDetailsDao
    abstract fun animeCharacterListDao():AnimeCharacterListDao
    abstract fun animeVideosDao():AnimeVideoListDao
    abstract fun animeReviewsDao():AnimeReviewListDao

    companion object{

        val DATABASE_NAME = "sushi_database"

    }
}