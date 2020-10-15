package com.destructo.sushi.model.mal.anime


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Broadcast(
    @Json(name = "day_of_the_week")
    val dayOfTheWeek: String?=null,
    @Json(name = "start_time")
    val startTime: String?=null
):Parcelable