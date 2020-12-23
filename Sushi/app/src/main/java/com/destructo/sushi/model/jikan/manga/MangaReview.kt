package com.destructo.sushi.model.jikan.manga


import com.squareup.moshi.Json

data class MangaReview(
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    val requestHash: String?=null,
    @Json(name = "reviews")
    val reviews: MutableList<ReviewEntity?>?=null
)