package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class TopicData(
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "created_by")
    val createdBy: CreatedBy,
    @Json(name = "id")
    val id: Int,
    @Json(name = "is_locked")
    val isLocked: Boolean,
    @Json(name = "last_post_created_at")
    val lastPostCreatedAt: String,
    @Json(name = "last_post_created_by")
    val lastPostCreatedBy: LastPostCreatedBy,
    @Json(name = "number_of_posts")
    val numberOfPosts: Int,
    @Json(name = "title")
    val title: String
)