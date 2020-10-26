package com.destructo.sushi.model.jikan.manga


import com.squareup.moshi.Json

data class Reviewer(
    @Json(name = "chapters_read")
    val chaptersRead: Int?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "scores")
    val scores: Scores?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "username")
    val username: String?=null
)