package com.destructo.sushi.ui.anime.seasonalAnime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeBinding
import com.destructo.sushi.databinding.ListItemSeasonAnimeBinding
import com.destructo.sushi.model.jikan.season.AnimeSubEntity
import com.destructo.sushi.model.jikan.top.TopAnimeEntity


class SeasonAnimeAdapter: ListAdapter<AnimeSubEntity, SeasonAnimeAdapter.ViewHolder>(SeasonAnimeDiffUtil()) {


    class ViewHolder private constructor(val binding: ListItemSeasonAnimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(animeSubEntity: AnimeSubEntity){
            binding.animeSubEntity = animeSubEntity
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSeasonAnimeBinding.inflate(layoutInflater, parent, false)
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
        val animeSubEntity = getItem(position)
        holder.bind(animeSubEntity)
    }


}

class SeasonAnimeDiffUtil: DiffUtil.ItemCallback<AnimeSubEntity>() {
    override fun areItemsTheSame(oldItem: AnimeSubEntity, newItem: AnimeSubEntity): Boolean {
        return oldItem.url == newItem.url
                && oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: AnimeSubEntity, newItem: AnimeSubEntity): Boolean {
        return oldItem.url == newItem.url
                && oldItem.title == newItem.title
    }

}