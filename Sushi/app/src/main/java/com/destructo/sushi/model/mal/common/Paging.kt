package com.destructo.sushi.model.mal.common

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Paging(
    @Json(name = "next")
    val next: String?=null,
    @Json(name = "previous")
    val previous: String?=null
):Parcelable