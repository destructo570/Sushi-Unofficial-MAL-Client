package com.destructo.sushi.model.mal.anime


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recommendation(
    @Json(name = "node")
    val anime: AnimeBasic?=null,
    @Json(name = "num_recommendations")
    val numRecommendations: Int?=null
):Parcelable