package com.destructo.sushi.di

import com.destructo.sushi.BASE_JIKAN_URL
import com.destructo.sushi.BASE_MAL_API_URL
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.MalAuthInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
object MalRetrofitModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideInterceptor():OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(MalAuthInterceptor())
            .build()
    }

    @Singleton
    @Provides
    @MalRetrofit
    fun provideMalApiRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl(BASE_MAL_API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideMalApi(
        @MalRetrofit
        retrofit: Retrofit.Builder
    ): MalApi {
        return retrofit
            .build()
            .create(MalApi::class.java)
    }


}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MalRetrofit