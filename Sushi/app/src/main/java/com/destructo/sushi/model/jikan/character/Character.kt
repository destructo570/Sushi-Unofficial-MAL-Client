package com.destructo.sushi.model.jikan.character


import android.os.Parcelable
import com.destructo.sushi.model.jikan.anime.support.VoiceActor
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character(
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "name")
    val name: String?=null,
    @Json(name = "role")
    val role: String?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "voice_actors")
    val voiceActors: List<VoiceActor?>?=null,
    @Json(name = "about")
    val about: String?=null,
    @Json(name = "animeography")
    val animeography: List<PersonEntity?>?=null,
    @Json(name = "mangaography")
    val mangaography: List<PersonEntity?>?=null,
    @Json(name = "member_favorites")
    val memberFavorites: Int?=null,
    @Json(name = "name_kanji")
    val nameKanji: String?=null,
    @Json(name = "nicknames")
    val nicknames: List<String?>?=null,
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int?=null,
    @Json(name = "request_cached")
    val requestCached: Boolean?=null,
    @Json(name = "request_hash")
    val requestHash: String?=null,
):Parcelable