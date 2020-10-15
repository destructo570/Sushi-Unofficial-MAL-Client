package com.destructo.sushi.model.mal.animeList


import com.destructo.sushi.model.mal.anime.Anime
import com.squareup.moshi.Json

data class AnimeListData(
    @Json(name = "node")
    val anime: Anime?=null
)