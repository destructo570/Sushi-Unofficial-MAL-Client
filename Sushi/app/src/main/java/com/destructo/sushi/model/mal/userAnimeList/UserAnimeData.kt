package com.destructo.sushi.model.mal.userAnimeList


import androidx.room.Entity
import com.destructo.sushi.model.mal.anime.Anime
import com.squareup.moshi.Json
@Entity(tableName = "user_anime_list", primaryKeys = ["anime"])
data class UserAnimeData(
    @Json(name = "list_status")
    val animeListStatus: AnimeListStatus?=null,
    @Json(name = "node")
    val anime: Anime,
    var status: String?,
    var title: String?

)