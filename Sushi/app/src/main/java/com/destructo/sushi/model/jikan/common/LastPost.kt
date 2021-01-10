package com.destructo.sushi.model.jikan.common


import com.squareup.moshi.Json

data class LastPost(
    @Json(name = "author_name")
    val authorName: String?=null,
    @Json(name = "author_url")
    val authorUrl: String?=null,
    @Json(name = "date_posted")
    val datePosted: String?=null,
    @Json(name = "url")
    val url: String?=null
)