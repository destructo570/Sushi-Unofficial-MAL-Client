package com.destructo.sushi.model.mal.manga


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Serialization(
    @Json(name = "node")
    val serializationInfo: SerializationInfo?=null
): Parcelable