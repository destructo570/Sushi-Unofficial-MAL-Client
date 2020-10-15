package com.destructo.sushi.model.mal.mangaRanking


import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json

data class MangaRanking(
    @Json(name = "data")
    val data: List<MangaRankingData?>?=null,
    @Json(name = "paging")
    val paging: Paging?=null
)