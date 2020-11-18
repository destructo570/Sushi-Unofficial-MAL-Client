package com.destructo.sushi.model.mal.anime


import android.os.Parcelable
import androidx.room.Entity
import com.destructo.sushi.model.mal.common.Genre
import com.destructo.sushi.model.mal.common.MainPicture
import com.destructo.sushi.model.mal.common.Picture
import com.destructo.sushi.model.mal.manga.RelatedManga
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "anime_details", primaryKeys = ["id"])
data class Anime(
    @Json(name = "alternative_titles")
    val alternativeTitles: AlternativeTitles?=null,
    @Json(name = "average_episode_duration")
    val averageEpisodeDuration: Int?=null,
    @Json(name = "background")
    val background: String?=null,
    @Json(name = "broadcast")
    val broadcast: Broadcast?=null,
    @Json(name = "created_at")
    val createdAt: String?=null,
    @Json(name = "end_date")
    val endDate: String?=null,
    @Json(name = "genres")
    val genres: List<Genre?>?=null,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "main_picture")
    val mainPicture: MainPicture?=null,
    @Json(name = "mean")
    val mean: Double?=null,
    @Json(name = "media_type")
    val mediaType: String?=null,
    @Json(name = "my_list_status")
    val myAnimeListStatus: MyAnimeListStatus?=null,
    @Json(name = "nsfw")
    val nsfw: String?=null,
    @Json(name = "num_episodes")
    val numEpisodes: Int?=null,
    @Json(name = "num_list_users")
    val numListUsers: Int?=null,
    @Json(name = "num_scoring_users")
    val numScoringUsers: Int?=null,
    @Json(name = "pictures")
    val pictures: List<Picture?>?=null,
    @Json(name = "popularity")
    val popularity: Int?=null,
    @Json(name = "rank")
    val rank: Int?=null,
    @Json(name = "rating")
    val rating: String?=null,
    @Json(name = "recommendations")
    val recommendations: List<Recommendation?>?=null,
    @Json(name = "related_anime")
    val relatedAnime: List<RelatedAnime?>?=null,
    @Json(name = "related_manga")
    val relatedManga: List<RelatedManga?>?=null,
    @Json(name = "source")
    val source: String?=null,
    @Json(name = "start_date")
    val startDate: String?=null,
    @Json(name = "start_season")
    val startSeason: StartSeason?=null,
    @Json(name = "statistics")
    val statistics: Statistics?=null,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "studios")
    val studios: List<Studio?>?=null,
    @Json(name = "synopsis")
    val synopsis: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "updated_at")
    val updatedAt: String?=null
): Parcelable