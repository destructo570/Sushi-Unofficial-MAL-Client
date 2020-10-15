package com.destructo.sushi.model.mal.manga

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class AuthorInfo (
    @Json(name = "first_name")
    val firstName: String?=null,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "last_name")
    val lastName: String?=null
    ):Parcelable