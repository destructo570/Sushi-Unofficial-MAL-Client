package com.destructo.sushi.model.mal.manga

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyMangaListStatus(
    @Json(name = "is_rereading")
    var isRereading: Boolean?=null,
    @Json(name = "num_volumes_read")
    var numVolumesRead: Int?=null,
    @Json(name = "num_chapters_read")
    var numChaptersRead: Int?=null,
    @Json(name = "score")
    var score: Int?=null,
    @Json(name = "status")
    var status: String?=null,
    @Json(name = "updated_at")
    var updatedAt: String?=null,
    @Json(name = "priority")
    var priority: Int?=null,
    @Json(name = "num_times_reread")
    var num_times_reread: Int?=null,
    @Json(name = "reread_varue")
    var reread_value: Int?=null,
    @Json(name = "tags")
    var tags: List<String?>?=null,
    @Json(name = "comments")
    var comments: String?=null,
    @Json(name = "start_date")
    var startDate: String? = null,
    @Json(name = "finish_date")
    var finishDate: String? = null
): Parcelable