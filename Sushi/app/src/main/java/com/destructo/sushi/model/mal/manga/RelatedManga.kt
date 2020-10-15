package com.destructo.sushi.model.mal.manga


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RelatedManga(
    @Json(name = "node")
    val manga: MangaBasic?=null,
    @Json(name = "relation_type")
    val relationType: String?=null,
    @Json(name = "relation_type_formatted")
    val relationTypeFormatted: String?=null
):Parcelable