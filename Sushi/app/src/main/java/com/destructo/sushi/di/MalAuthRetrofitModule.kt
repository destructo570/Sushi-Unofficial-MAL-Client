package com.destructo.sushi.di

import com.destructo.sushi.BASE_MAL_AUTH_URL
import com.destructo.sushi.network.MalAuthApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MalAuthRetrofitModule {

        @Singleton
        @Provides
        @MalAuthRetrofit
        fun provideMalApiRetrofit(moshi: Moshi): Retrofit.Builder{
            return Retrofit.Builder()
                .baseUrl(BASE_MAL_AUTH_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
        }

        @Singleton
        @Provides
        fun provideMalAuthApi(
            @MalAuthRetrofit
            retrofit: Retrofit.Builder
        ): MalAuthApi {
            return retrofit
                .build()
                .create(MalAuthApi::class.java)
        }


    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MalAuthRetrofit
