package com.destructo.sushi.model.anime.core


import com.destructo.sushi.model.JikanEntity
import com.destructo.jikanplay.model.common.Recommendation
import com.squareup.moshi.Json

data class AnimeRecomm(
    @Json(name = "recommendations")
    val recommendations: List<Recommendation?>?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
): JikanEntity