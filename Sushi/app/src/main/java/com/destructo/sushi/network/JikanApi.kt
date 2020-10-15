package com.destructo.sushi.network

import com.destructo.sushi.model.jikan.schedule.Schedule
import com.destructo.sushi.model.jikan.season.Season
import com.destructo.sushi.model.jikan.top.TopAnime
import com.destructo.sushi.model.jikan.top.TopManga
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanApi {


    @GET("top/anime/{page}/{subtype}")
    fun getTopAnimeAsync(
        @Path("page") page: String,
        @Path("subtype") subtype: String,
    ): Deferred<TopAnime>

    @GET("top/anime/{page}/upcoming")
    fun getUpcomingAnimeAsync(
        @Path("page") page: String,
    ): Deferred<TopAnime>


    @GET("top/anime/{page}/airing")
    fun getCurrentlyAiringsync(
        @Path("page") page: String,
    ): Deferred<TopAnime>


    @GET("season/{year}/{season}")
    fun getSeasonalAnimeAsync(
        @Path("year") year: String,
        @Path("season") season: String,
    ): Deferred<Season>

    @GET("top/manga/{page}/{subtype}")
    fun getTopMangaAsync(
        @Path("page") page: String,
        @Path("subtype") subtype: String,
    ): Deferred<TopManga>


    @GET("schedule/{weekday}")
    fun getAnimeScheduleAsync(
        @Path("weekday") weekday: String,
    ): Deferred<Schedule>

}
