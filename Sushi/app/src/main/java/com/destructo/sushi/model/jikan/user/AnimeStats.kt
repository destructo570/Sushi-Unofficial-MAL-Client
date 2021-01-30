package com.destructo.sushi.model.jikan.user


import com.squareup.moshi.Json

data class AnimeStats(
    @Json(name = "completed")
    val completed: Int?=null,
    @Json(name = "days_watched")
    val daysWatched: Double?=null,
    @Json(name = "dropped")
    val dropped: Int?=null,
    @Json(name = "episodes_watched")
    val episodesWatched: Int?=null,
    @Json(name = "mean_score")
    val meanScore: Double?=null,
    @Json(name = "on_hold")
    val onHold: Int?=null,
    @Json(name = "plan_to_watch")
    val planToWatch: Int?=null,
    @Json(name = "rewatched")
    val rewatched: Int?=null,
    @Json(name = "total_entries")
    val totalEntries: Int?=null,
    @Json(name = "watching")
    val watching: Int?=null
)