package com.destructo.sushi.model.mal.userInfo


import com.squareup.moshi.Json

data class UserInfo(
    @Json(name = "anime_statistics")
    val animeStatistics: AnimeStatistics?=null,
    @Json(name = "birthday")
    val birthday: String?=null,
    @Json(name = "gender")
    val gender: String?=null,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "is_supporter")
    val isSupporter: Boolean?=null,
    @Json(name = "joined_at")
    val joinedAt: String?=null,
    @Json(name = "location")
    val location: String?=null,
    @Json(name = "name")
    val name: String?=null,
    @Json(name = "time_zone")
    val timeZone: String?=null,
    @Json(name = "picture")
    val picture: String?=null
)