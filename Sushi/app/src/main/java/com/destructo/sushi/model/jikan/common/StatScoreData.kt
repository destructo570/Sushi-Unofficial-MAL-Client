package com.destructo.sushi.model.jikan.common

import com.squareup.moshi.Json

class StatScoreData (
    @Json(name = "percentage")
    val percentage: Double,
    @Json(name = "votes")
    val votes: Int
)