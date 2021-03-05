package com.destructo.sushi.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.destructo.sushi.model.database.*
import com.destructo.sushi.model.jikan.user.animeList.Anime
import com.destructo.sushi.model.jikan.user.friends.Friend
import com.destructo.sushi.model.jikan.user.mangaList.Manga
import com.destructo.sushi.model.mal.animeList.AnimeListData
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.model.mal.mangaList.MangaListData
import com.destructo.sushi.model.mal.mangaRanking.MangaRankingData
import com.destructo.sushi.model.mal.seasonalAnime.SeasonAnimeData
import com.destructo.sushi.room.TypeConverters as TypeConverters1

@TypeConverters(value = [TypeConverters1::class])
@Database(entities = [AnimeDetailEntity::class, AnimeCharacterListEntity::class,
    AnimeVideosEntity::class, AnimeReviewsEntity::class, MangaDetailsEntity::class,
    MangaCharacterListEntity::class, MangaReviewsEntity::class, AnimeRankingData::class,
    MangaRankingData::class, SeasonAnimeData::class, AnimeListData::class, MangaListData::class,
    UserAnimeEntity::class, UserMangaEntity::class, UserInfoEntity::class, Anime::class,
    Friend::class, Manga::class], version = 9, exportSchema = false)
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
    abstract fun userAnimeDao():UserAnimeDao
    abstract fun userMangaDao():UserMangaDao
    abstract fun userInfoDao():UserInfoDao
    abstract fun profileAnimeList():ProfileAnimeListDao
    abstract fun profileMangaList():ProfileMangaListDao
    abstract fun profileUserFriendList():ProfileUserFriendListDao

    companion object{

        const val DATABASE_NAME = "sushi_database"

    }
}