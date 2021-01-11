package com.destructo.sushi.model.mal.animeRecom


import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json

data class SuggestedAnime(
    @Json(name = "data")
    val data: List<Data?>?=null,
    @Json(name = "paging")
    val paging: Paging?=null
)