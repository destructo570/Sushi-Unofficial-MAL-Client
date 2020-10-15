package com.destructo.sushi.model.mal.updateUserMangaList


import com.squareup.moshi.Json

data class UpdateUserManga(
    @Json(name = "comments")
    val comments: String?=null,
    @Json(name = "is_rereading")
    val isRereading: Boolean?=null,
    @Json(name = "num_chapters_read")
    val numChaptersRead: Int?=null,
    @Json(name = "num_times_reread")
    val numTimesReread: Int?=null,
    @Json(name = "num_volumes_read")
    val numVolumesRead: Int?=null,
    @Json(name = "priority")
    val priority: Int?=null,
    @Json(name = "reread_value")
    val rereadValue: Int?=null,
    @Json(name = "score")
    val score: Int?=null,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "tags")
    val tags: List<String?>?=null,
    @Json(name = "updated_at")
    val updatedAt: String?=null
)