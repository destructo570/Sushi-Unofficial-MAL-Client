package com.destructo.sushi.model.anime.core


import com.destructo.sushi.model.JikanEntity
import com.destructo.jikanplay.model.common.User
import com.squareup.moshi.Json

data class AnimeUserUpdates(
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
    @Json(name = "users")
    val users: List<User?>?=null
): JikanEntity