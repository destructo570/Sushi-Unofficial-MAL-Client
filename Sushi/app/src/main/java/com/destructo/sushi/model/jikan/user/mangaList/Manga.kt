package com.destructo.sushi.model.jikan.user.mangaList


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.destructo.sushi.model.jikan.MALEntity
import com.squareup.moshi.Json

@Entity(tableName = "jikan_profile_manga_list")
data class Manga(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    @Json(name = "added_to_list")
    val addedToList: Boolean?=null,
    @Json(name = "days")
    val days: Double?=null,
    @Json(name = "end_date")
    val endDate: String?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "is_rereading")
    val isRereading: Boolean?=null,
    @Json(name = "magazines")
    val magazines: List<MALEntity?>?=null,
    @Json(name = "mal_id")
    val malId: Int?=null,
    @Json(name = "priority")
    val priority: String?=null,
    @Json(name = "publishing_status")
    val publishingStatus: Int?=null,
    @Json(name = "read_chapters")
    val readChapters: Int?=null,
    @Json(name = "read_end_date")
    val readEndDate: String?=null,
    @Json(name = "read_start_date")
    val readStartDate: String?=null,
    @Json(name = "read_volumes")
    val readVolumes: Int?=null,
    @Json(name = "reading_status")
    val readingStatus: Int?=null,
    @Json(name = "retail")
    val retail: String?=null,
    @Json(name = "score")
    val score: Int?=null,
    @Json(name = "start_date")
    val startDate: String?=null,
    @Json(name = "tags")
    val tags: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "total_chapters")
    val totalChapters: Int?=null,
    @Json(name = "total_volumes")
    val totalVolumes: Int?=null,
    @Json(name = "type")
    val type: String?=null,
    @Json(name = "url")
    val url: String?=null
)