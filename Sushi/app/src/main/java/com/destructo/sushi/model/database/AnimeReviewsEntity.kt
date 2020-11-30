package com.destructo.sushi.model.database

import androidx.room.Entity
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews

@Entity(tableName = "anime_reviews", primaryKeys = ["id"])
data class AnimeReviewsEntity(
    val reviewList: AnimeReviews?,
    val id:Int,
    val time: Long
)