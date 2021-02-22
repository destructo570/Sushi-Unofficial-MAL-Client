package com.destructo.sushi.model.jikan.user.mangaList


import com.squareup.moshi.Json

data class ProfileUserMangaList(
    @Json(name = "manga")
    val manga: List<Manga?>?=null,
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    val requestHash: String?=null
)