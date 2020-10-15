package com.destructo.sushi.model.mal.seasonalAnime


import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json

data class SeasonalAnime(
    @Json(name = "data")
    val data: List<SeasonAnimeData>?,
    @Json(name = "paging")
    val paging: Paging?,
    @Json(name = "season")
    val season: Season?
)