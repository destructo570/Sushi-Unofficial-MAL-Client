package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemPublishedMangaBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.jikan.person.PublishedManga

class PublishedMangaAdapter (
    val malIdListener: MalIdListener
):
    ListAdapter<PublishedManga, PublishedMangaAdapter.ViewHolder>(PublishedMangaDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemPublishedMangaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(publishedManga: PublishedManga, malIdListener: MalIdListener) {
            binding.publishedManga = publishedManga
            binding.listener = malIdListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPublishedMangaBinding.inflate(layoutInflater, parent, false)
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
        val person = getItem(position)
        holder.bind(person,malIdListener)
    }

}

class PublishedMangaDiffUtil : DiffUtil.ItemCallback<PublishedManga>() {
    override fun areItemsTheSame(oldItem: PublishedManga, newItem: PublishedManga): Boolean {
        return oldItem.manga?.malId == newItem.manga?.malId
    }

    override fun areContentsTheSame(oldItem: PublishedManga, newItem: PublishedManga): Boolean {
        return oldItem.manga == newItem.manga
    }
}