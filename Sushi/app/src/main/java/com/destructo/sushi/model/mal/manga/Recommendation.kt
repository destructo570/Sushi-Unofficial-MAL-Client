package com.destructo.sushi.model.mal.manga


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recommendation(
    @Json(name = "node")
    val manga: MangaBasic?=null,
    @Json(name = "num_recommendations")
    val numRecommendations: Int?=null
):Parcelable