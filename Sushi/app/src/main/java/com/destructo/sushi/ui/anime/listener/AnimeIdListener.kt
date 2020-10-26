package com.destructo.sushi.ui.anime.listener

import com.destructo.sushi.model.mal.anime.AnimeBasic

class AnimeIdListener(val clickListener: (animeMalId: Int?) -> Unit) {
    fun onClick(id: Int) = clickListener(id)
}