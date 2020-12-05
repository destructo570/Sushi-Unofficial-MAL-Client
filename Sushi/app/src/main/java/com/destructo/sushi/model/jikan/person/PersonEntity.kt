package com.destructo.sushi.model.jikan.person


import com.squareup.moshi.Json

data class PersonEntity(
    @Json(name = "about")
    val about: String?=null,
    @Json(name = "alternate_names")
    val alternateNames: List<String?>?=null,
    @Json(name = "anime_staff_positions")
    val animeStaffPositions: List<AnimeStaffPosition>?=null,
    @Json(name = "birthday")
    val birthday: String?=null,
    @Json(name = "family_name")
    val familyName: String?=null,
    @Json(name = "given_name")
    val givenName: String?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "member_favorites")
    val memberFavorites: Int?=null,
    @Json(name = "name")
    val name: String?=null,
    @Json(name = "published_manga")
    val publishedManga: List<String?>?=null,
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    val requestHash: String?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "voice_acting_roles")
    val voiceActingRoles: List<VoiceActingRole?>?=null,
    @Json(name = "website_url")
    val websiteUrl: String?=null
)