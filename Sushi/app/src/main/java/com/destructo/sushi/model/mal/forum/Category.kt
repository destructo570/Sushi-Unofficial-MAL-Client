package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class Category(
    @Json(name = "boards")
    val boards: List<Board?>?=null,
    @Json(name = "title")
    val title: String?=null
)