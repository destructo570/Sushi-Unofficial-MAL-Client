package com.destructo.sushi.model.anime.core


import com.destructo.sushi.model.JikanEntity
import com.destructo.jikanplay.model.common.Review
import com.squareup.moshi.Json

data class AnimeReviews(
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
    @Json(name = "reviews")
    val reviews: List<Review?>?=null
): JikanEntity