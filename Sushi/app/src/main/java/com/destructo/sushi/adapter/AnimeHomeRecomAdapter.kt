package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeHomeBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.mal.anime.Anime

class AnimeHomeRecomAdapter(private val malIdListener: MalIdListener) :
    ListAdapter<Anime, AnimeHomeRecomAdapter.ViewHolder>(AnimeDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemAnimeHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animeEntity: Anime, listener: MalIdListener) {
            binding.animeEntity = animeEntity
            binding.animeListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAnimeHomeBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, malIdListener)
    }


}