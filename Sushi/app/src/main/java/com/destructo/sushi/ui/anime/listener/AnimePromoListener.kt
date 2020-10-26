package com.destructo.sushi.ui.anime.listener

import com.destructo.sushi.model.jikan.anime.support.Promo
import com.destructo.sushi.model.jikan.anime.support.Staff

class AnimePromoListener(val clickListener: (promoVideoUrl: String?) -> Unit) {
    fun onClick(promo: Promo) = clickListener(promo.videoUrl)
}