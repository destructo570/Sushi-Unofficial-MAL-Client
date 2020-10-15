package com.destructo.sushi.model.mal.common


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ranking(
    @Json(name = "rank")
    val rank: Int?=null
):Parcelable