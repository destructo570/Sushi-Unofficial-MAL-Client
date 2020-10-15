package com.destructo.sushi.model.mal.manga


import com.destructo.sushi.model.mal.common.Genre
import com.destructo.sushi.model.mal.common.MainPicture
import com.destructo.sushi.model.mal.common.Picture
import com.squareup.moshi.Json

data class Manga(
    @Json(name = "alternative_titles")
    val alternativeTitles: AlternativeTitles?=null,
    @Json(name = "authors")
    val authors: List<Author?>?=null,
    @Json(name = "background")
    val background: String?=null,
    @Json(name = "created_at")
    val createdAt: String?=null,
    @Json(name = "genres")
    val genres: List<Genre?>?=null,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "main_picture")
    val mainPicture: MainPicture?=null,
    @Json(name = "mean")
    val mean: Double?=null,
    @Json(name = "media_type")
    val mediaType: String?=null,
    @Json(name = "nsfw")
    val nsfw: String?=null,
    @Json(name = "num_chapters")
    val numChapters: Int?=null,
    @Json(name = "num_list_users")
    val numListUsers: Int?=null,
    @Json(name = "num_scoring_users")
    val numScoringUsers: Int?=null,
    @Json(name = "num_volumes")
    val numVolumes: Int?=null,
    @Json(name = "pictures")
    val pictures: List<Picture?>?=null,
    @Json(name = "popularity")
    val popularity: Int?=null,
    @Json(name = "rank")
    val rank: Int?=null,
    @Json(name = "recommendations")
    val recommendations: List<Recommendation?>?=null,
    @Json(name = "related_anime")
    val relatedAnime: List<RelatedAnime?>?=null,
    @Json(name = "related_manga")
    val relatedManga: List<RelatedManga?>?=null,
    @Json(name = "serialization")
    val serialization: List<Serialization?>?=null,
    @Json(name = "start_date")
    val startDate: String?=null,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "synopsis")
    val synopsis: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "updated_at")
    val updatedAt: String?=null
)