package com.destructo.sushi.ui.anime.characterDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemCharacterBinding
import com.destructo.sushi.databinding.ListItemGenPersonBinding
import com.destructo.sushi.model.jikan.character.PersonEntity

class PersonAdapter(private val personListener: PersonListener) :
    ListAdapter<PersonEntity, PersonAdapter.ViewHolder>(PersonEntityDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemGenPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: PersonEntity, listener: PersonListener) {
            binding.personEntity = person
            binding.personListener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemGenPersonBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(person, personListener)
    }

}

class PersonEntityDiffUtil : DiffUtil.ItemCallback<PersonEntity>() {
    override fun areItemsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
        return oldItem.malId == newItem.malId
    }

    override fun areContentsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
        return oldItem.malId == newItem.malId
                && oldItem.name == newItem.name
    }

}