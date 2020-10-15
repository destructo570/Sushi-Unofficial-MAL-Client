package com.destructo.sushi.model.mal.userAnimeList


import com.destructo.sushi.model.mal.common.Paging
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData
import com.squareup.moshi.Json

data class UserAnimeList(
    @Json(name = "data")
    val data: List<UserAnimeData?>?=null,
    @Json(name = "paging")
    val paging: Paging?=null
)