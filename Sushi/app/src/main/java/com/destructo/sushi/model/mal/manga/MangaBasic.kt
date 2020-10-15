package com.destructo.sushi.model.mal.manga

import android.os.Parcelable
import com.destructo.sushi.model.mal.common.MainPicture
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MangaBasic(
    @Json(name = "id")
    val id: Int,
    @Json(name = "main_picture")
    val mainPicture: MainPicture?,
    @Json(name = "title")
    val title: String=""
):Parcelable