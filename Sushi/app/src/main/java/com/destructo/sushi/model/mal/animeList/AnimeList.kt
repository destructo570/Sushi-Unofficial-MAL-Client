package com.destructo.sushi.model.mal.animeList


import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json

data class AnimeList(
    @Json(name = "data")
    val data: List<AnimeListData?>?=null,
    @Json(name = "paging")
    val paging: Paging?=null
)