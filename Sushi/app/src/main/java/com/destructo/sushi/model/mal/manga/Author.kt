package com.destructo.sushi.model.mal.manga


import com.squareup.moshi.Json

data class Author(
    @Json(name = "node")
    val authorInfo: AuthorInfo?=null,
    @Json(name = "role")
    val role: String?=null
)