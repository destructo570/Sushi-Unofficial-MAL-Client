package com.destructo.sushi.model.jikan.manga

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class ReviewEntity(
    @Json(name = "content")
    val content: String?=null,
    @Json(name = "date")
    val date: String?=null,
    @Json(name = "helpful_count")
    val helpfulCount: Int?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "reviewer")
    val reviewer: Reviewer?=null,
    @Json(name = "type")
    val type: String?=null,
    @Json(name = "url")
    val url: String?=null
):Parcelable