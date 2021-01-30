package com.destructo.sushi.model.jikan.user


import android.os.Parcelable
import com.destructo.sushi.model.jikan.MALSubEntity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorites(
    @Json(name = "anime")
    val anime: List<MALSubEntity?>?=null,
    @Json(name = "characters")
    val characters: List<MALSubEntity?>?=null,
    @Json(name = "manga")
    val manga: List<MALSubEntity?>?=null,
    @Json(name = "people")
    val people: List<MALSubEntity?>?=null
): Parcelable