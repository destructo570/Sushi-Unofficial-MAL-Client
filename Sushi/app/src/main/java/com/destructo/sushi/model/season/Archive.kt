package com.destructo.sushi.model.season


import com.squareup.moshi.Json

data class Archive(
    @Json(name = "seasons")
    val seasons: List<String?>?=null,
    @Json(name = "year")
    val year: Int?=null
)