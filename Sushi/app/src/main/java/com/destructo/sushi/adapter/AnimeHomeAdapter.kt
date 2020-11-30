package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeHomeBinding
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.listener.MalIdListener

class AnimeHomeAdapter (private val malIdListener: MalIdListener) :
    ListAdapter<AnimeRankingData, AnimeHomeAdapter.ViewHolder>(AnimeHomeDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemAnimeHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animeEntity: AnimeRankingData, listener: MalIdListener) {
            binding.animeEntity = animeEntity.anime
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

class AnimeHomeDiffUtil : DiffUtil.ItemCallback<AnimeRankingData>() {
    override fun areItemsTheSame(oldItem: AnimeRankingData, newItem: AnimeRankingData): Boolean {
        return oldItem.anime.id == newItem.anime.id
    }

    override fun areContentsTheSame(oldItem: AnimeRankingData, newItem: AnimeRankingData): Boolean {
        return oldItem.anime.title == newItem.anime.title
                && oldItem.anime.id == newItem.anime.id
    }

}