package com.destructo.sushi.model.mal.userInfo


import com.squareup.moshi.Json

data class AnimeStatistics(
    @Json(name = "mean_score")
    val meanScore: Double?=null,
    @Json(name = "num_days")
    val numDays: Double?=null,
    @Json(name = "num_days_completed")
    val numDaysCompleted: Double?=null,
    @Json(name = "num_days_dropped")
    val numDaysDropped: Double?=null,
    @Json(name = "num_days_on_hold")
    val numDaysOnHold: Double?=null,
    @Json(name = "num_days_watched")
    val numDaysWatched: Double?=null,
    @Json(name = "num_days_watching")
    val numDaysWatching: Double?=null,
    @Json(name = "num_episodes")
    val numEpisodes: Int?=null,
    @Json(name = "num_items")
    val numItems: Int?=null,
    @Json(name = "num_items_completed")
    val numItemsCompleted: Int?=null,
    @Json(name = "num_items_dropped")
    val numItemsDropped: Int?=null,
    @Json(name = "num_items_on_hold")
    val numItemsOnHold: Int?=null,
    @Json(name = "num_items_plan_to_watch")
    val numItemsPlanToWatch: Int?=null,
    @Json(name = "num_items_watching")
    val numItemsWatching: Int?=null,
    @Json(name = "num_times_rewatched")
    val numTimesRewatched: Int?=null
)