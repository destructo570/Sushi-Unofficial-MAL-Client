package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemMangaCharacterLargeBinding
import com.destructo.sushi.listener.MangaCharacterListener
import com.destructo.sushi.model.jikan.manga.character.Character


class AllMangaCharacterAdapter(private val mangaCharacterListener: MangaCharacterListener) :
    ListAdapter<Character, AllMangaCharacterAdapter.ViewHolder>(MangaCharacterDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemMangaCharacterLargeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, listener: MangaCharacterListener) {
            binding.character = character
            binding.characterListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMangaCharacterLargeBinding.inflate(layoutInflater, parent, false)
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
        val mangaEntity = getItem(position)
        holder.bind(mangaEntity, mangaCharacterListener)
    }


}