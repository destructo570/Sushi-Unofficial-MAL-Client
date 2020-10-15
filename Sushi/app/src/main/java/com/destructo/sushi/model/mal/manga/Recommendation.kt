package com.destructo.sushi.model.mal.manga


import com.destructo.sushi.model.mal.manga.MangaBasic
import com.squareup.moshi.Json

data class Recommendation(
    @Json(name = "node")
    val manga: MangaBasic?=null,
    @Json(name = "num_recommendations")
    val numRecommendations: Int?=null
)