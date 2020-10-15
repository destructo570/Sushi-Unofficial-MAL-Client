package com.destructo.sushi.model.mal.animeRanking


import android.os.Parcelable
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.common.Ranking
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnimeRankingData(
    @Json(name = "node")
    val anime: Anime?=null,
    @Json(name = "ranking")
    val ranking: Ranking?=null
):Parcelable