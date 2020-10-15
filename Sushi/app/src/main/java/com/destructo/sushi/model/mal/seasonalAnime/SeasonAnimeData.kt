package com.destructo.sushi.model.mal.seasonalAnime


import android.os.Parcelable
import com.destructo.sushi.model.mal.anime.Anime
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SeasonAnimeData(
    @Json(name = "node")
    val anime: Anime?=null
):Parcelable