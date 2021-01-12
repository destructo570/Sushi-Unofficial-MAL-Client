package com.destructo.sushi.model.mal.forum


import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json

data class ForumTopics(
    @Json(name = "data")
    val topicData: List<TopicData?>?=null,
    @Json(name = "paging")
    val paging: Paging?=null
)