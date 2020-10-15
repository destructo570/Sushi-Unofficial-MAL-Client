package com.destructo.sushi.model.mal.anime


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StartSeason(
    @Json(name = "season")
    val season: String?=null,
    @Json(name = "year")
    val year: Int?=null
):Parcelable