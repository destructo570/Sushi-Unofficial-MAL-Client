package com.destructo.sushi.model.params

data class MangaUpdateParams (
    val mangaId: String,
    val status: String? = null,
    val is_rereading: Boolean? = null,
    val score: Int? = null,
    val num_volumes_read: Int? = null,
    val num_chapters_read: Int? = null,
    val priority: Int? = null,
    val num_times_reread: Int? = null,
    val reread_value: Int? = null,
    val tags: List<String>? = null,
    val comments: String? = null,
    val start_date:String?=null,
    val finish_date:String?=null
)