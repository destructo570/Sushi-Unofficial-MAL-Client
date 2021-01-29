package com.destructo.sushi.model.jikan.user


import com.squareup.moshi.Json

data class MangaStats(
    @Json(name = "chapters_read")
    val chaptersRead: Int,
    @Json(name = "completed")
    val completed: Int,
    @Json(name = "days_read")
    val daysRead: Double,
    @Json(name = "dropped")
    val dropped: Int,
    @Json(name = "mean_score")
    val meanScore: Double,
    @Json(name = "on_hold")
    val onHold: Int,
    @Json(name = "plan_to_read")
    val planToRead: Int,
    @Json(name = "reading")
    val reading: Int,
    @Json(name = "reread")
    val reread: Int,
    @Json(name = "total_entries")
    val totalEntries: Int,
    @Json(name = "volumes_read")
    val volumesRead: Int
)