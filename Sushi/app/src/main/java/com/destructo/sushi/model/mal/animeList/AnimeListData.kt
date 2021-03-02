package com.destructo.sushi.model.mal.animeList


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.destructo.sushi.model.mal.anime.Anime
import com.squareup.moshi.Json

@Entity(tableName = "search_anime_list")
data class AnimeListData(
    @Json(name = "node")
    val anime: Anime,
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null
)