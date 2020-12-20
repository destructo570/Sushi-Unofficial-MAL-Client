package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemCharacterLargeBinding
import com.destructo.sushi.listener.AnimeCharacterListener
import com.destructo.sushi.model.jikan.character.Character

class AllCharactersAdapter(private val animeCharacterListener: AnimeCharacterListener) :
    ListAdapter<Character, AllCharactersAdapter.ViewHolder>(AllCharacterDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemCharacterLargeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, listener: AnimeCharacterListener) {
            binding.character = character
            binding.characterListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCharacterLargeBinding.inflate(layoutInflater, parent, false)
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

class AllCharacterDiffUtil : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.malId == newItem.malId
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.malId == newItem.malId
                && oldItem.name == newItem.name
    }

}