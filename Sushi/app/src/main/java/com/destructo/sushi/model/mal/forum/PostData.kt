package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class PostData(
    @Json(name = "posts")
    val posts: List<Post>,
    @Json(name = "title")
    val title: String
)