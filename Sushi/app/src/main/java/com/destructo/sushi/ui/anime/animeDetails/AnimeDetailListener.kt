package com.destructo.sushi.ui.anime.animeDetails

import com.destructo.sushi.model.mal.anime.Anime

class AnimeDetailListener(val clickListener: (animeMalId: Int?) -> Unit) {
    fun onClick(anime: Anime) = clickListener(anime.id)
}