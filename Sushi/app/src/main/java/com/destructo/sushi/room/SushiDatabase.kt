package com.destructo.sushi.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.destructo.sushi.model.database.*
import com.destructo.sushi.model.jikan.user.animeList.Anime
import com.destructo.sushi.model.jikan.user.friends.Friend
import com.destructo.sushi.model.mal.animeList.AnimeListData
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.model.mal.mangaList.MangaListData
import com.destructo.sushi.model.mal.mangaRanking.MangaRankingData
import com.destructo.sushi.model.mal.seasonalAnime.SeasonAnimeData
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData
import com.destructo.sushi.model.mal.userMangaList.UserMangaData
import com.destructo.sushi.room.TypeConverters as TypeConverters1

@TypeConverters(value = [TypeConverters1::class])
@Database(entities = [AnimeDetailEntity::class, AnimeCharacterListEntity::class,
    AnimeVideosEntity::class, AnimeReviewsEntity::class, MangaDetailsEntity::class,
    MangaCharacterListEntity::class, MangaReviewsEntity::class, AnimeRankingData::class,
    MangaRankingData::class, SeasonAnimeData::class, AnimeListData::class, MangaListData::class,
    UserAnimeData::class, UserMangaData::class, UserInfoEntity::class, Anime::class,
    Friend::class], version = 4, exportSchema = false)
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
    abstract fun seasonAnimeDao():SeasonAnimeDao
    abstract fun animeListDao():SearchAnimeDao
    abstract fun mangaListDao():SearchMangaDao
    abstract fun userAnimeListDao():UserAnimeListDao
    abstract fun userMangaListDao():UserMangaListDao
    abstract fun userInfoDao():UserInfoDao
    abstract fun profileAnimeList():ProfileAnimeListDao
    abstract fun profileUserFriendList():ProfileUserFriendListDao

    companion object{

        const val DATABASE_NAME = "sushi_database"

    }
}