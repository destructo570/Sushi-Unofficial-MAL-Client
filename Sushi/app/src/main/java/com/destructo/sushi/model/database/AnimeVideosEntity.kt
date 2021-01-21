package com.destructo.sushi.model.database

import androidx.room.Entity
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo


@Entity(tableName = "anime_videos", primaryKeys = ["id"])
data class AnimeVideosEntity(
    val videosAndEpisodes: AnimeVideo?,
    val id:Int,
    override val time: Long,
): BaseDatabaseEntity