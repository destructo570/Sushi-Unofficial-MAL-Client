package com.destructo.sushi.ui.anime.topAnime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.model.top.TopAnimeEntity
import com.destructo.sushi.databinding.ListItemAnimeBinding

class TopAnimeAdapter: ListAdapter<TopAnimeEntity, TopAnimeAdapter.ViewHolder>(TopAnimeDiffUtil()) {


    class ViewHolder private constructor(val binding: ListItemAnimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(animeEntity: TopAnimeEntity){
            binding.animeEntity = animeEntity
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
        holder.bind(animeEntity)
    }


}

class TopAnimeDiffUtil: DiffUtil.ItemCallback<TopAnimeEntity>() {
    override fun areItemsTheSame(oldItem: TopAnimeEntity, newItem: TopAnimeEntity): Boolean {
        return oldItem.url == newItem.url
                && oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: TopAnimeEntity, newItem: TopAnimeEntity): Boolean {
        return oldItem.url == newItem.url
    }

}