package com.destructo.sushi.model.mal.seasonalAnime


import com.squareup.moshi.Json

data class Season(
    @Json(name = "season")
    val season: String?=null,
    @Json(name = "year")
    val year: Int?=null
)