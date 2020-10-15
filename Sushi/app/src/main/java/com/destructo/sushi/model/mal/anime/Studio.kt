package com.destructo.sushi.model.mal.anime


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Studio(
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "name")
    val name: String?=null
):Parcelable