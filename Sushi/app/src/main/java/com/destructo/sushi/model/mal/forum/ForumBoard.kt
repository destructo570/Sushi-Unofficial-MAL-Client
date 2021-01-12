package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class ForumBoard(
    @Json(name = "categories")
    val categories: List<Category?>?=null
)