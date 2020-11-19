package com.destructo.sushi.model.database

import androidx.room.Entity
import com.destructo.sushi.model.mal.manga.Manga

@Entity(tableName = "manga_details", primaryKeys = ["id"])
data class MangaDetailsEntity(
    val manga: Manga?,
    val id:Int,
    val time: Long
)