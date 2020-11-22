package com.destructo.sushi.di

import android.content.Context
import androidx.room.Room
import com.destructo.sushi.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideSushiDatabase(@ApplicationContext context: Context): SushiDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            SushiDatabase::class.java,
            SushiDatabase.DATABASE_NAME
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAnimeDbDetailsDao(sushiDatabase: SushiDatabase): AnimeDetailsDao{
        return sushiDatabase.animeDetailsDao()
    }

    @Singleton
    @Provides
    fun provideAnimeCharacterListDao(sushiDatabase: SushiDatabase): AnimeCharacterListDao{
        return sushiDatabase.animeCharacterListDao()
    }

    @Singleton
    @Provides
    fun provideAnimeVideoListDao(sushiDatabase: SushiDatabase): AnimeVideoListDao{
        return sushiDatabase.animeVideosDao()
    }

    @Singleton
    @Provides
    fun provideAnimeReviewListDao(sushiDatabase: SushiDatabase): AnimeReviewListDao{
        return sushiDatabase.animeReviewsDao()
    }

    @Singleton
    @Provides
    fun provideMangaDetailsDao(sushiDatabase: SushiDatabase): MangaDetailsDao{
        return sushiDatabase.mangaDetailsDao()
    }

    @Singleton
    @Provides
    fun provideMangaCharacterListDao(sushiDatabase: SushiDatabase): MangaCharacterListDao{
        return sushiDatabase.mangaCharacterListDao()
    }

    @Singleton
    @Provides
    fun provideMangaReviewListDao(sushiDatabase: SushiDatabase): MangaReviewListDao{
        return sushiDatabase.mangaReviewListDao()
    }

    @Singleton
    @Provides
    fun provideAnimeRankingListDao(sushiDatabase: SushiDatabase): AnimeRankingDao{
        return sushiDatabase.animeRankingListDao()
    }

    @Singleton
    @Provides
    fun provideMangaRankingListDao(sushiDatabase: SushiDatabase): MangaRankingDao{
        return sushiDatabase.mangaRankingListDao()
    }

    @Singleton
    @Provides
    fun provideSeasonalAnimeListDao(sushiDatabase: SushiDatabase): SeasonAnimeDao{
        return sushiDatabase.seasonAnimeDao()
    }

    @Singleton
    @Provides
    fun provideSearchAnimeListDao(sushiDatabase: SushiDatabase): SearchAnimeDao{
        return sushiDatabase.animeListDao()
    }

    @Singleton
    @Provides
    fun provideSearchMangaListDao(sushiDatabase: SushiDatabase): SearchMangaDao{
        return sushiDatabase.mangaListDao()
    }

    @Singleton
    @Provides
    fun provideUserAnimeListDao(sushiDatabase: SushiDatabase): UserAnimeListDao{
        return sushiDatabase.userAnimeListDao()
    }

    @Singleton
    @Provides
    fun provideUserMangaListDao(sushiDatabase: SushiDatabase): UserMangaListDao{
        return sushiDatabase.userMangaListDao()
    }
}