package com.destructo.sushi.model.jikan.user


import com.destructo.sushi.model.jikan.MALSubEntity
import com.squareup.moshi.Json

data class Favorites(
    @Json(name = "anime")
    val anime: List<MALSubEntity?>?=null,
    @Json(name = "characters")
    val characters: List<MALSubEntity?>?=null,
    @Json(name = "manga")
    val manga: List<MALSubEntity?>?=null,
    @Json(name = "people")
    val people: List<MALSubEntity?>?=null
)