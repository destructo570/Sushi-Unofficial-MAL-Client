package com.destructo.sushi.model.jikan.manga


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
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
): Parcelable