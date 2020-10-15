package com.destructo.sushi.model.mal.userMangaList


import com.squareup.moshi.Json

data class ListStatus(
    @Json(name = "is_rereading")
    val isRereading: Boolean?=null,
    @Json(name = "num_chapters_read")
    val numChaptersRead: Int?=null,
    @Json(name = "num_volumes_read")
    val numVolumesRead: Int?=null,
    @Json(name = "score")
    val score: Int?=null,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "updated_at")
    val updatedAt: String?=null
)