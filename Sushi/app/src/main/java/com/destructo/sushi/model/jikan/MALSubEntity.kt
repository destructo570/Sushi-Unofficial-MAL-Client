package com.destructo.sushi.model.jikan

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MALSubEntity(
    @Json(name = "image_url")
    val imageUrl: String,
    @Json(name = "mal_id")
    val malId: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
):Parcelable