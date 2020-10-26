package com.destructo.sushi.network

import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.jikan.character.Character
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.jikan.schedule.Schedule
import com.destructo.sushi.model.jikan.season.Season
import com.destructo.sushi.model.jikan.season.SeasonArchive
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
    fun getCurrentlyAiringAsync(
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

    @GET("season/archive")
    fun getSeasonArchiveAsync(
    ): Deferred<SeasonArchive>

    @GET("anime/{animeId}/characters_staff")
    fun getCharacterAndStaffAsync(
        @Path("animeId") animeId: String,
        ): Deferred<AnimeCharacterAndStaff>

    @GET("anime/{animeId}/videos")
    fun getAnimeVideosAsync(
        @Path("animeId") animeId: String,
    ): Deferred<AnimeVideo>


    @GET("anime/{animeId}/reviews")
    fun getAnimeReviewsAsync(
        @Path("animeId") animeId: String,
    ): Deferred<AnimeReviews>


    @GET("manga/{mangaId}/reviews")
    fun getMangaReviewsAsync(
        @Path("mangaId") mangaId: String,
    ): Deferred<MangaReview>


    @GET("character/{characterId}")
    fun getCharacterDetailsAsync(
        @Path("characterId") characterId: String,
    ): Deferred<Character>


    @GET("manga/{mangaId}/characters")
    fun getMangaCharactersAsync(
        @Path("mangaId") mangaId: String,
    ): Deferred<MangaCharacter>


}
