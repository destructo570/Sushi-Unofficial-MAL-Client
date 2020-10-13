package com.destructo.sushi.model.anime.support


import com.squareup.moshi.Json

data class EpisodeVideo(
    @Json(name = "episode")
    val episode: String?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "url")
    val url: String?=null
)