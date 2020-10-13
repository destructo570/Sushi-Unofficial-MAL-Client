package com.destructo.jikanplay.model.common


import com.destructo.sushi.model.anime.support.VoiceActor
import com.squareup.moshi.Json

data class Character(
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "name")
    val name: String?=null,
    @Json(name = "role")
    val role: String?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "voice_actors")
    val voiceActors: List<VoiceActor?>?=null
)