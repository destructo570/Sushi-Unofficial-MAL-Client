package com.destructo.sushi.model.mal.manga

import com.destructo.sushi.model.mal.anime.AnimeBasic
import com.squareup.moshi.Json

data class RelatedAnime(
    @Json(name = "node")
        val manga: AnimeBasic?=null,
    @Json(name = "relation_type")
        val relationType: String?=null,
    @Json(name = "relation_type_formatted")
        val relationTypeFormatted: String?=null
    )
