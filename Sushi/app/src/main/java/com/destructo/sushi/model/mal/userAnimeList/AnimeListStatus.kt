package com.destructo.sushi.model.mal.userAnimeList


import com.squareup.moshi.Json

data class AnimeListStatus(
    @Json(name = "is_rewatching")
    val isRewatching: Boolean?=null,
    @Json(name = "num_episodes_watched")
    val numEpisodesWatched: Int?=null,
    @Json(name = "score")
    val score: Int?=null,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "updated_at")
    val updatedAt: String?=null
)