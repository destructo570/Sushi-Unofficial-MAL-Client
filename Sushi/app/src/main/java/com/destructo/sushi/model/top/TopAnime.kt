package com.destructo.sushi.model.top


import android.os.Parcelable
import com.destructo.sushi.model.JikanEntity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopAnime(
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "top")
    val topAnimeEntity: List<TopAnimeEntity?>?=null
): JikanEntity,Parcelable