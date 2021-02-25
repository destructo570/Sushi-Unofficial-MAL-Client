package com.destructo.sushi.model.jikan.user.animeList


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.destructo.sushi.model.jikan.MALEntity
import com.squareup.moshi.Json

@Entity(tableName = "jikan_profile_anime_list")
data class Anime(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    @Json(name = "added_to_list")
    val addedToList: Boolean?=null,
    @Json(name = "airing_status")
    val airingStatus: Int?=null,
    @Json(name = "days")
    val days: Double?=null,
    @Json(name = "end_date")
    val endDate: String?=null,
    @Json(name = "has_episode_video")
    val hasEpisodeVideo: Boolean?=null,
    @Json(name = "has_promo_video")
    val hasPromoVideo: Boolean?=null,
    @Json(name = "has_video")
    val hasVideo: Boolean?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "is_rewatching")
    val isRewatching: Boolean?=null,
    @Json(name = "licensors")
    val licensors: List<MALEntity?>?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "priority")
    val priority: String?=null,
    @Json(name = "rating")
    val rating: String?=null,
    @Json(name = "score")
    val score: Int?=null,
    @Json(name = "season_name")
    val seasonName: String?=null,
    @Json(name = "season_year")
    val seasonYear: String?=null,
    @Json(name = "start_date")
    val startDate: String?=null,
    @Json(name = "storage")
    val storage: String?=null,
    @Json(name = "studios")
    val studios: List<MALEntity?>?=null,
    @Json(name = "tags")
    val tags: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "total_episodes")
    val totalEpisodes: Int?=null,
    @Json(name = "type")
    val type: String?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "video_url")
    val videoUrl: String?=null,
    @Json(name = "watch_end_date")
    val watchEndDate: String?=null,
    @Json(name = "watch_start_date")
    val watchStartDate: String?=null,
    @Json(name = "watched_episodes")
    val watchedEpisodes: Int?=null,
    @Json(name = "watching_status")
    val watchingStatus: Int?=null
)