package com.destructo.sushi.model.database

import androidx.room.Entity
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.mal.anime.Anime

@Entity(tableName = "anime_character_list", primaryKeys = ["id"])
data class AnimeCharacterListEntity(
    val characterAndStaffList: AnimeCharacterAndStaff?,
    val id:Int,
    val time: Long
)