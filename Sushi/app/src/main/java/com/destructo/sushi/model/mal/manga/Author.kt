package com.destructo.sushi.model.mal.manga


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Author(
    @Json(name = "node")
    val authorInfo: AuthorInfo?=null,
    @Json(name = "role")
    val role: String?=null
):Parcelable