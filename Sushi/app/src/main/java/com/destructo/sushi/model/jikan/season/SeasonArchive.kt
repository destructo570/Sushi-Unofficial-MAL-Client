package com.destructo.sushi.model.jikan.season


import com.destructo.sushi.model.jikan.JikanEntity
import com.squareup.moshi.Json

data class SeasonArchive(
    @Json(name = "archive")
    val archive: List<Archive?>?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null
): JikanEntity