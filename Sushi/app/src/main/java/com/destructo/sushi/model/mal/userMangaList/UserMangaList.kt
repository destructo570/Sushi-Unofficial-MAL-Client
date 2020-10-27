package com.destructo.sushi.model.mal.userMangaList


import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json

data class UserMangaList(
    @Json(name = "data")
    val data: List<UserMangaData?>?=null,
    @Json(name = "paging")
    val paging: Paging?=null
)