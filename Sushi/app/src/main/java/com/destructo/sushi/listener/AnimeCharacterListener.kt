package com.destructo.sushi.listener

class AnimeCharacterListener(val clickListener: (characterMalId: Int?) -> Unit) {
    fun onClick(malId: Int) = clickListener(malId)
}