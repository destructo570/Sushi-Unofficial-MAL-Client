package com.destructo.sushi.model.jikan.user


import com.squareup.moshi.Json

data class AnimeStats(
    @Json(name = "completed")
    val completed: Int,
    @Json(name = "days_watched")
    val daysWatched: Double,
    @Json(name = "dropped")
    val dropped: Int,
    @Json(name = "episodes_watched")
    val episodesWatched: Int,
    @Json(name = "mean_score")
    val meanScore: Double,
    @Json(name = "on_hold")
    val onHold: Int,
    @Json(name = "plan_to_watch")
    val planToWatch: Int,
    @Json(name = "rewatched")
    val rewatched: Int,
    @Json(name = "total_entries")
    val totalEntries: Int,
    @Json(name = "watching")
    val watching: Int
)