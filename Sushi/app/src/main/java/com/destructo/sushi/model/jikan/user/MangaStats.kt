package com.destructo.sushi.model.jikan.user


import com.squareup.moshi.Json

data class MangaStats(
    @Json(name = "chapters_read")
    val chaptersRead: Int?=null,
    @Json(name = "completed")
    val completed: Int?=null,
    @Json(name = "days_read")
    val daysRead: Double?=null,
    @Json(name = "dropped")
    val dropped: Int?=null,
    @Json(name = "mean_score")
    val meanScore: Double?=null,
    @Json(name = "on_hold")
    val onHold: Int?=null,
    @Json(name = "plan_to_read")
    val planToRead: Int?=null,
    @Json(name = "reading")
    val reading: Int?=null,
    @Json(name = "reread")
    val reread: Int?=null,
    @Json(name = "total_entries")
    val totalEntries: Int?=null,
    @Json(name = "volumes_read")
    val volumesRead: Int?=null
)