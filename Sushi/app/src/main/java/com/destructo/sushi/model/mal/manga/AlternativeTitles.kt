package com.destructo.sushi.model.mal.manga


import com.squareup.moshi.Json

data class AlternativeTitles(
    @Json(name = "en")
    val en: String?=null,
    @Json(name = "ja")
    val ja: String?=null,
    @Json(name = "synonyms")
    val synonyms: List<String?>?=null
)