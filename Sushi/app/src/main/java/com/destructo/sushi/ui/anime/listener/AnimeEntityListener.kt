package com.destructo.sushi.ui.anime.listener

import com.destructo.sushi.model.mal.anime.Anime

class AnimeEntityListener (val clickListener: (anime: Anime?) -> Unit) {
    fun onClick(anime: Anime?) = clickListener(anime)
}