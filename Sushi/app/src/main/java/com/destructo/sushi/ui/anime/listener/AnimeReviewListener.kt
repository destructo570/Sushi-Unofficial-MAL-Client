package com.destructo.sushi.ui.anime.listener

import com.destructo.sushi.model.jikan.common.Review

class AnimeReviewListener(val clickListener: (review: Review?) -> Unit) {
    fun onClick(review: Review) = clickListener(review)
}