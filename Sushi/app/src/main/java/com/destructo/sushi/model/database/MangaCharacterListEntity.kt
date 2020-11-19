package com.destructo.sushi.model.database

import androidx.room.Entity
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.mal.manga.Manga

@Entity(tableName = "manga_characters_list", primaryKeys = ["id"])
data class MangaCharacterListEntity(
    val mangaCharacterList: MangaCharacter?,
    val id:Int,
    val time: Long
)