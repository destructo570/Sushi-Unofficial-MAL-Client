package com.destructo.sushi.ui.manga.adapter

import com.destructo.sushi.model.jikan.anime.support.Staff
import com.destructo.sushi.model.mal.anime.AnimeBasic
import com.destructo.sushi.model.mal.manga.MangaBasic

class MangaRecommListener(val clickListener: (mangaMalId: Int?) -> Unit) {
    fun onClick(manga: MangaBasic) = clickListener(manga.id)
}