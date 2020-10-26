package com.destructo.sushi.ui.manga.listener

class MangaIdListener(val clickListener: (mangaMalId: Int?) -> Unit) {
    fun onClick(id: Int) = clickListener(id)
}