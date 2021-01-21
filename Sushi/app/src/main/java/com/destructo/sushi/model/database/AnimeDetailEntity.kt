package com.destructo.sushi.model.database

import androidx.room.Entity
import com.destructo.sushi.model.mal.anime.Anime

@Entity(tableName = "anime_detail", primaryKeys = ["id"])
data class AnimeDetailEntity(
    val anime: Anime?,
    val id:Int,
    override val time: Long,
): BaseDatabaseEntity