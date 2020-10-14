package com.destructo.sushi.model.schedule

import android.os.Parcelable
import com.destructo.sushi.model.JikanEntity
import com.destructo.sushi.model.season.AnimeSubEntity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedule(
    @Json(name = "monday")
    val monday: List<AnimeSubEntity?>?=null,
    @Json(name = "tuesday")
    val tuesday: List<AnimeSubEntity?>?=null,
    @Json(name = "wednesday")
    val wednesday: List<AnimeSubEntity?>?=null,
    @Json(name = "thursday")
    val thursday: List<AnimeSubEntity?>?=null,
    @Json(name = "friday")
    val friday: List<AnimeSubEntity?>?=null,
    @Json(name = "saturday")
    val saturday: List<AnimeSubEntity?>?=null,
    @Json(name = "sunday")
    val sunday: List<AnimeSubEntity?>?=null,
    @Json(name = "other")
    val other: List<AnimeSubEntity?>?=null,
    @Json(name = "unknown")
    val unknown: List<AnimeSubEntity?>?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null
): JikanEntity, Parcelable