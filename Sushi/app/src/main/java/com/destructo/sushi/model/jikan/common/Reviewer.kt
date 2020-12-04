package com.destructo.sushi.model.jikan.common


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Reviewer(
    @Json(name = "episodes_seen")
    val episodesSeen: Int?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "scores")
    val reviewScores: ReviewScores?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "username")
    val username: String?=null
):Parcelable