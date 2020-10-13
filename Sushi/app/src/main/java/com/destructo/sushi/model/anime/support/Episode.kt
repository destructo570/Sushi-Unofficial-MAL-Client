package com.destructo.sushi.model.anime.support


import com.squareup.moshi.Json

data class Episode(
    @Json(name = "aired")
    val aired: String?=null,
    @Json(name = "episode_id")
    val episodeId: Int?=null,
    @Json(name = "filler")
    val filler: Boolean?=null,
    @Json(name = "forum_url")
    val forumUrl: String?=null,
    @Json(name = "recap")
    val recap: Boolean?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "title_japanese")
    val titleJapanese: String?=null,
    @Json(name = "title_romanji")
    val titleRomanji: String?=null,
    @Json(name = "video_url")
    val videoUrl: String?=null
)