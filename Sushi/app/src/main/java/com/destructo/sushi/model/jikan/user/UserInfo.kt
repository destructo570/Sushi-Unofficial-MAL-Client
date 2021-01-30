package com.destructo.sushi.model.jikan.user


import com.squareup.moshi.Json

data class UserInfo(
    @Json(name = "about")
    val about: String?=null,
    @Json(name = "anime_stats")
    val animeStats: AnimeStats?=null,
    @Json(name = "birthday")
    val birthday: String?=null,
    @Json(name = "favorites")
    val favorites: Favorites?=null,
    @Json(name = "gender")
    val gender: String?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "joined")
    val joined: String?=null,
    @Json(name = "last_online")
    val lastOnline: String?=null,
    @Json(name = "location")
    val location: String?=null,
    @Json(name = "manga_stats")
    val mangaStats: MangaStats?=null,
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    val requestHash: String?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "user_id")
    val userId: Int?=null,
    @Json(name = "username")
    val username: String?=null
)