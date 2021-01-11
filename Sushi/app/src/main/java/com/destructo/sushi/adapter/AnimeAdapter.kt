package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.mal.anime.Anime

class AnimeAdapter(private val malIdListener: MalIdListener) :
    ListAdapter<Anime, AnimeAdapter.ViewHolder>(AnimeDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animeEntity: Anime, listener: MalIdListener) {
            binding.animeEntity = animeEntity
            binding.animeListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAnimeBinding.inflate(layoutInflater, parent, false)
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

class AnimeDiffUtil : DiffUtil.ItemCallback<Anime>() {
    override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem.title == newItem.title
                && oldItem.id == newItem.id
    }

}