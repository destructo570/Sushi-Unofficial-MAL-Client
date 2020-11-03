package com.destructo.sushi.model.mal.manga

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyMangaListStatus(
    @Json(name = "is_rereading")
    val isRereading: Boolean?=null,
    @Json(name = "num_volumes_read")
    val numVolumesRead: Int?=null,
    @Json(name = "num_chapters_read")
    val numChaptersRead: Int?=null,
    @Json(name = "score")
    val score: Int?=null,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "updated_at")
    val updatedAt: String?=null,
    @Json(name = "priority")
    val priority: Int?=null,
    @Json(name = "num_times_reread")
    val num_times_reread: Int?=null,
    @Json(name = "reread_value")
    val reread_value: Int?=null,
    @Json(name = "tags")
    val tags: List<String?>?=null,
    @Json(name = "comments")
    val comments: String?=null
): Parcelable