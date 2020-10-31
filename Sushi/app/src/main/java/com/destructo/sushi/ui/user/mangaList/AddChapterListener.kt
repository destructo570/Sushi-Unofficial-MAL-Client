package com.destructo.sushi.ui.user.mangaList

import com.destructo.sushi.model.mal.manga.Manga

class AddChapterListener(val clickListener: (manga : Manga?) -> Unit) {
    fun onClick(manga: Manga?) = clickListener(manga)
}