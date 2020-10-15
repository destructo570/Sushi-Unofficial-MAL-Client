package com.destructo.sushi.model.mal.manga


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlternativeTitles(
    @Json(name = "en")
    val en: String?=null,
    @Json(name = "ja")
    val ja: String?=null,
    @Json(name = "synonyms")
    val synonyms: List<String?>?=null
):Parcelable