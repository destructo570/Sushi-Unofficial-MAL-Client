package com.destructo.sushi.ui.manga.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemMangaCharacterBinding
import com.destructo.sushi.model.jikan.manga.character.Character


class MangaCharacterAdapter(private val mangaCharacterListener: MangaCharacterListener) :
    ListAdapter<Character, MangaCharacterAdapter.ViewHolder>(MangaCharacterDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemMangaCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, listener: MangaCharacterListener) {
            binding.character = character
            binding.characterListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMangaCharacterBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, mangaCharacterListener)
    }


}

class MangaCharacterDiffUtil : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.malId == newItem.malId
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.malId == newItem.malId
                && oldItem.name == newItem.name
    }

}