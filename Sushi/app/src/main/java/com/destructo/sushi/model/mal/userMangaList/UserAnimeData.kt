package com.destructo.sushi.model.mal.userMangaList


import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.userMangaList.ListStatus
import com.squareup.moshi.Json

data class UserAnimeData(
    @Json(name = "list_status")
    val listStatus: ListStatus?=null,
    @Json(name = "node")
    val manga: Manga?=null
)