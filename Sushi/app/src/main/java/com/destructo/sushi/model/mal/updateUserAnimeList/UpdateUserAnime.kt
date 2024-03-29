package com.destructo.sushi.model.mal.updateUserAnimeList


import com.squareup.moshi.Json

data class UpdateUserAnime(
    @Json(name = "comments")
    val comments: String?=null,
    @Json(name = "is_rewatching")
    val isRewatching: Boolean?=null,
    @Json(name = "num_episodes_watched")
    val numEpisodesWatched: Int?=null,
    @Json(name = "num_times_rewatched")
    val numTimesRewatched: Int?=null,
    @Json(name = "priority")
    val priority: Int?=null,
    @Json(name = "rewatch_value")
    val rewatchValue: Int?=null,
    @Json(name = "score")
    val score: Int?=null,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "tags")
    val tags: List<String?>?=null,
    @Json(name = "updated_at")
    val updatedAt: String?=null
)