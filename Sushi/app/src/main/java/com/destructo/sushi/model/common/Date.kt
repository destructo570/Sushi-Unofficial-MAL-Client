package com.destructo.jikanplay.model.common


import com.squareup.moshi.Json

data class Date(
    @Json(name = "day")
    val day: Int?=null,
    @Json(name = "month")
    val month: Int?=null,
    @Json(name = "year")
    val year: Int?=null
)