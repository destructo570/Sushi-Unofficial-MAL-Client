package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class LastPostCreatedBy(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
)