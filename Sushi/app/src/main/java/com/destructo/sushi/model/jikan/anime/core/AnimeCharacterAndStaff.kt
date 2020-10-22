package com.destructo.sushi.model.jikan.anime.core


import com.destructo.sushi.model.jikan.JikanEntity
import com.destructo.sushi.model.jikan.anime.support.Staff
import com.destructo.sushi.model.jikan.character.Character
import com.squareup.moshi.Json

data class AnimeCharacterAndStaff(
    @Json(name = "characters")
    val characters: List<Character?>?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
    @Json(name = "staff")
    val staff: List<Staff?>?=null
): JikanEntity