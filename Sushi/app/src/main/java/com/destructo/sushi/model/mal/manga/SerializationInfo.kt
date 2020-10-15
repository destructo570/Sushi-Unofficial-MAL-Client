package com.destructo.sushi.model.mal.manga


import com.squareup.moshi.Json

data class SerializationInfo(
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "name")
    val name: String?=null
)