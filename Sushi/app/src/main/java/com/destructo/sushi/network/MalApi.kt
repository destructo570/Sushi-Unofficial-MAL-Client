package com.destructo.sushi.network

import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface MalApi {


    @GET("anime/ranking")
    fun getAnimeRanking(
        @Query("ranking_type") ranking_type:String,
        @Query("limit") limit:String?,
        @Query("offset") offset:String?,
        @Query("fields") fields:String?

    ): Deferred<AnimeRanking>



}