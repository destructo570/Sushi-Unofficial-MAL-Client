package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class Subboard(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String
)