package com.destructo.sushi.model.mal.seasonalAnime


import com.destructo.sushi.model.mal.anime.Anime
import com.squareup.moshi.Json

data class SeasonAnimeData(
    @Json(name = "node")
    val anime: Anime?=null
)