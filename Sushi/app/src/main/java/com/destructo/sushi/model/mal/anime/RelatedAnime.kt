package com.destructo.sushi.model.mal.anime


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RelatedAnime(
    @Json(name = "node")
    val anime: AnimeBasic?=null,
    @Json(name = "relation_type")
    val relationType: String?=null,
    @Json(name = "relation_type_formatted")
    val relationTypeFormatted: String?=null
):Parcelable