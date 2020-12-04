package com.destructo.sushi.model.jikan.common


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
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
):Parcelable