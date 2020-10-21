package com.destructo.sushi.ui.anime.adapter

import com.destructo.sushi.model.jikan.anime.support.EpisodeVideo
import com.destructo.sushi.model.jikan.anime.support.Promo

class AnimeEpisodeListener(val clickListener: (episodeVideoUrl: String?) -> Unit) {
    fun onClick(episode: EpisodeVideo) = clickListener(episode.videoUrl)
}