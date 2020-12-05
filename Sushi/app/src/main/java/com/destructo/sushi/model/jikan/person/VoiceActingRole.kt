package com.destructo.sushi.model.jikan.person


import com.destructo.sushi.model.jikan.MALSubEntity
import com.squareup.moshi.Json

data class VoiceActingRole(
    @Json(name = "anime")
    val anime: MALSubEntity?=null,
    @Json(name = "character")
    val character: MALSubEntity?=null,
    @Json(name = "role")
    val role: String?=null
)