package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemEpisodePreviewBinding
import com.destructo.sushi.listener.MalUrlListener
import com.destructo.sushi.model.jikan.anime.support.EpisodeVideo

class AnimeEpisodePreviewAdapter(private val malUrlListener: MalUrlListener) :
    ListAdapter<EpisodeVideo, AnimeEpisodePreviewAdapter.ViewHolder>(EpisodeVideoDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemEpisodePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: EpisodeVideo, listener: MalUrlListener) {
            binding.episode = character
            binding.listener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemEpisodePreviewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animeEntity = getItem(position)
        holder.bind(animeEntity, malUrlListener)
    }


}