package com.destructo.sushi.network

import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.mangaRanking.MangaRanking
import com.destructo.sushi.model.mal.seasonalAnime.Season
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MalApi {

    @GET("anime/{Id}")
    fun getAnimeByIdAsync(
        @Path("Id") animeId:String,
        @Query("fields") fields:String
    ): Deferred<Anime>
    @GET("anime/ranking")


    fun getAnimeRankingAsync(
        @Query("ranking_type") ranking_type:String,
        @Query("limit") limit:String?,
        @Query("offset") offset:String?,
        @Query("fields") fields:String?

    ): Deferred<AnimeRanking>

    @GET("anime/season/{year}/{season}")
    fun getSeasonalAnimeAsync(
        @Path("year") year:String,
        @Path("season") season:String,
        @Query("sort") sort:String?,
        @Query("limit") limit:String?,
        @Query("offset") offset:String?,
        @Query("fields") fields:String?

    ): Deferred<SeasonalAnime>


    @GET("manga/ranking")
    fun getMangaRankingAsync(
        @Query("ranking_type") ranking_type:String?,
        @Query("limit") limit:String?,
        @Query("offset") offset:String?,
        @Query("fields") fields:String?

    ): Deferred<MangaRanking>



}