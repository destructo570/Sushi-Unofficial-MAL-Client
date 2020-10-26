package com.destructo.sushi.ui.manga.listener

class MangaCharacterListener(val clickListener: (characterMalId: Int?) -> Unit) {
    fun onClick(malId: Int) = clickListener(malId)
}