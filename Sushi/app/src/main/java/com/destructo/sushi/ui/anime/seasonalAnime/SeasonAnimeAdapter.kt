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
import com.destructo.sushi.model.mal.seasonalAnime.SeasonAnimeData
import com.destructo.sushi.ui.anime.animeDetails.AnimeDetailListener


class SeasonAnimeAdapter(private val animeDetailListener: AnimeDetailListener): ListAdapter<SeasonAnimeData, SeasonAnimeAdapter.ViewHolder>(SeasonAnimeDiffUtil()) {


    class ViewHolder private constructor(val binding: ListItemSeasonAnimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(seasonAnime: SeasonAnimeData,listener: AnimeDetailListener){
            binding.animeSubEntity = seasonAnime.anime
            binding.animeListener = listener
            binding.executePendingBindings()

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
        holder.bind(animeSubEntity,animeDetailListener)
    }

}

class SeasonAnimeDiffUtil: DiffUtil.ItemCallback<SeasonAnimeData>() {
    override fun areItemsTheSame(oldItem: SeasonAnimeData, newItem: SeasonAnimeData): Boolean {
        return oldItem.anime?.id == newItem.anime?.id
    }

    override fun areContentsTheSame(oldItem: SeasonAnimeData, newItem: SeasonAnimeData): Boolean {
        return oldItem.anime?.id == newItem.anime?.id
                && oldItem.anime?.title == newItem.anime?.title
    }

}