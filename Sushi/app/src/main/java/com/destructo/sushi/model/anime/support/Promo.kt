package com.destructo.sushi.model.anime.support


import com.squareup.moshi.Json

data class Promo(
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "video_url")
    val videoUrl: String?=null
)