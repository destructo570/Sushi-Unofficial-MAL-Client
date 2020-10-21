package com.destructo.sushi.ui.anime.adapter

import com.destructo.sushi.model.jikan.common.Character

class AnimeCharacterListener(val clickListener: (characterMalId: Int?) -> Unit) {
    fun onClick(character: Character) = clickListener(character.malId)
}