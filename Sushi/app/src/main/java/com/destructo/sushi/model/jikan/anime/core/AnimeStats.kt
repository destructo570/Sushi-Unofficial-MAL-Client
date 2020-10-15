package com.destructo.sushi.model.jikan.anime.core


import com.destructo.sushi.model.jikan.JikanEntity
import com.destructo.jikanplay.model.common.StatScores
import com.squareup.moshi.Json

data class AnimeStats(
    @Json(name = "completed")
    val completed: Int?=null,
    @Json(name = "dropped")
    val dropped: Int?=null,
    @Json(name = "on_hold")
    val onHold: Int?=null,
    @Json(name = "plan_to_watch")
    val planToWatch: Int?=null,
    @Json(name = "scores")
    val statScores: StatScores?=null,
    @Json(name = "total")
    val total: Int?=null,
    @Json(name = "watching")
    val watching: Int?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
): JikanEntity