package com.destructo.sushi.model.mal.userAnimeList


import com.destructo.sushi.model.mal.anime.Anime
import com.squareup.moshi.Json

data class UserAnimeData(
    @Json(name = "list_status")
    val animeListStatus: AnimeListStatus?=null,
    @Json(name = "node")
    val anime: Anime?=null
)