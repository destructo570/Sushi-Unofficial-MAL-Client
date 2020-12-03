package com.destructo.sushi.listener

import com.destructo.sushi.model.jikan.manga.character.Character

class MangaCharacterListener(val clickListener: (character: Character?) -> Unit) {
    fun onClick(character: Character) = clickListener(character)
}