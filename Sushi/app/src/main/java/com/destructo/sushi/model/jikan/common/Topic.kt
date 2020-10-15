package com.destructo.jikanplay.model.common


import com.squareup.moshi.Json

data class Topic(
    @Json(name = "author_name")
    val authorName: String?=null,
    @Json(name = "author_url")
    val authorUrl: String?=null,
    @Json(name = "date_posted")
    val datePosted: String?=null,
    @Json(name = "last_post")
    val lastPost: LastPost?=null,
    @Json(name = "replies")
    val replies: Int?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "topic_id")
    val topicId: Int?=null,
    @Json(name = "url")
    val url: String?=null
)