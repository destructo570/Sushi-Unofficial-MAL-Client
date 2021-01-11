package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class Paging(
    @Json(name = "next")
    val next: String
)