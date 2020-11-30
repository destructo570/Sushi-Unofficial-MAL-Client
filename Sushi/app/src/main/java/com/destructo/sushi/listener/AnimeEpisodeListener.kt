package com.destructo.sushi.listener

import com.destructo.sushi.model.jikan.anime.support.EpisodeVideo

class AnimeEpisodeListener(val clickListener: (episodeVideoUrl: String?) -> Unit) {
    fun onClick(episode: EpisodeVideo) = clickListener(episode.videoUrl)
}