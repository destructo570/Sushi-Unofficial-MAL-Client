package com.destructo.sushi.model.jikan.person


import com.destructo.sushi.model.jikan.MALSubEntity
import com.squareup.moshi.Json

data class AnimeStaffPosition(
    @Json(name = "anime")
    val anime: MALSubEntity?=null,
    @Json(name = "position")
    val position: String?=null
)