package com.destructo.sushi.model.database

import androidx.room.Entity
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff

@Entity(tableName = "anime_character_list", primaryKeys = ["id"])
data class AnimeCharacterListEntity(
    val characterAndStaffList: AnimeCharacterAndStaff?,
    val id:Int,
    override val time: Long
):BaseDatabaseEntity