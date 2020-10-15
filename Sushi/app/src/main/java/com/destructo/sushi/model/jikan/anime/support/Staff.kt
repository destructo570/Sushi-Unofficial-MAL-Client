package com.destructo.sushi.model.jikan.anime.support


import com.squareup.moshi.Json

data class Staff(
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "name")
    val name: String?=null,
    @Json(name = "positions")
    val positions: List<String?>?=null,
    @Json(name = "url")
    val url: String?=null
)