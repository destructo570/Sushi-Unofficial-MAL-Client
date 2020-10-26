package com.destructo.sushi.ui.manga.mangaDetails

import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.manga.Manga


class MangaDetailListener(val clickListener: (mangaMalId: Int?) -> Unit) {
    fun onClick(manga: Manga) = clickListener(manga.id)
}