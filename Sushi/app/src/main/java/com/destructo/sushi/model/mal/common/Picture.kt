package com.destructo.sushi.model.mal.common

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Picture(
    @Json(name = "large")
    val large: String?=null,
    @Json(name = "medium")
    val medium: String?=null
):Parcelable