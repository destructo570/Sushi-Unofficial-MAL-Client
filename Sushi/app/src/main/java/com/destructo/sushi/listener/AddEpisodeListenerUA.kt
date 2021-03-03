package com.destructo.sushi.listener

import com.destructo.sushi.model.database.UserAnimeEntity
import com.destructo.sushi.model.mal.anime.Anime

class AddEpisodeListenerUA(val clickListener: (anime : UserAnimeEntity?) -> Unit) {
    fun onClick(anime: UserAnimeEntity?) = clickListener(anime)
}