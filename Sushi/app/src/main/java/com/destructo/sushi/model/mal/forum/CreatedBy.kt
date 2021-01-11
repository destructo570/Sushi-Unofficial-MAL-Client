package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class CreatedBy(
    @Json(name = "forum_avator")
    val forumAvator: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
)