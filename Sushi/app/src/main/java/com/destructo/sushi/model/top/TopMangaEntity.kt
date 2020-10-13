package com.destructo.sushi.model.top


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopMangaEntity(
    @Json(name = "end_date")
    val endDate: String?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "members")
    val members: Int?=null,
    @Json(name = "rank")
    val rank: Int?=null,
    @Json(name = "score")
    val score: Double?=null,
    @Json(name = "start_date")
    val startDate: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "type")
    val type: String?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "volumes")
    val volumes: Int?=null
):Parcelable