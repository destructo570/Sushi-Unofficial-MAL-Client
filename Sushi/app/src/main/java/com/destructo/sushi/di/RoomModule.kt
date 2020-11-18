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
}