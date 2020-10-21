package com.destructo.sushi.ui.anime.adapter

import com.destructo.sushi.model.jikan.anime.support.Staff
import com.destructo.sushi.model.mal.anime.AnimeBasic

class AnimeRecommListener(val clickListener: (animeMalId: Int?) -> Unit) {
    fun onClick(anime: AnimeBasic) = clickListener(anime.id)
}