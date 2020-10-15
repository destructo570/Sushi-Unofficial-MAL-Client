package com.destructo.sushi.model.mal.anime


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Status(
    @Json(name = "completed")
    val completed: String?=null,
    @Json(name = "dropped")
    val dropped: String?=null,
    @Json(name = "on_hold")
    val onHold: String?=null,
    @Json(name = "plan_to_watch")
    val planToWatch: String?=null,
    @Json(name = "watching")
    val watching: String?=null
):Parcelable