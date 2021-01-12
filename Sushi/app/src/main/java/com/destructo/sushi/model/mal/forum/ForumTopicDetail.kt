package com.destructo.sushi.model.mal.forum


import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json

data class ForumTopicDetail(
    @Json(name = "data")
    val postData: PostData?=null,
    @Json(name = "paging")
    val paging: Paging?=null
)