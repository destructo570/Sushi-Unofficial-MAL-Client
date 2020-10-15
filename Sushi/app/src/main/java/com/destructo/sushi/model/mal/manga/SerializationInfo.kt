package com.destructo.sushi.model.mal.manga


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SerializationInfo(
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "name")
    val name: String?=null
): Parcelable