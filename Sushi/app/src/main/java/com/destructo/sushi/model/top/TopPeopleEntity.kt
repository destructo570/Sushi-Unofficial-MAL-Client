package com.destructo.sushi.model.top


import com.squareup.moshi.Json

data class TopPeopleEntity(
    @Json(name = "birthday")
    val birthday: String,
    @Json(name = "favorites")
    val favorites: Int,
    @Json(name = "image_url")
    val imageUrl: String,
    @Json(name = "mal_id")
    val malId: Int,
    @Json(name = "name_kanji")
    val nameKanji: String,
    @Json(name = "rank")
    val rank: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "url")
    val url: String
)