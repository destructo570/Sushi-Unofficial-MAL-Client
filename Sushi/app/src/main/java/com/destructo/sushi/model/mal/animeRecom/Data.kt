package com.destructo.sushi.model.mal.animeRecom


import com.destructo.sushi.model.mal.anime.Anime
import com.squareup.moshi.Json

data class Data(
    @Json(name = "node")
    val anime: Anime?=null
)