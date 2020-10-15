package com.destructo.sushi.model.mal.mangaRanking


import android.os.Parcelable
import com.destructo.sushi.model.mal.common.Ranking
import com.destructo.sushi.model.mal.manga.Manga
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MangaRankingData(
    @Json(name = "node")
    val manga: Manga?=null,
    @Json(name = "ranking")
    val ranking: Ranking?=null
):Parcelable