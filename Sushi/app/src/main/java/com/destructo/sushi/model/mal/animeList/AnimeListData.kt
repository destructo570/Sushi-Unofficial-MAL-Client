package com.destructo.sushi.model.mal.animeList


import androidx.room.Entity
import com.destructo.sushi.model.mal.anime.Anime
import com.squareup.moshi.Json

@Entity(tableName = "search_anime_list", primaryKeys = ["anime"])
data class AnimeListData(
    @Json(name = "node")
    val anime: Anime,
)