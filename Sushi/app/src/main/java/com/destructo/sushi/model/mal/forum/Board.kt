package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class Board(
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "subboards")
    val subboards: List<Subboard>,
    @Json(name = "title")
    val title: String
)