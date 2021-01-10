package com.destructo.sushi.model.jikan.common


import com.destructo.sushi.model.jikan.common.Date
import com.squareup.moshi.Json

data class Prop(
    @Json(name = "from")
    val date: Date?=null,
    @Json(name = "to")
    val to: Date?=null
)