package com.destructo.sushi.model.jikan.season


import android.os.Parcelable
import com.destructo.sushi.model.jikan.MALEntity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnimeSubEntity(
    @Json(name = "airing_start")
    val airingStart: String?=null,
    @Json(name = "continuing")
    val continuing: Boolean?=null,
    @Json(name = "episodes")
    val episodes: Int?=null,
    @Json(name = "genres")
    val genres: List<MALEntity?>?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "kids")
    val kids: Boolean?=null,
    @Json(name = "licensors")
    val licensors: List<String?>?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "members")
    val members: Int?=null,
    @Json(name = "producers")
    val producers: List<MALEntity?>?=null,
    @Json(name = "r18")
    val r18: Boolean?=null,
    @Json(name = "score")
    val score: Double?=null,
    @Json(name = "source")
    val source: String?=null,
    @Json(name = "synopsis")
    val synopsis: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "type")
    val type: String?=null,
    @Json(name = "url")
    val url: String?=null
): Parcelable