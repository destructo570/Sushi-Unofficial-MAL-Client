package com.destructo.sushi.model.jikan.manga


import com.squareup.moshi.Json

data class Scores(
    @Json(name = "art")
    val art: Int?=null,
    @Json(name = "character")
    val character: Int?=null,
    @Json(name = "enjoyment")
    val enjoyment: Int?=null,
    @Json(name = "overall")
    val overall: Int?=null,
    @Json(name = "story")
    val story: Int?=null
)