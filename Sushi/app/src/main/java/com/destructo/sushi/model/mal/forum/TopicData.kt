package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class TopicData(
    @Json(name = "created_at")
    val createdAt: String?=null,
    @Json(name = "created_by")
    val createdBy: CreatedBy?=null,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "is_locked")
    val isLocked: Boolean?=null,
    @Json(name = "last_post_created_at")
    val lastPostCreatedAt: String?=null,
    @Json(name = "last_post_created_by")
    val lastPostCreatedBy: LastPostCreatedBy?=null,
    @Json(name = "number_of_posts")
    val numberOfPosts: Int?=null,
    @Json(name = "title")
    val title: String?=null
)