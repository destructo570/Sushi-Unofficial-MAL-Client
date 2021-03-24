package com.destructo.sushi.listener

import com.destructo.sushi.model.database.UserMangaEntity

class AddChapterListener(val clickListener: (manga : UserMangaEntity?) -> Unit) {
    fun onClick(manga: UserMangaEntity?) = clickListener(manga)
}