package com.destructo.sushi.model


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MALEntity (
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "name")
    val name: String?=null,
    @Json(name = "type")
    val type: String?=null,
    @Json(name = "url")
    val url: String?=null
):Parcelable