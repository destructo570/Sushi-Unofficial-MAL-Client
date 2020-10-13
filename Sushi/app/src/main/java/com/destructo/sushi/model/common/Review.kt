package com.destructo.jikanplay.model.common


import com.squareup.moshi.Json

data class Review(
    @Json(name = "content")
    val content: String?=null,
    @Json(name = "date")
    val date: String?=null,
    @Json(name = "helpful_count")
    val helpfulCount: Int?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "reviewer")
    val reviewer: Reviewer?=null,
    @Json(name = "type")
    val type: Any?=null,
    @Json(name = "url")
    val url: String?=null
)