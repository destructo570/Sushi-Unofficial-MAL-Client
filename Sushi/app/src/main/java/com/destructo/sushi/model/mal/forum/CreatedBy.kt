package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class CreatedBy(
    @Json(name = "forum_avator")
    val forumAvator: String?=null,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "name")
    val name: String?=null
)