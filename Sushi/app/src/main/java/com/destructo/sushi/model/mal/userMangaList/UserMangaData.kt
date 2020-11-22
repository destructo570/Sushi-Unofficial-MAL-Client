package com.destructo.sushi.model.mal.userMangaList


import androidx.room.Entity
import com.destructo.sushi.model.mal.manga.Manga
import com.squareup.moshi.Json

@Entity(tableName = "user_manga_list", primaryKeys = ["manga"])
data class UserMangaData(
    @Json(name = "list_status")
    val mangaListStatus: MangaListStatus?=null,
    @Json(name = "node")
    val manga: Manga,
    var status: String?,
    var title: String?
)