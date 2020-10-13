package com.destructo.sushi.model.anime.core


import com.destructo.sushi.model.JikanEntity
import com.squareup.moshi.Json

data class AnimeMoreInfo(
    @Json(name = "moreinfo")
    val moreinfo: String?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
): JikanEntity