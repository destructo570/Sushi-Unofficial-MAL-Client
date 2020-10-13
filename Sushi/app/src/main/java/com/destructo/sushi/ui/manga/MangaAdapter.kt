package com.destructo.sushi.ui.manga

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeBinding
import com.destructo.sushi.databinding.ListItemMangaBinding
import com.destructo.sushi.model.top.TopAnimeEntity
import com.destructo.sushi.model.top.TopMangaEntity
import com.destructo.sushi.ui.anime.topAnime.TopAnimeAdapter

class MangaAdapter:ListAdapter<TopMangaEntity, MangaAdapter.ViewHolder>(MangaDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mangaEntity = getItem(position)
        holder.bind(mangaEntity)
    }

    class ViewHolder(val binding: ListItemMangaBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(mangaEntity: TopMangaEntity){
            binding.mangaEntity = mangaEntity
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMangaBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }


}

class MangaDiffUtil:DiffUtil.ItemCallback<TopMangaEntity>(){
    override fun areItemsTheSame(oldItem: TopMangaEntity, newItem: TopMangaEntity): Boolean {
        return oldItem.malId == newItem.malId
                && oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: TopMangaEntity, newItem: TopMangaEntity): Boolean {
        return oldItem.malId == newItem.malId
                && oldItem.title == newItem.title
    }

}