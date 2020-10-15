package com.destructo.sushi.model.mal.mangaRanking


import android.os.Parcelable
import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MangaRanking(
    @Json(name = "data")
    val data: List<MangaRankingData?>?=null,
    @Json(name = "paging")
    val paging: Paging?=null
):Parcelable