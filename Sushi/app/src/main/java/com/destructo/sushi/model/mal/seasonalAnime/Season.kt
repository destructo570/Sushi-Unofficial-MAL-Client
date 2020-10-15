package com.destructo.sushi.model.mal.seasonalAnime


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Season(
    @Json(name = "season")
    val season: String?=null,
    @Json(name = "year")
    val year: Int?=null

):Parcelable