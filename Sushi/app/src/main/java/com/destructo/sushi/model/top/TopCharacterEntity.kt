package com.destructo.sushi.model.top


import com.destructo.sushi.model.MALEntity
import com.squareup.moshi.Json

data class TopCharacterEntity(
    @Json(name = "animeography")
    val animeography: List<MALEntity?>?=null,
    @Json(name = "favorites")
    val favorites: Int?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "mangaography")
    val mangaography: List<MALEntity?>?=null,
    @Json(name = "name_kanji")
    val nameKanji: String?=null,
    @Json(name = "rank")
    val rank: Int?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "url")
    val url: String?=null
)