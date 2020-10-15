package com.destructo.sushi.model.mal.animeRanking


import android.os.Parcelable
import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnimeRanking(
    @Json(name = "data")
    val data: List<AnimeRankingData?>?=null,
    @Json(name = "paging")
    val paging: Paging?=null
):Parcelable