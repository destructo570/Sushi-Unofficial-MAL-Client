package com.destructo.sushi.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeBinding
import com.destructo.sushi.model.mal.animeList.AnimeList
import com.destructo.sushi.model.mal.animeList.AnimeListData
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.ui.anime.adapter.AnimeRankingAdapter
import com.destructo.sushi.ui.anime.listener.AnimeIdListener

class AnimeListAdapter(private val animeIdListener: AnimeIdListener) :
    ListAdapter<AnimeListData, AnimeListAdapter.ViewHolder>(AnimeListDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animeEntity: AnimeListData, listener: AnimeIdListener) {
            binding.animeEntity = animeEntity.anime
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
        holder.bind(animeEntity, animeIdListener)
    }


}

class AnimeListDiffUtil : DiffUtil.ItemCallback<AnimeListData>() {
    override fun areItemsTheSame(oldItem: AnimeListData, newItem: AnimeListData): Boolean {
        return oldItem.anime?.id == newItem.anime?.id
    }

    override fun areContentsTheSame(oldItem: AnimeListData, newItem: AnimeListData): Boolean {
        return oldItem.anime == newItem.anime
    }

}