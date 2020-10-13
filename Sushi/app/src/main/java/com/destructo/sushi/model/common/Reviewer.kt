package com.destructo.jikanplay.model.common


import com.squareup.moshi.Json

data class Reviewer(
    @Json(name = "episodes_seen")
    val episodesSeen: Int?=null,
    @Json(name = "chapters_read")
    val chaptersRead: Int?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "scores")
    val reviewScores: ReviewScores?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "username")
    val username: String?=null
)