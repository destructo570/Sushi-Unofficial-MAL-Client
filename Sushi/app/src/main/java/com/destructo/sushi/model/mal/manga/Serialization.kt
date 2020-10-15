package com.destructo.sushi.model.mal.manga


import com.squareup.moshi.Json

data class Serialization(
    @Json(name = "node")
    val serializationInfo: SerializationInfo?=null
)