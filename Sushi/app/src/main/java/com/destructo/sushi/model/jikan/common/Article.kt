package com.destructo.sushi.model.jikan.common


import com.squareup.moshi.Json

data class Article(
    @Json(name = "author_name")
    val authorName: String?=null,
    @Json(name = "author_url")
    val authorUrl: String?=null,
    @Json(name = "comments")
    val comments: Int?=null,
    @Json(name = "date")
    val date: String?=null,
    @Json(name = "forum_url")
    val forumUrl: String?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "intro")
    val intro: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "url")
    val url: String?=null
)