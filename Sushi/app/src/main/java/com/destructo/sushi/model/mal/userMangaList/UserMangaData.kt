package com.destructo.sushi.model.mal.userMangaList


import com.destructo.sushi.model.mal.manga.Manga
import com.squareup.moshi.Json

data class UserMangaData(
    @Json(name = "list_status")
    val mangaListStatus: MangaListStatus?=null,
    @Json(name = "node")
    val manga: Manga?=null
)