package com.destructo.sushi.model.jikan.top


import com.destructo.sushi.model.jikan.JikanEntity
import com.squareup.moshi.Json

data class TopCharacter(
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "top")
    val topCharacterEntity: List<TopCharacterEntity?>?=null
): JikanEntity