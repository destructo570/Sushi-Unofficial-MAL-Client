package com.destructo.sushi.model.params


data class AnimeUpdateParams(
    val animeId:String,
    val status:String?=null,
    val is_rewatching:Boolean?=null,
    val score:Int?=null,
    val num_watched_episodes:Int?=null,
    val priority:Int?=null,
    val num_times_rewatched:Int?=null,
    val rewatch_value:Int?=null,
    val tags:List<String>?=null,
    val comments:String?=null,
    val start_date:String?=null,
    val finish_date:String?=null
)
