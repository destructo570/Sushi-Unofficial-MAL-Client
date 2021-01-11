package com.destructo.sushi.model.mal.forum


import com.squareup.moshi.Json

data class ForumTopics(
    @Json(name = "data")
    val topicData: List<TopicData>,
    @Json(name = "paging")
    val paging: Paging
)