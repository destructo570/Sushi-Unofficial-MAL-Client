package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class Post(
    @Json(name = "body")
    val body: String?=null,
    @Json(name = "created_at")
    val createdAt: String?=null,
    @Json(name = "created_by")
    val createdBy: CreatedBy?=null,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "number")
    val number: Int?=null,
    @Json(name = "signature")
    val signature: String?=null
)