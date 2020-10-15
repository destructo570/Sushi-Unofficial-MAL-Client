package com.destructo.sushi.model.mal.anime

import android.os.Parcelable
import com.destructo.sushi.model.mal.common.MainPicture
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnimeBasic (
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "main_picture")
    val mainPicture: MainPicture?=null,
    @Json(name = "title")
    val title: String?=null
):Parcelable