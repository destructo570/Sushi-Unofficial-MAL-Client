package com.destructo.sushi.model.jikan.anime.core


import com.destructo.sushi.model.jikan.JikanEntity
import com.destructo.sushi.model.jikan.anime.support.Episode
import com.squareup.moshi.Json

data class AnimeEpisode(
    @Json(name = "episodes")
    val episodes: List<Episode?>?=null,
    @Json(name = "episodes_last_page")
    val episodesLastPage: Int?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
): JikanEntity