package com.destructo.sushi.model.jikan.common


import com.squareup.moshi.Json

data class User(
    @Json(name = "date")
    val date: String?=null,
    @Json(name = "episodes_seen")
    val episodesSeen: Int?=null,
    @Json(name = "episodes_total")
    val episodesTotal: Int?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "score")
    val score: Any?=null,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "username")
    val username: String?=null
)