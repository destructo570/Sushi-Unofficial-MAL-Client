package com.destructo.sushi.model.jikan.user


import com.destructo.sushi.model.jikan.MALSubEntity
import com.squareup.moshi.Json

data class Favorites(
    @Json(name = "anime")
    val anime: List<MALSubEntity>,
    @Json(name = "characters")
    val characters: List<MALSubEntity>,
    @Json(name = "manga")
    val manga: List<MALSubEntity>,
    @Json(name = "people")
    val people: List<MALSubEntity>
)