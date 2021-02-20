package com.destructo.sushi.model.jikan.user.animeList


import com.squareup.moshi.Json

data class ProfileUserAnimeList(
    @Json(name = "anime")
    val anime: List<Anime>?=null,
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    val requestHash: String?=null
)