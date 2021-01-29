package com.destructo.sushi.model.jikan.user


import com.squareup.moshi.Json

data class UserInfo(
    @Json(name = "about")
    val about: String,
    @Json(name = "anime_stats")
    val animeStats: AnimeStats,
    @Json(name = "birthday")
    val birthday: String,
    @Json(name = "favorites")
    val favorites: Favorites,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "image_url")
    val imageUrl: String,
    @Json(name = "joined")
    val joined: String,
    @Json(name = "last_online")
    val lastOnline: String,
    @Json(name = "location")
    val location: String,
    @Json(name = "manga_stats")
    val mangaStats: MangaStats,
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int,
    @Json(name = "request_cached")
    val requestCached: Boolean,
    @Json(name = "request_hash")
    val requestHash: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "user_id")
    val userId: Int,
    @Json(name = "username")
    val username: String
)