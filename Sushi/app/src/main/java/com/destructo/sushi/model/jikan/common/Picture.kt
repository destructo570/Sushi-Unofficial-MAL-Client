package com.destructo.sushi.model.jikan.common


import com.squareup.moshi.Json

data class Picture(
    @Json(name = "large")
    val large: String?=null,
    @Json(name = "small")
    val small: String?=null
)