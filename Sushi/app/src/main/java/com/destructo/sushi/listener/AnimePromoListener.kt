package com.destructo.sushi.listener

import com.destructo.sushi.model.jikan.anime.support.Promo

class AnimePromoListener(val clickListener: (promoVideoUrl: String?) -> Unit) {
    fun onClick(promo: Promo) = clickListener(promo.videoUrl)
}