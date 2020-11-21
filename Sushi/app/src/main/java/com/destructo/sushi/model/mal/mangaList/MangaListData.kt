package com.destructo.sushi.model.mal.mangaList


import androidx.room.Entity
import com.destructo.sushi.model.mal.manga.Manga
import com.squareup.moshi.Json

@Entity(tableName = "search_manga_list", primaryKeys = ["manga"])
data class MangaListData(
    @Json(name = "node")
    val manga: Manga
)