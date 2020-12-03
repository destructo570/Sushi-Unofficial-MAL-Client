package com.destructo.sushi.listener

import com.destructo.sushi.model.jikan.manga.ReviewEntity

class MangaReviewListener (val clickListener: (review: ReviewEntity?) -> Unit) {
    fun onClick(review: ReviewEntity) = clickListener(review)
}