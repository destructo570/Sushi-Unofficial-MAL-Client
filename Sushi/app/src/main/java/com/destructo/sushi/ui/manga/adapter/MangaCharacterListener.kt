package com.destructo.sushi.ui.manga.adapter

class MangaCharacterListener(val clickListener: (characterMalId: Int?) -> Unit) {
    fun onClick(malId: Int) = clickListener(malId)
}