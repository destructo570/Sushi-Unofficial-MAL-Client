package com.destructo.sushi.model.mal.seasonalAnime


import android.os.Parcelable
import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SeasonalAnime(
    @Json(name = "data")
    val data: List<SeasonAnimeData?>?=null,
    @Json(name = "paging")
    val paging: Paging?=null,
    @Json(name = "season")
    val season: Season?=null
): Parcelable