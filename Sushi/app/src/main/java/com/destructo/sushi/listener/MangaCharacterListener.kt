package com.destructo.sushi.listener

class MangaCharacterListener(val clickListener: (characterMalId: Int?) -> Unit) {
    fun onClick(malId: Int) = clickListener(malId)
}