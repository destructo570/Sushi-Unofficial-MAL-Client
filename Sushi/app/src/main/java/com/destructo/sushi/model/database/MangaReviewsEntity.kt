package com.destructo.sushi.model.database

import androidx.room.Entity
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.manga.MangaReview

@Entity(tableName = "manga_reviews", primaryKeys = ["id"])
data class MangaReviewsEntity(
    val reviewList: MangaReview?,
    val id:Int,
    val time: Long
)