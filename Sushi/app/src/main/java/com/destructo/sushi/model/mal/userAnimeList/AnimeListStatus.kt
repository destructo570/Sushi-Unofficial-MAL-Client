package com.destructo.sushi.model.mal.userAnimeList


import com.squareup.moshi.Json

data class AnimeListStatus(
    @Json(name = "is_rewatching")
    var isRewatching: Boolean?=null,
    @Json(name = "num_episodes_watched")
    var numEpisodesWatched: Int?=null,
    @Json(name = "score")
    var score: Int?=null,
    @Json(name = "status")
    var status: String?=null,
    @Json(name = "updated_at")
    var updatedAt: String?=null
)