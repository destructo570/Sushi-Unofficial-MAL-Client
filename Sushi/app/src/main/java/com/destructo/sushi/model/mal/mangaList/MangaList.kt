package com.destructo.sushi.model.mal.mangaList


import com.destructo.sushi.model.mal.common.Paging
import com.squareup.moshi.Json

data class MangaList(
    @Json(name = "data")
    val data: List<MangaListData?>?=null,
    @Json(name = "paging")
    val paging: Paging?=null
)