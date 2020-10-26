package com.destructo.sushi.model.jikan.manga.character


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MangaCharacter(
    @Json(name = "characters")
    val characters: List<Character?>?=null,
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    val requestHash: String?=null
):Parcelable