package com.destructo.sushi.di

import com.destructo.sushi.BASE_JIKAN_URL
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.JikanRateLimitInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object JikanRetrofitModule {


    @Singleton
    @Provides
    @JikanInterceptor
    fun provideJikanInterceptor(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(JikanRateLimitInterceptor())
            .build()
    }

    @Singleton
    @Provides
    @JikanRetrofit
    fun provideJikanApiRetrofit(moshi: Moshi,
                                @JikanInterceptor okHttpClient: OkHttpClient): Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl(BASE_JIKAN_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideJikanApi(
        @JikanRetrofit
        retrofit: Retrofit.Builder
    ): JikanApi {
        return retrofit
            .build()
            .create(JikanApi::class.java)
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JikanRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JikanInterceptor