package com.destructo.sushi.model.mal.anime


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyAnimeListStatus(
    @Json(name = "is_rewatching")
    var isRewatching: Boolean? = null,
    @Json(name = "num_episodes_watched")
    var numEpisodesWatched: Int? = null,
    @Json(name = "score")
    var score: Int? = null,
    @Json(name = "status")
    var status: String? = null,
    @Json(name = "updated_at")
    var updatedAt: String? = null,
    @Json(name = "comments")
    var comments: String? = null,
    @Json(name = "num_times_rewatched")
    var numTimesRewatched: Int? = null,
    @Json(name = "priority")
    var priority: Int? = null,
    @Json(name = "rewatch_value")
    var rewatchValue: Int? = null,
    @Json(name = "tags")
    var tags: List<String?>? = null,
    @Json(name = "start_date")
    val startDate: String? = null,
    @Json(name = "finish_date")
    val finishDate: String? = null,
) : Parcelable