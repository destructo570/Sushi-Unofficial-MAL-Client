package com.destructo.sushi.model.mal.mangaList


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.destructo.sushi.model.mal.manga.Manga
import com.squareup.moshi.Json

@Entity(tableName = "search_manga_list")
data class MangaListData(
    @Json(name = "node")
    val manga: Manga,
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null
)