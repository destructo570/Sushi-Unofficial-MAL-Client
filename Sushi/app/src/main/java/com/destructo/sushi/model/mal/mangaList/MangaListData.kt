package com.destructo.sushi.model.mal.mangaList


import com.destructo.sushi.model.mal.manga.Manga
import com.squareup.moshi.Json

data class MangaListData(
    @Json(name = "node")
    val manga: Manga?=null
)