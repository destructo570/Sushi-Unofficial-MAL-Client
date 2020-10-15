package com.destructo.sushi.model.jikan

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

interface JikanEntity  {

    @Json(name="request_hash")
    val requestHash: String?

    @Json(name="request_cached")
    val requestCached: Boolean?

    @Json(name="request_cache_expiry")
    val requestCacheExpiry: Int?
}