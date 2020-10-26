package com.destructo.sushi.ui.manga.listener

import com.destructo.sushi.model.jikan.common.Review
import com.destructo.sushi.model.jikan.manga.MangaReview

class MangaReviewListener (val clickListener: (review: MangaReview?) -> Unit) {
    fun onClick(review: MangaReview) = clickListener(review)
}