package com.destructo.sushi.model.mal.anime


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Statistics(
    @Json(name = "num_list_users")
    val numListUsers: Int?=null,
    @Json(name = "status")
    val status: Status?=null
):Parcelable