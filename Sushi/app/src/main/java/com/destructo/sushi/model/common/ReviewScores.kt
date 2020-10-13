package com.destructo.jikanplay.model.common


import com.squareup.moshi.Json

data class ReviewScores(
    @Json(name = "art")
    val art: Int?=null,
    @Json(name = "animation")
    val animation: Int?=null,
    @Json(name = "character")
    val character: Int?=null,
    @Json(name = "enjoyment")
    val enjoyment: Int?=null,
    @Json(name = "overall")
    val overall: Int?=null,
    @Json(name = "sound")
    val sound: Int?=null,
    @Json(name = "story")
    val story: Int?=null
)