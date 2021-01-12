package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class Board(
    @Json(name = "description")
    val description: String?=null,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "subboards")
    val subboards: List<Subboard?>?=null,
    @Json(name = "title")
    val title: String?=null
)