package com.destructo.sushi.network

import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.animeList.AnimeList
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRecom.SuggestedAnime
import com.destructo.sushi.model.mal.forum.ForumBoard
import com.destructo.sushi.model.mal.forum.ForumTopicDetail
import com.destructo.sushi.model.mal.forum.ForumTopics
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.mangaList.MangaList
import com.destructo.sushi.model.mal.mangaRanking.MangaRanking
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.model.mal.userInfo.UserInfo
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import kotlinx.coroutines.Deferred
import retrofit2.http.*

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
        @Query("fields") fields:String?,
        @Query("nsfw") nsfw:Boolean

    ): Deferred<AnimeRanking>

    @GET
    fun getAnimeRankingNextAsync(
        @Url url:String,
        @Query("nsfw") nsfw:Boolean
    ): Deferred<AnimeRanking>

    @GET
    fun getMangaRankingNextAsync(@Url url:String,
                                 @Query("nsfw") nsfw:Boolean
    ): Deferred<MangaRanking>

    @GET
    fun getSeasonalAnimeNextAsync(@Url url:String): Deferred<SeasonalAnime>

    @GET
    fun getSearchAnimeNextAsync(@Url url:String,
                                @Query("nsfw")nsfw:Boolean): Deferred<AnimeList>

    @GET
    fun getSearchMangaNextAsync(@Url url:String,
                                @Query("nsfw")nsfw:Boolean): Deferred<MangaList>

    @GET
    fun getUserAnimeNextAsync(@Url url:String): Deferred<UserAnimeList>

    @GET
    fun getUserMangaNextAsync(@Url url:String): Deferred<UserMangaList>

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
        @Query("fields") fields:String?,
        @Query("nsfw") nsfw:Boolean

    ): Deferred<MangaRanking>


    @GET("manga/{Id}")
    fun getMangaByIdAsync(
        @Path("Id") animeId:String,
        @Query("fields") fields:String
    ): Deferred<Manga>

    @GET("users/{user_name}/animelist")
    fun getUserAnimeListAsync(
        @Path("user_name") user_name:String,
        @Query("limit") limit:String,
        @Query("status") status:String?,
        @Query("sort") sort:String?,
        @Query("offset") offset:String,
        @Query("fields") fields:String,

        ): Deferred<UserAnimeList>


    @GET("users/{user_name}/mangalist")
    fun getUserMangaListAsync(
        @Path("user_name") user_name:String,
        @Query("limit") limit:String,
        @Query("status") status:String?,
        @Query("sort") sort:String?,
        @Query("offset") offset:String,
        @Query("fields") fields:String,

        ): Deferred<UserMangaList>

    @FormUrlEncoded
    @PATCH("anime/{anime_id}/my_list_status")
    fun updateUserAnime(
        @Path("anime_id") anime_id:String,
        @Field("status") status:String?,
        @Field("is_rewatching") is_rewatching:Boolean?,
        @Field("score") score:Int?,
        @Field("num_watched_episodes") num_watched_episodes:Int?,
        @Field("priority") priority:Int?,
        @Field("num_times_rewatched") num_times_rewatched:Int?,
        @Field("rewatch_value") rewatch_value:Int?,
        @Field("tags") tags:List<String>?,
        @Field("comments") comments:String?,
        @Field("start_date") startDate:String?,
        @Field("finish_date") finishDate:String?
    ): Deferred<UpdateUserAnime>

    @FormUrlEncoded
    @PATCH("manga/{manga_id}/my_list_status")
    fun updateUserManga(
        @Path("manga_id") manga_id:String,
        @Field("status") status:String?,
        @Field("is_rereading") is_rereading:Boolean?,
        @Field("score") score:Int?,
        @Field("num_volumes_read") num_volumes_read:Int?,
        @Field("num_chapters_read") num_chapters_read:Int?,
        @Field("priority") priority:Int?,
        @Field("num_times_reread") num_times_reread:Int?,
        @Field("reread_value") reread_value:Int?,
        @Field("tags") tags:List<String>?,
        @Field("comments") comments:String?,
        @Field("start_date") startDate:String?,
        @Field("finish_date") finishDate:String?
    ): Deferred<UpdateUserManga>

    @DELETE("anime/{anime_id}/my_list_status")
    fun deleteAnimeFromList(
        @Path("anime_id") anime_id:String
    ):Deferred<Unit>

    @DELETE("manga/{manga_id}/my_list_status")
    fun deleteMangaFromList(
        @Path("manga_id") manga_id:String
    ):Deferred<Unit>

    @GET("users/@me")
    fun getUserInfo(
        @Query("fields") status:String?,
        ): Deferred<UserInfo>

    @GET("anime")
    fun searchAnimeAsync(
        @Query("q") query:String,
        @Query("limit") limit:String?,
        @Query("offset") offset:String?,
        @Query("fields") fields:String?,
        @Query("nsfw") nsfw:Boolean

    ): Deferred<AnimeList>

    @GET("manga")
    fun searchMangaAsync(
        @Query("q") query:String,
        @Query("limit") limit:String?,
        @Query("offset") offset:String?,
        @Query("fields") fields:String?,
        @Query("nsfw") nsfw:Boolean

        ): Deferred<MangaList>


    @GET("anime/suggestions")
    fun getAnimeRecomAsync(
        @Query("limit") limit:String?,
        @Query("offset") offset:String?,
        @Query("fields") fields:String?,
        @Query("nsfw") nsfw:Boolean

    ): Deferred<SuggestedAnime>


    @GET("forum/boards")
    fun getForumBoardsAsync(): Deferred<ForumBoard>

    @GET("forum/topic/{topic_id}")
    fun getForumTopicDetailAsync(
        @Path("topic_id") topic_id:String
    ): Deferred<ForumTopicDetail>

    @GET("forum/topics")
    fun getForumTopicsAsync(
        @Query("board_id") board_id:Int?,
        @Query("subboard_id") sub_board_id:Int?,
        @Query("limit") limit:Int?,
        @Query("offset") offset:Int,
        @Query("sort") sort:String?,
        @Query("q") q:String?,
        @Query("topic_user_name") topic_user_name:String?,
        @Query("user_name") user_name:String?,
        ): Deferred<ForumTopics>

    @GET("forum/topics")
    fun getForumTopicsBasicAsync(
        @Query("board_id") board_id:Int?,
        @Query("limit") limit:Int?,
        @Query("offset") offset:Int,
        @Query("sort") sort:String?
    ): Deferred<ForumTopics>

}