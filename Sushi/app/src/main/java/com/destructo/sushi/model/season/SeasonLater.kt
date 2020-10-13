package com.destructo.sushi.model.season


import com.destructo.sushi.model.JikanEntity
import com.destructo.sushi.model.season.AnimeSubEntity
import com.squareup.moshi.Json

data class SeasonLater(
    @Json(name = "anime")
    val anime: List<AnimeSubEntity?>?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "season_name")
    val seasonName: String?=null,
    @Json(name = "season_year")
    val seasonYear: String?=null
): JikanEntity