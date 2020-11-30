package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeHomeBinding
import com.destructo.sushi.model.mal.seasonalAnime.SeasonAnimeData
import com.destructo.sushi.listener.MalIdListener

class SeasonAnimeHomeAdapter (private val malIdListener: MalIdListener):
    ListAdapter<SeasonAnimeData, SeasonAnimeHomeAdapter.ViewHolder>( SeasonAnimeHomeDiffUtil()) {


    class ViewHolder private constructor(val binding: ListItemAnimeHomeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(seasonAnime: SeasonAnimeData, listener: MalIdListener){
            binding.animeEntity = seasonAnime.anime
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
        val animeSubEntity = getItem(position)
        holder.bind(animeSubEntity,malIdListener)
    }

}

class  SeasonAnimeHomeDiffUtil: DiffUtil.ItemCallback<SeasonAnimeData>() {
    override fun areItemsTheSame(oldItem: SeasonAnimeData, newItem: SeasonAnimeData): Boolean {
        return oldItem.anime.id == newItem.anime.id
    }

    override fun areContentsTheSame(oldItem: SeasonAnimeData, newItem: SeasonAnimeData): Boolean {
        return oldItem.anime.id == newItem.anime.id
                && oldItem.anime.title == newItem.anime.title
    }

}