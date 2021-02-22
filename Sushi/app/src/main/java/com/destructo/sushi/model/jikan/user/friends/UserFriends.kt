package com.destructo.sushi.model.jikan.user.friends


import com.squareup.moshi.Json

data class UserFriends(
    @Json(name = "friends")
    val friends: List<Friend?>?=null,
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    val requestHash: String?=null
)