package com.destructo.sushi.model.anime.core


import com.destructo.sushi.model.JikanEntity
import com.destructo.sushi.model.anime.support.EpisodeVideo
import com.destructo.sushi.model.anime.support.Promo
import com.squareup.moshi.Json

data class AnimeVideo(
    @Json(name = "episodes")
    val episodeVideos: List<EpisodeVideo?>?=null,
    @Json(name = "promo")
    val promo: List<Promo?>?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
): JikanEntity