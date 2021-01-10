package com.destructo.sushi.model.jikan.anime.core


import com.destructo.sushi.model.jikan.common.TimeInterval
import com.destructo.sushi.model.jikan.JikanEntity
import com.destructo.sushi.model.jikan.MALEntity
import com.destructo.sushi.model.jikan.anime.support.RelatedAnime
import com.squareup.moshi.Json

data class Anime(
    @Json(name = "aired")
    val aired: TimeInterval?=null,
    @Json(name = "airing")
    val airing: Boolean?=null,
    @Json(name = "background")
    val background: String?=null,
    @Json(name = "broadcast")
    val broadcast: String?=null,
    @Json(name = "duration")
    val duration: String?=null,
    @Json(name = "ending_themes")
    val endingThemes: List<String?>?=null,
    @Json(name = "episodes")
    val episodes: Int?=null,
    @Json(name = "favorites")
    val favorites: Int?=null,
    @Json(name = "genres")
    val genres: List<MALEntity?>?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "licensors")
    val licensors: List<MALEntity?>?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "members")
    val members: Int?=null,
    @Json(name = "opening_themes")
    val openingThemes: List<String?>?=null,
    @Json(name = "popularity")
    val popularity: Int?=null,
    @Json(name = "premiered")
    val premiered: String?=null,
    @Json(name = "producers")
    val producers: List<MALEntity?>?=null,
    @Json(name = "rank")
    val rank: Int?=null,
    @Json(name = "rating")
    val rating: String?=null,
    @Json(name = "related")
    val relatedAnime: RelatedAnime?=null,
    @Json(name = "score")
    val score: Double?=null,
    @Json(name = "scored_by")
    val scoredBy: Int?=null,
    @Json(name = "source")
    val source: String?=null,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "studios")
    val studios: List<MALEntity?>?=null,
    @Json(name = "synopsis")
    val synopsis: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "title_english")
    val titleEnglish: String?=null,
    @Json(name = "title_japanese")
    val titleJapanese: String?=null,
    @Json(name = "title_synonyms")
    val titleSynonyms: List<String?>?=null,
    @Json(name = "trailer_url")
    val trailerUrl: String?=null,
    @Json(name = "type")
    val type: String?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "request_hash")
    override val requestHash: String?=null,
    @Json(name = "request_cached")
    override val requestCached: Boolean?=null,
    @Json(name = "request_cache_expiry")
    override val requestCacheExpiry: Int?=null
): JikanEntity