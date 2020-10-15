package com.destructo.jikanplay.model.common


import com.squareup.moshi.Json

data class Recommendation(
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "recommendation_count")
    val recommendationCount: Int?=null,
    @Json(name = "recommendation_url")
    val recommendationUrl: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "url")
    val url: String?=null
)