package com.destructo.sushi.model.jikan.anime.core


import com.destructo.sushi.model.jikan.common.Article
import com.destructo.sushi.model.jikan.JikanEntity
import com.squareup.moshi.Json

data class AnimeNews(
    @Json(name = "articles")
    val articles: List<Article?>?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null,
): JikanEntity