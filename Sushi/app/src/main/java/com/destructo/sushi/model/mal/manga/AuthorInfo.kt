package com.destructo.sushi.model.mal.manga

import com.squareup.moshi.Json

class AuthorInfo (
    @Json(name = "first_name")
    val firstName: String?=null,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "last_name")
    val lastName: String?=null
    )