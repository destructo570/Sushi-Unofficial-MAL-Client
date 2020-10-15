package com.destructo.sushi.model.jikan.season


import android.os.Parcelable
import com.destructo.sushi.model.jikan.JikanEntity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Season(
    @Json(name = "anime")
    val animeSubEntities: List<AnimeSubEntity?>?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "season_name")
    val seasonName: String?=null,
    @Json(name = "season_year")
    val seasonYear: Int?=null
): JikanEntity, Parcelable