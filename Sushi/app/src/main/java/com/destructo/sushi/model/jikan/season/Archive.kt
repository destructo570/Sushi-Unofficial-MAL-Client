package com.destructo.sushi.model.jikan.season


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Archive(
    @Json(name = "seasons")
    val seasons: List<String?>?=null,
    @Json(name = "year")
    val year: Int?=null
): Parcelable