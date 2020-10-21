package com.destructo.sushi.ui.anime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.model.jikan.common.Character
import com.destructo.sushi.databinding.ListItemAnimeBinding
import com.destructo.sushi.databinding.ListItemCharacterBinding

class AnimeCharacterListAdapter(private val animeCharacterListener: AnimeCharacterListener) :
    ListAdapter<Character, AnimeCharacterListAdapter.ViewHolder>(AnimeCharacterDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, listener: AnimeCharacterListener) {
            binding.character = character
            binding.characterListener = listener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCharacterBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, animeCharacterListener)
    }


}

class AnimeCharacterDiffUtil : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.malId == newItem.malId
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.malId == newItem.malId
                && oldItem.name == newItem.name
    }

}