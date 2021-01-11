package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class Post(
    @Json(name = "body")
    val body: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "created_by")
    val createdBy: CreatedBy,
    @Json(name = "id")
    val id: Int,
    @Json(name = "number")
    val number: Int,
    @Json(name = "signature")
    val signature: String
)