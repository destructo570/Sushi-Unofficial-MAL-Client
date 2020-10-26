package com.destructo.sushi.ui.manga.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemRelatedMangaBinding
import com.destructo.sushi.model.mal.anime.RelatedAnime
import com.destructo.sushi.model.mal.manga.RelatedManga

class MangaRelatedListAdapter(private val mangaDetailListener: MangaRecommListener) :
    ListAdapter<RelatedManga, MangaRelatedListAdapter.ViewHolder>(MangaRelatedAnimeDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemRelatedMangaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(relatedManga: RelatedManga, listener: MangaRecommListener) {
            binding.relatedManga = relatedManga
            binding.listener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRelatedMangaBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, mangaDetailListener)
    }


}

class MangaRelatedAnimeDiffUtil : DiffUtil.ItemCallback<RelatedManga>() {
    override fun areItemsTheSame(oldItem: RelatedManga, newItem: RelatedManga): Boolean {
        return oldItem.manga?.id == newItem.manga?.id
    }

    override fun areContentsTheSame(oldItem: RelatedManga, newItem: RelatedManga): Boolean {
        return oldItem.manga?.title == newItem.manga?.title
                && oldItem.manga?.id == newItem.manga?.id
    }

}