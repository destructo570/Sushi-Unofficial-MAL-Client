package com.destructo.sushi.model.jikan.common

import com.squareup.moshi.Json


data class TimeInterval(
    @Json(name = "from")
    val from: String?=null,
    @Json(name = "prop")
    val prop: Prop?=null,
    @Json(name = "string")
    val string: String?=null,
    @Json(name = "to")
    val to: String?=null
)